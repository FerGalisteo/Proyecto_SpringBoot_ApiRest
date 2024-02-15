package com.dwes.security.service.impl;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.dwes.security.dto.response.PexelsResponse;

import jakarta.annotation.PostConstruct;
import reactor.core.publisher.Mono;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

@Component
public class PexelsWebClient {
	private static final Logger logger = LoggerFactory.getLogger(PexelsWebClient.class);

	private WebClient webClient = null;

	@Value("${pexels.key}")
	private String pexelsKey;

	@PostConstruct
	public void init() {

		this.webClient = WebClient.builder().baseUrl("https://api.pexels.com/v1") // Quita el paréntesis al final
				.defaultHeader("Authorization", pexelsKey)
				.build();
	}

	public Mono<PexelsResponse> buscarImagenes(String query) {
		return this.webClient.get().uri(uriBuilder -> uriBuilder.path("/search").queryParam("query", query)
				// .queryParam("per_page", 1)
				.build()).retrieve()

				.bodyToMono(PexelsResponse.class)
				// Log the JSON response
				.doOnNext(response -> logger.info("Respuesta JSON: {}", response.toString())); // Utiliza
																								// response.toString()
																								// para imprimir la
																								// información );
	}
}
