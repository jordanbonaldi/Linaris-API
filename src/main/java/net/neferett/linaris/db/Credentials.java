package net.neferett.linaris.db;

public final class Credentials {

	private final int		database;
	private final String	host;
	private final String	password;
	private final String	username;

	public Credentials(final String host, final String password, final int database) {
		this(host, null, password, database);
	}

	public Credentials(final String host, final String username, final String password) {
		this(host, username, password, 0);
	}

	public Credentials(final String host, final String username, final String password, final int database) {
		this.host = host;
		this.username = username;
		this.password = password;
		this.database = database;
	}

	public int database() {
		return this.database;
	}

	public String getHost() {
		return this.host;
	}

	public String getPassword() {
		return this.password;
	}

	public String getUsername() {
		return this.username;
	}

}
