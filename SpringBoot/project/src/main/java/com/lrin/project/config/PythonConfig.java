package com.lrin.project.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;

@Configuration
public class PythonConfig {

    @Value("${external.server.url}")
    private String externalServerUrl;

    @Bean
    WebClient webClient() {
        return WebClient.builder()
                .exchangeStrategies(ExchangeStrategies.builder()
                        .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(-1))
                        .build())
                .baseUrl(externalServerUrl) // 파이썬 AI 서버 주소
                .filter(ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
                    ClientRequest filteredRequest = ClientRequest.from(clientRequest)
                            .header("Authorization", "Bearer YOUR_TOKEN_HERE") // 여기에 토큰 추가
                            .build();
                    return Mono.just(filteredRequest);
                }))
                .build();
    }


}//end class