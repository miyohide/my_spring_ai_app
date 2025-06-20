package com.github.miyohide.demo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
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

  public MyController(
      ChatClient.Builder builder,
      ChatMemoryRepository chatMemoryRepository,
      SimpleLoggerAdvisor simpleLoggerAdvisor) {
    ChatMemory chatMemory =
        MessageWindowChatMemory.builder().chatMemoryRepository(chatMemoryRepository).build();
    this.chatClient =
        builder
            .defaultAdvisors(
                MessageChatMemoryAdvisor.builder(chatMemory).build(), simpleLoggerAdvisor)
            .build();
    this.chatMemoryRepository = chatMemoryRepository;
  }

  @PostMapping("/chat")
  public Map<String, String> chat(
      @RequestParam(value = "message", defaultValue = "Tell me a joke") String message,
      @RequestParam(value = "historyId", defaultValue = "") String historyId) {
    // historyIdが指定されなかった場合、日付情報を元に生成する
    if (historyId.isBlank()) {
      LocalDateTime now = LocalDateTime.now();
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
      historyId = now.format(formatter);
    }
    // advisorsにてhistoryIdを指定したいが、実質的にfinalでないとコンパイルエラーとなるため、
    // 実質的にfinalとなるように別の変数に格納する
    String finalHistoryId = historyId;
    String response =
        this.chatClient
            .prompt(message)
            .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, finalHistoryId))
            .call()
            .content();
    return Map.of("response", response, "historyId", historyId);
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
    return chatMemoryRepository.findByConversationId(id).toString();
  }

  @GetMapping("/weather")
  public String getWheather(
      @RequestParam(value = "city", defaultValue = "Tokyo") String city) {
    return this.chatClient
        .prompt("What is the wheather in " + city + "?")
        .tools(new WeatherTools())
        .call()
        .content();
  }
}
