/*
 * Copyright 2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.greglturnquist.hackingspringboot.reactive.client;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * @author Greg Turnquist
 */
// tag::setup[]
@SpringBootTest
@AutoConfigureWebTestClient
public class RSocketTest {

	@Autowired WebTestClient webTestClient;

	@Autowired ItemRepository repository;

	@Test
	void verifyRemoteOperationsThroughRSocketFireAndForget() throws InterruptedException {
		this.repository.deleteAll().as(StepVerifier::create).verifyComplete();

		this.webTestClient.post().uri("/items/fire-and-forget") // <1>
				.bodyValue(new Item("Alf alarm clock", "nothing important", 19.99)) 
				.exchange() 
				.expectStatus().isCreated() 
				.expectBody().isEmpty();

		Thread.sleep(500); // <4>

		this.repository.findAll() 
				.as(StepVerifier::create) 
				.expectNextMatches(item -> {
					assertThat(item.getId()).isNotNull();
					assertThat(item.getName()).isEqualTo("Alf alarm clock");
					assertThat(item.getDescription()).isEqualTo("nothing important");
					assertThat(item.getPrice()).isEqualTo(19.99);
					return true;
				}) //
				.verifyComplete();
	}

	@Test
	void verifyRemoteOperationsThroughRSocketRequestResponse() throws InterruptedException {
		this.repository.deleteAll().as(StepVerifier::create).verifyComplete();

		this.webTestClient.post().uri("/items/request-response") // <1>
				.bodyValue(new Item("Alf alarm clock", "nothing important", 19.99)) 
				.exchange() 
				.expectStatus().isCreated() 
				.expectBody(Item.class) 
				.value(item -> {
					assertThat(item.getId()).isNotNull();
					assertThat(item.getName()).isEqualTo("Alf alarm clock");
					assertThat(item.getDescription()).isEqualTo("nothing important");
					assertThat(item.getPrice()).isEqualTo(19.99);
				});

		Thread.sleep(500); // <4>

		this.repository.findAll() 
				.as(StepVerifier::create) 
				.expectNextMatches(item -> {
					assertThat(item.getId()).isNotNull();
					assertThat(item.getName()).isEqualTo("Alf alarm clock");
					assertThat(item.getDescription()).isEqualTo("nothing important");
					assertThat(item.getPrice()).isEqualTo(19.99);
					return true;
				}) 
				.verifyComplete();
	}

}
