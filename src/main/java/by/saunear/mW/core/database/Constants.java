package by.saunear.mW.core.database;

public class Constants {
	
	public static String sqlCreateWhitelist = "CREATE TABLE IF NOT EXISTS whitelist (name TEXT NOT NULL, tgid bigint NOT NULL, active bool NOT NULL);";
	public static String sqlInsertPlayerName = "INSERT INTO whitelist(name, tgid, active) VALUES(?, ?, ?);";
	public static String sqlDeletePlayerName = "DELETE FROM whitelist WHERE name = ?;";
	public static String sqlSelectPlayerName = "SELECT 1 FROM whitelist WHERE name = ?;";

	public static String urlPrefix = "jdbc:sqlite:";
	public static String urlFilename = "mwuser.db";
}
