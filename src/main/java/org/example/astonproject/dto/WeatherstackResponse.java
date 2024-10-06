package org.example.astonproject.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherstackResponse {
    private Current current;
    private Error error;

    @Data
    public static class Current {
        private int temperature;
        @JsonProperty("weather_descriptions")
        private String[] weatherDescription;
    }

    @Data
    public static class Error {
        private int code;
        private String type;
        private String info;
    }
}
