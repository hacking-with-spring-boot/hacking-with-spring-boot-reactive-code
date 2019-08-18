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
package com.greglturnquist.hackingspringboot.reactive.ch7;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.MediaTypes;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.document;

/**
 * @author Greg Turnquist
 */
// tag::intro[]
@WebFluxTest(controllers = ApiItemController.class) // <1>
@AutoConfigureRestDocs // <2>
public class ApiItemControllerDocumentationTest {

	@Autowired private WebTestClient webTestClient; // <3>

	@MockBean InventoryService service; // <4>

	@MockBean ItemRepository repository; // <5>
	// end::intro[]

	// tag::test1[]
	@Test
	void findingAllItems() {
		when(repository.findAll()) // <1>
				.thenReturn(Flux.just(new Item("item-1", "Alf alarm clock", "nothing I really need", 19.99)));

		this.webTestClient.get().uri("/api/items") //
				.exchange() //
				.expectStatus().isOk() //
				.expectBody() //
				.consumeWith(document("findAll", preprocessResponse(prettyPrint()))); // <2>
	}
	// end::test1[]

	// tag::test2[]
	@Test
	void postNewItem() {
		when(repository.save(any())) //
				.thenReturn(Mono.just(new Item("1", "Alf alarm clock", "nothing important", 19.99)));

		// TODO: Remove `content-type` pending https://github.com/spring-projects/spring-hateoas/issues/1047
		this.webTestClient.post().uri("/api/items") // <1>
				.contentType(MediaTypes.HAL_JSON) // <2>
				.body(new Item("Alf alarm clock", "nothing important", 19.99)) // <3>
				.exchange() //
				.expectStatus().isCreated() // <4>
				.expectBody() //
				.consumeWith(document("post-new-item", preprocessResponse(prettyPrint()))); //
	}
	// end::test2[]

	// tag::test3[]
	@Test
	void findOneItem() {
		when(repository.findById("item-1")) // <1>
				.thenReturn(Mono.just(new Item("item-1", "Alf alarm clock", "nothing I really need", 19.99)));

		this.webTestClient.get().uri("/api/items/item-1") //
				.exchange() //
				.expectStatus().isOk() //
				.expectBody() //
				.consumeWith(document("findOne", preprocessResponse(prettyPrint()))); // <2>
	}
	// end::test3[]

}
