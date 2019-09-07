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

package com.greglturnquist.hackingspringboot.reactive.ch8.client;

import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Greg Turnquist
 */
@RestController
public class RSocketController {

	private static final Logger log = LoggerFactory.getLogger(RSocketController.class);

	private final RSocketRequester requester;

	public RSocketController(RSocketRequester requester) {
		this.requester = requester;
	}

	@PostMapping("/items")
	Mono<ResponseEntity<?>> addNewItemUsingRSocket(@RequestBody Mono<Item> item) {
		return item //
				.flatMap(content -> this.requester //
						.route("newItems") //
						.data(content) //
						.send()) //
				.map(aVoid -> ResponseEntity.created(URI.create("/items")).build());
	}
}
