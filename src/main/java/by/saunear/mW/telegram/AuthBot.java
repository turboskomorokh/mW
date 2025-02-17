package by.saunear.mW.telegram;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import by.saunear.mW.core.Whitelist;
import by.saunear.mW.l10n.Messages;

public class AuthBot extends TelegramLongPollingBot {

    private final Map<Long, Map<String, Object>> userRegistrations = new HashMap<>();
    private final Whitelist wl;
    Messages messages;
    private final Logger logger;
    private boolean enabled;
    private final String token;
    private final String botName;

    @SuppressWarnings("deprecation")
    public AuthBot(Logger logger, Messages messages, Map<String, Object> config, Whitelist wl) {
        this.logger = logger;
        this.messages = messages;
        this.wl = wl;

        this.enabled = (boolean) config.getOrDefault("enabled", false);
        this.token = (String) config.getOrDefault("bot-token", "");
        this.botName = (String) config.getOrDefault("bot-name", "");

        if (token.isEmpty() || botName.isEmpty()) {
            this.logger.error("Invalid Telegram bot configuration.");
            this.enabled = false;
        }
    }

    public void start() {
        if (!enabled || !wl.enabled) {
            return;
        }
        logger.info(messages.get("TELEGRAM_BOT_INITIALIZED"));
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(this);
        } catch (TelegramApiException e) {
            logger.error(e.getMessage());
        }
    }

    private void sendMessage(long chatId, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(text);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            logger.error(e.getMessage());
        }
    }

    private void startRegistration(Update update, Map<String, Object> user) {
        long chatId = update.getMessage().getChatId();
        user.put("stage", 1);
        user.put("tgid", chatId);
        user.put("username", null);

        sendMessage(chatId, messages.get("TELEGRAM_REGISTER_BEGIN"));
        sendMessage(chatId, messages.get("TELEGRAM_REGISTER_ASK_PLAYERNAME"));
    }

    private void stage1(Update update, Map<String, Object> user) {
        long chatId = update.getMessage().getChatId();
        String message = update.getMessage().getText();

        if (!message.matches("^[a-zA-Z0-9-_]+$") || message.length() < 4 || message.length() > 16) {
            sendMessage(chatId,
            messages.get("TELEGRAM_REGISTER_PLAYERNAME_WRONG").replace("%playerName", (String) user.get("username")));
            return;
        }
        if (wl.check(message)) {
            sendMessage(chatId,
            messages.get("TELEGRAM_REGISTER_PLAYERNAME_CLAIMED").replace("%playerName", (String) user.get("username")));
            return;
        }

        user.put("username", message);
        sendMessage(chatId,
                messages.get("TELEGRAM_REGISTER_PLAYERNAME_CONFIRMATION").replace("%playerName", (String) user.get("username")));
        user.put("stage", 2);
    }

    private void stage2(Update update, Map<String, Object> user) {
        long chatId = update.getMessage().getChatId();
        String message = update.getMessage().getText();
        if (message.toLowerCase().charAt(0) != 'y') {
            sendMessage(chatId, messages.get("TELEGRAM_REGISTER_RESET"));
            user.put("stage", 0);
            return;
        }
        sendMessage(chatId, messages.get("TELEGRAM_REGISTER_SUCCESS").replace("%playerName", (String) user.get("username")));
        wl.add((String) user.get("username"), (Long) user.get("tgid"));
    }

    @Override
    public void onUpdateReceived(Update update) {
        long chatId = update.getMessage().getChatId();
        String message = update.getMessage().getText();

        userRegistrations.putIfAbsent(chatId, new HashMap<>());
        Map<String, Object> userData = userRegistrations.get(chatId);

        if ("/register".equals(message)) {
            startRegistration(update, userData);
            return;
        }
        if ("/reset".equals(message)) {
            sendMessage(chatId, messages.get("TELEGRAM_REGISTER_RESET"));
            return;
        }

        int stage = (int) userData.getOrDefault("stage", 0);

        switch (stage) {
            case 0:
                startRegistration(update, userData);
                break;
            case 1:
                stage1(update, userData);
                break;
            case 2:
                stage2(update, userData);
                break;
            default:
                break;
        }
    }

    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public String getBotToken() {
        return token;
    }
}
