/*
 * Copyright 2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.greglturnquist.hackingspringboot;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import org.springframework.stereotype.Service;

/**
 * @author Greg Turnquist
 */
@Service
class ItemRepository {

	private static AtomicLong ITEM_SEQ = new AtomicLong(0);
	private static List<Item> ITEMS = new ArrayList<>();

	void blockingSave(Item item) {
		item.setId(ITEM_SEQ.getAndIncrement());
		ITEMS.add(item);
	}

	Mono<Void> save(Item item) {
		return Mono.just(ITEM_SEQ.getAndIncrement())
			.map(id -> {
				item.setId(id);
				return item;
			})
			.map(ITEMS::add)
			.then();
	}

	Flux<Item> findAll() {
		return Flux.fromIterable(ITEMS);
	}

	Mono<Void> deleteById(long id) {
		return findAll()
			.filter(item -> item.getId() == id)
			.map(item -> ITEMS.remove(item))
			.then();
	}
}
