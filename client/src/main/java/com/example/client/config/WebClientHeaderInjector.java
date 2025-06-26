package com.example.client.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Configuration
public class WebClientHeaderInjector {

    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder()
                .filter((request, next) -> {
                    Map<String, String> headers = HeaderContext.get();

                    if (headers != null && !headers.isEmpty()) {
                        System.out.println("➡ Adding headers to WebClient request:");
                        headers.forEach((key, value) ->
                                System.out.println("   " + key + ": " + value)
                        );

                        ClientRequest newRequest = ClientRequest.from(request)
                                .headers(httpHeaders -> headers.forEach(httpHeaders::set))
                                .build();

                        return next.exchange(newRequest);
                    }

                    System.out.println("⚠ No headers found in HeaderContext");
                    return next.exchange(request);
                });
    }
}