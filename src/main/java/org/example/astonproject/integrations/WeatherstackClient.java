package org.example.astonproject.integrations;

import lombok.RequiredArgsConstructor;
import org.example.astonproject.config.properties.WeatherstackProperties;
import org.example.astonproject.dto.WeatherstackResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class WeatherstackClient {
    private final RestTemplate restTemplate;
    private final WeatherstackProperties weatherstackProperties;

    public WeatherstackResponse getWeatherInfo(String city) {
        return restTemplate.getForObject(
                weatherstackProperties.getBaseUrl() + "/current?access_key=" +
                        weatherstackProperties.getAccessKey() + "&query=" + city,
                WeatherstackResponse.class
        );
    }
}
