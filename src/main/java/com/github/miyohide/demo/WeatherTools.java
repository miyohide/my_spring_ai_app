package com.github.miyohide.demo;

import org.springframework.ai.tool.annotation.Tool;

public class WeatherTools {
    @Tool(description = "Get the current weather in city")
    public String getWeather(String city) {
        return "The weather in " + city + " is sunny";
    }
}
