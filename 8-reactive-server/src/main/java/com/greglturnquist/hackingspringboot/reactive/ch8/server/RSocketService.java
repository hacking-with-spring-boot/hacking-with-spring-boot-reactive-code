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

package com.greglturnquist.hackingspringboot.reactive.ch8.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

/**
 * @author Greg Turnquist
 */
// tag::code[]
@Controller
public class RSocketService {

	private static final Logger log = LoggerFactory.getLogger(RSocketService.class);

	private final ItemRepository repository;

	public RSocketService(ItemRepository repository) {
		this.repository = repository;
	}

	@MessageMapping("newItems")
	public Mono<Item> processNewItemsViaRSocket(Item item) {
		log.debug("Consuming => " + item);
		return this.repository.save(item);
	}
}
