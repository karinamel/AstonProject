package org.example.astonproject.dto;

import lombok.Data;

@Data
public class WeatherInfoDto {
    private String city;
    private int temperature;
    private String WeatherDescription;
}
