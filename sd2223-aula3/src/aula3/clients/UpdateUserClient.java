package aula3.clients;

import aula3.api.User;

import java.io.IOException;
import java.net.URI;

public class UpdateUserClient {

	public static void main(String[] args) throws IOException {
		
		if( args.length != 6) {
			System.err.println( "Use: java aula3.clients.UpdateUserClient url userId oldpwd fullName email password");
			return;
		}
		
		String serverUrl = args[0];
		String userId = args[1];
		String pwd = args[2];
		String fullName = args[3];
		String email = args[4];
		String password = args[5];
		
		var u = new User( userId, fullName, email, password);
		
		System.out.println("Sending request to server.");

		var res = new RestUsersClient(URI.create(serverUrl)).updateUser(userId, pwd, u);
		System.out.println("Result: " + res);
	}
	
}
