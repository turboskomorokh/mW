package by.saunear.mW.l10n;

public class Constants {
    public static String DATABASE_CONNECTION_ESTABLISHED = "Database connection has been established.";
    public static String DATABASE_CONNECTION_NO_DRIVER_ERROR = "Database driver SQLite JDBC not found.";
    public static String DATABASE_CONNECTION_ERROR = "Database connection failed!";
    public static String DATABASE_CONNECTION_CLOSE_ERROR = "Database connection close failed.";
    public static String DATABASE_CONNECTION_CLOSED = "Database connection closed.";
    public static String DATABASE_TABLE_CREATION_ERROR = "Database table initialization failed!";
    public static String DATABASE_TABLE_INSERTION_ERROR = "Database player insertion failed!";
    public static String DATABASE_TABLE_DELETION_ERROR = "Database player deletion failed!";
    public static String DATABASE_TABLE_ENTRY_ERROR = "Database entry access error.";
    public static String DATABASE_TABLE_ENTRIES_ERROR = "Database entries access error.";
    public static String WHITELIST_JOIN_REGISTER_MESSAGE = "Register your player name!";
    public static String WHITELIST_JOIN_UNREGISTERED_PLAYER = "Unregistered player %playerName trying to connect!";
    public static String WHITELIST_JOIN_REGISTERED_PLAYER = "Registered player %playerName trying to connect!";
    public static String WHITELIST_ADD_SUCCESS = "Player %playerName added successfully.";
    public static String WHITELIST_ADD_FAILURE = "An error occured while adding player %playerName.";
    public static String WHITELIST_REMOVE_SUCCESS = "Player %playerName removed successfully.";
    public static String WHITELIST_REMOVE_FAILURE = "An error occured while removing player %playerName.";
    public static String WHITELIST_REMOVE_NOT_EXIST = "Player %playerName is not on whitelist!";
    public static String PLUGIN_COMMAND_HELP = "\nmwl add <name>\t\t- add player to whitelist\nmwl remove <name>\t- remove player from whitelist\nmwl list\t\t- list players on whitelist\n";
    public static String PLUGIN_COMMAND_NO_PERMISSION = "You don't have a permission to execute this command!";
    public static String PLUGIN_COMMAND_NO_WHITELIST_PLAYERS = "No players are on whitelist!";
    public static String PLUGIN_COMMAND_NOT_ENOUGH_ARGUMENTS = "Not enough arguments.";
    public static String PLUGIN_COMMAND_WRONG_PLAYERNAME = "Wrong player name!";
    public static String TELEGRAM_BOT_CONFIG_INVALID = "Bot configuration is invalid.";
    public static String TELEGRAM_BOT_INITIALIZED = "Telegram bot initialized.";
    public static String TELEGRAM_REGISTER_BEGIN = "Registration";
    public static String TELEGRAM_REGISTER_RESET = "Registration reset";
    public static String TELEGRAM_REGISTER_ASK_PLAYERNAME = "Write your player name";
    public static String TELEGRAM_REGISTER_PLAYERNAME_WRONG = "The name %playerName is incorrect, try another one";
    public static String TELEGRAM_REGISTER_PLAYERNAME_CLAIMED = "Name %playerName is already claimed";
    public static String TELEGRAM_REGISTER_PLAYERNAME_CONFIRMATION = "Register as %playerName? Confirm with 'Yes'";
    public static String TELEGRAM_REGISTER_SUCCESS = "Successfully registered you as %playerName.";
}
