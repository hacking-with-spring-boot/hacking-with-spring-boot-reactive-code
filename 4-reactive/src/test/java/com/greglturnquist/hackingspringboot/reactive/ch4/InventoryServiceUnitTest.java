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

package com.greglturnquist.hackingspringboot.reactive.ch4;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

/**
 * @author Greg Turnquist
 */
@ExtendWith(SpringExtension.class)
public class InventoryServiceUnitTest {

	InventoryService inventoryService;
	@MockBean private ItemRepository itemRepository;
	@MockBean private CartRepository cartRepository;

	@BeforeEach
	void setUp() {
	    when(cartRepository.findById(anyString())).thenReturn(Mono.empty());
	    when(itemRepository.findById(anyString())).thenReturn(Mono.just(new Item("TV tray", "Alf TV tray", 19.99)));
		inventoryService = new InventoryService(itemRepository, cartRepository);
	}

	@Test
	void testit() {
        inventoryService.addItemToCart("My Cart", "item1")
            .as(StepVerifier::create)
            .verifyComplete();
	}

}
