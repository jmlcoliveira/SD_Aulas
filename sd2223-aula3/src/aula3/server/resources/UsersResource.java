package aula3.server.resources;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import aula3.api.User;
import aula3.api.rest.UsersService;
import jakarta.inject.Singleton;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response.Status;

@Singleton
public class UsersResource implements UsersService {

	private final Map<String,User> users = new HashMap<>();

	private static Logger Log = Logger.getLogger(UsersResource.class.getName());

	public UsersResource() {
	}

	@Override
	public String createUser(User user) {
		Log.info("createUser : " + user);

		// Check if user data is valid
		if(user.getName() == null || user.getPwd() == null || user.getDisplayName() == null || user.getDomain() == null) {
			Log.info("User object invalid.");
			throw new WebApplicationException( Status.BAD_REQUEST );
		}

		// Insert user, checking if name already exists
		if( users.putIfAbsent(user.getName(), user) != null ) {
			Log.info("User already exists.");
			throw new WebApplicationException( Status.CONFLICT );
		}

		return user.getName();
	}

	@Override
	public User getUser(String userId, String password) {
		Log.info("getUser : user = " + userId + "; pwd = " + password);

		return validateUser(userId, password);
	}

	@Override
	public User updateUser(String userId, String password, User new_user) {
		Log.info("updateUser : user = " + userId + "; pwd = " + password + " ; user = " + new_user);

		var user = validateUser(userId, password);

		users.replace(userId, new_user);

		return new_user;
	}

	private User validateUser(String userId, String password){
		// Check if user is valid
		if(userId == null || password == null) {
			Log.info("UserId or password null.");
			throw new WebApplicationException( Status.BAD_REQUEST );
		}

		var user = users.get(userId);

		// Check if user exists
		if( user == null ) {
			Log.info("User does not exist.");
			throw new WebApplicationException( Status.NOT_FOUND );
		}

		//Check if the password is correct
		if( !user.getPwd().equals( password)) {
			Log.info("Password is incorrect.");
			throw new WebApplicationException( Status.FORBIDDEN );
		}

		return user;
	}

	@Override
	public User deleteUser(String userId, String password) {
		Log.info("deleteUser : user = " + userId + "; pwd = " + password);

		var user = validateUser(userId, password);

		return users.remove(userId);
	}

	@Override
	public List<User> searchUsers(String pattern) {
		Log.info("searchUsers : pattern = " + pattern);

		if(pattern == null) return new LinkedList<>(users.values());

		List<User> l = new LinkedList<>();
		for(User u : users.values()) {
			if (u.getDisplayName().contains(pattern))
				l.add(u);
		}
		return l;
	}

}
