package aula5.api.java;

import java.util.List;

import aula5.api.User;

public interface Users {

	Result<String> createUser(User user);
	
	Result<User> getUser(String name, String pwd);
	
	Result<User> updateUser(String name, String pwd, User user);
	
	Result<User> deleteUser(String name, String pwd);
	
	Result<List<User>> searchUsers(String pattern);	
	
	Result<Void> verifyPassword( String name, String pwd);
}
