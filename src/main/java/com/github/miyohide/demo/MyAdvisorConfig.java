package com.github.miyohide.demo;

import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyAdvisorConfig {
  @Bean
  public SimpleLoggerAdvisor simpleLoggerAdvisor() {
    return new SimpleLoggerAdvisor(
        request -> "Custom request: " + request.prompt().getUserMessage(),
        response -> "Custom response: " + response.getMetadata(),
        0);
  }
}
