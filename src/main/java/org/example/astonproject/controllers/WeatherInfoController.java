package org.example.astonproject.controllers;


import lombok.RequiredArgsConstructor;
import org.example.astonproject.dto.WeatherInfoDto;
import org.example.astonproject.services.WeatherInfoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/weather")
@RequiredArgsConstructor
public class WeatherInfoController {
    private final WeatherInfoService weatherInfoService;

    @GetMapping
    public WeatherInfoDto getWeatherInfo(@RequestParam String city) {
        return weatherInfoService.getWeatherInfo(city);
    }
}
