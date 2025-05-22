package com.github.miyohide.demo;

import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class MyController {
  private final ChatModel chatModel;

  public MyController(ChatModel chatModel) {
    this.chatModel = chatModel;
  }

  @GetMapping("/ai/gen")
  public Map<String, String> getWeather() {
    String response = ChatClient.create(this.chatModel).prompt("東京の天気は？").call().content();
    return Map.of("gen", response);
  }
}
