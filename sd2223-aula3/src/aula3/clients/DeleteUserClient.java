package aula3.clients;

import java.io.IOException;
import java.net.URI;

public class DeleteUserClient {

	public static void main(String[] args) throws IOException {
		
		if( args.length != 3) {
			System.err.println( "Use: java aula3.clients.DeleteUserClient url userId password");
			return;
		}
		
		String serverUrl = args[0];
		String userId = args[1];
		String password = args[2];
		
		System.out.println("Sending request to server.");

		var res = new RestUsersClient(URI.create(serverUrl)).deleteUser(userId, password);
		System.out.println("Result: " + res);
	}
}
