package com.github.miyohide.demo;

import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

import org.springframework.ai.chat.client.ChatClient;
// import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
// @CrossOrigin
@Slf4j
public class MyController {
  private final ChatClient chatClient;

  public MyController(ChatClient.Builder builder) {
    this.chatClient = builder.build();
  }

  @PostMapping("/chat")
  public Map<String, String> chat(
    @RequestParam(value = "message", defaultValue = "Tell me a joke") String message
  ) {
    String response = this.chatClient.prompt(message).call().content();
    return Map.of("response", response);
  }

  @GetMapping("/chatstream")
  public Flux<String> chatStream(
    @RequestParam(value = "message", defaultValue = "Tell me a joke") String message
  ) {
    return this.chatClient.prompt(message).stream().content();
  }
}
