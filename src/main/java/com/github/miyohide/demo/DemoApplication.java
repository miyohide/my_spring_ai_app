package com.github.miyohide.demo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import lombok.extern.slf4j.Slf4j;
@SpringBootApplication
@Slf4j
public class DemoApplication implements CommandLineRunner {

  public static void main(String[] args) {
    new SpringApplicationBuilder(DemoApplication.class).web(WebApplicationType.NONE).run(args);
  }

  @Override
  public void run(String... args) throws Exception {
    log.info("Hello World!");
  }
}
