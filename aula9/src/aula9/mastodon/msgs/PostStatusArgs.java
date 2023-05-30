package aula9.mastodon.msgs;

public record PostStatusArgs(String status, String visibility) {

	public PostStatusArgs(String msg) {
		this(msg, "private");
	}
}