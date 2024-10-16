package org.example.astonproject.integrations;

import org.example.astonproject.config.properties.TelegramBotProperties;
import org.example.astonproject.dto.WeatherInfoDto;
import org.example.astonproject.enums.BotState;
import org.example.astonproject.services.WeatherInfoService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.HashMap;
import java.util.Map;

import static org.example.astonproject.enums.BotState.*;

@Component
public class TelegramBot extends TelegramLongPollingBot {
    private static final String WEATHER_MESSAGE_FORMAT = "Local temperature is %d Celsius, %s";
    private static final String START_MESSAGE = """
            Welcome to the Bot Assistant!
            To find out the weather forecast, send a command /weather
            """;
    private static final String ENTER_CITY_MESSAGE = "Enter the city";
    private static final String ERROR_MESSAGE = "There was an error sending the command. Check command.";

    private final TelegramBotProperties botProperties;
    private final WeatherInfoService weatherInfoService;
    private final Map<Long, BotState> botStateMap = new HashMap<>();

    public TelegramBot(TelegramBotProperties telegramBotProperties, WeatherInfoService weatherInfoService) {
        super(telegramBotProperties.getToken());
        this.botProperties = telegramBotProperties;
        this.weatherInfoService = weatherInfoService;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (!update.hasMessage() || !update.getMessage().hasText()) {
            return;
        }

        String text = update.getMessage().getText();
        long chatId = update.getMessage().getChatId();

        switch (text) {
            case "/start":
                sendStartMessage(chatId);
                break;
            case "/weather":
                sendWaitingCityMessage(chatId);
                break;
            default:
                if (botStateMap.get(chatId).equals(WAITING_FOR_CITY)) {
                    sendWeatherInfo(chatId, text);
                    return;
                }
                sendStartMessage(chatId);
        }

    }

    private void sendWeatherInfo(long chatId, String city) {
        botStateMap.put(chatId, START);

        try {
            WeatherInfoDto weatherInfo = weatherInfoService.getWeatherInfo(city);
            String message = WEATHER_MESSAGE_FORMAT.formatted(
                    weatherInfo.getTemperature(), weatherInfo.getWeatherDescription().toLowerCase()
            );

            sendMessage(chatId, message);
        } catch (Exception e) {
            sendMessage(chatId, ERROR_MESSAGE);
        }
    }

    private void sendStartMessage(long chatId) {
        botStateMap.put(chatId, BotState.START);
        sendMessage(chatId, START_MESSAGE);
    }

    private void sendWaitingCityMessage(long chatId) {
        botStateMap.put(chatId, WAITING_FOR_CITY);
        sendMessage(chatId, ENTER_CITY_MESSAGE);
    }

    private void sendMessage(long chatId, String message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(message);

        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getBotUsername() {
        return botProperties.getName();
    }
}
