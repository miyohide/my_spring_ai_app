package com.github.miyohide.demo;

import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@Slf4j
public class MyController {
  private final ChatClient chatClient;

  public MyController(ChatClient.Builder builder) {
    this.chatClient = builder.build();
  }

  @GetMapping("/ai/gen")
  public Map<String, String> getWeather() {
    String response = this.chatClient.prompt("東京の天気は？").call().content();
    return Map.of("gen", response);
  }

  @GetMapping("/ai/genstream")
  public Flux<String> getWeatherStream() {
    return this.chatClient.prompt("東京の天気は？").stream().content();
  }
}
