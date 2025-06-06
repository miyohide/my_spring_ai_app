package com.github.miyohide.demo;

import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyAdvisorConfig {
  @Bean
  public SimpleLoggerAdvisor simpleLoggerAdvisor() {
    return new SimpleLoggerAdvisor(
        request -> "Custom request: " + request.toString(),
        response -> "Custom response: " + response.getResult(),
        0
    );
  }
}
