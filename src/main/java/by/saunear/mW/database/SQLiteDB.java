package by.saunear.mW.database;

import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;

import by.saunear.mW.l10n.Messages;

public class SQLiteDB {

	private Connection conn = null;
	private String path;

	private Logger logger;

	public SQLiteDB(String path, Logger logger) {
		this.path = (Constants.urlPrefix) + Paths.get(path).resolve(Constants.urlFilename).toString();
		this.logger = logger;
		this.connect();
		this.init();
	}

	public void connect() {
		try {
			// Ручная проверка драйвера
			Class.forName("org.sqlite.JDBC");

			this.conn = DriverManager.getConnection(path.toString());
		} catch (ClassNotFoundException e) {
			logger.error(Messages.DATABASE_CONNECTION_ESTABLISHED + "\n" + e.getMessage());
			throw new RuntimeException(e.getMessage());
		} catch (SQLException e) {
			logger.error(Messages.DATABASE_CONNECTION_ERROR + "\n" + e.getMessage());
			throw new RuntimeException(e.getMessage());
		}
		logger.info(Messages.DATABASE_CONNECTION_ESTABLISHED);
	}

	public void init() {
		try {
			Statement stmt = conn.createStatement();
			stmt.execute(Constants.sqlCreateWhitelist);
		} catch (SQLException e) {
			logger.error(Messages.DATABASE_TABLE_CREATION_ERROR);
			throw new RuntimeException(e.getMessage());
		}
	}

	public void insert(String playerName, Long id) {
		try {
			PreparedStatement pstmt = conn.prepareStatement(Constants.sqlInsertPlayerName);
			pstmt.setString(1, playerName);
			pstmt.setLong(2, id);
			pstmt.setBoolean(3, true);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			logger.error(Messages.DATABASE_TABLE_INSERTION_ERROR);
			throw new RuntimeException(e.getMessage());
		}
	}

	public void delete(String playerName) {
		try {
			PreparedStatement pstmt = conn.prepareStatement(Constants.sqlDeletePlayerName);
			pstmt.setString(1, playerName);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			logger.error(Messages.DATABASE_TABLE_DELETION_ERROR);
			throw new RuntimeException(e.getMessage());
		}
	}

	public boolean exists(String playerName) {
		try {
			PreparedStatement pstmt = conn.prepareStatement(Constants.sqlSelectPlayerName);
			pstmt.setString(1, playerName);
			ResultSet rs = pstmt.executeQuery();
			return rs.next();
		} catch (SQLException e) {
			logger.error(Messages.DATABASE_TABLE_ENTRY_ERROR);
			throw new RuntimeException(e.getMessage());
		}
	}

	public Map<Long, String> getAll() {
		Map<Long, String> playersList = new HashMap<Long, String>();
		String sql = "SELECT * FROM whitelist";
		try {
			PreparedStatement pstmt = conn.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				String playerName = rs.getString("name");
				long playerTgId = rs.getLong("tgid");
				playersList.put(playerTgId, playerName);
			}
		} catch (SQLException e) {
			logger.error(Messages.DATABASE_TABLE_ENTRIES_ERROR);
			throw new RuntimeException(e.getMessage());
		}
		return playersList;
	}

	public void close() {
		try {
			if (conn != null && !conn.isClosed()) {
				conn.close();
				logger.info(Messages.DATABASE_CONNECTION_CLOSED);
			}
		} catch (SQLException e) {
			logger.error(Messages.DATABASE_CONNECTION_CLOSE_ERROR);
			throw new RuntimeException(e.getMessage());
		}
	}
}
