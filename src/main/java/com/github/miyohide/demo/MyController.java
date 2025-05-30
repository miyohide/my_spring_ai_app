package com.github.miyohide.demo;

import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.ChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@Slf4j
public class MyController {
  private final ChatClient chatClient;
  private final ChatMemoryRepository chatMemoryRepository;

  public MyController(ChatClient.Builder builder, ChatMemoryRepository chatMemoryRepository) {
    ChatMemory chatMemory = MessageWindowChatMemory.builder().build();
    this.chatClient =
        builder.defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build()).build();
    this.chatMemoryRepository = chatMemoryRepository;
  }

  @PostMapping("/chat")
  public Map<String, String> chat(
      @RequestParam(value = "message", defaultValue = "Tell me a joke") String message) {
    String response = this.chatClient.prompt(message).call().content();
    return Map.of("response", response);
  }

  @GetMapping("/chatstream")
  public Flux<String> chatStream(
      @RequestParam(value = "message", defaultValue = "Tell me a joke") String message) {
    return this.chatClient.prompt(message).stream().content();
  }

  @GetMapping("/chathistories")
  public List<String> getChatHistories() {
      return chatMemoryRepository.findConversationIds();
  }

  @GetMapping("/chathistories/{id}")
  public String getChatHitory(@PathVariable("id") String id) {
      return chatMemoryRepository.findByConversationId(id).getFirst().toString();
  }
}
