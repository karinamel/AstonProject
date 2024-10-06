package org.example.astonproject.services;

import lombok.RequiredArgsConstructor;
import org.example.astonproject.dto.WeatherInfoDto;
import org.example.astonproject.dto.WeatherstackResponse;
import org.example.astonproject.exceptions.BadRequestException;
import org.example.astonproject.integrations.WeatherstackClient;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WeatherInfoService {
    private final WeatherstackClient weatherStackClient;

    public WeatherInfoDto getWeatherInfo(String city) {
        WeatherstackResponse weatherForecast = weatherStackClient.getWeatherInfo(city);
        if (weatherForecast.getError() != null) {
            throw new BadRequestException(weatherForecast.getError().getInfo());
        }

        WeatherInfoDto weatherInfo = new WeatherInfoDto();
        weatherInfo.setCity(city);
        weatherInfo.setTemperature(weatherForecast.getCurrent().getTemperature());
        weatherInfo.setWeatherDescription(String.join(", ",
                weatherForecast.getCurrent().getWeatherDescription()
        ));
        return weatherInfo;
    }
}
