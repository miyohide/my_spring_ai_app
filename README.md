# Spring Boot Chat Demo with Amazon Bedrock

A Spring Boot application that implements a chat interface using Spring AI with Amazon Bedrock's Nova-Lite model.

## Prerequisites

- Java 21
- AWS Account with Bedrock access
- AWS credentials configured
- EC2 Instance

## Building and Running

Use the included Gradle wrapper to build and run the application:

```bash
./gradlew bootRun
```
## Usage

The application provides two chat interfaces:

1. Basic Chat Interface
   - Access at `http://localhost:8080`
   - Uses HTMX for dynamic updates

2. Streaming Chat Interface
   - Access at `http://localhost:8080/stream.html`
   - Provides real-time streaming responses

## API Endpoints

The application exposes two endpoints:

- `/chat` - Regular chat endpoint
- `/chatstream` - Streaming response endpoint

## Development

The project uses:
- Spring AI 1.0.0
- Spotless with Google Java Format
- Lombok

To format the code:
```bash
./gradlew spotlessApply
```
