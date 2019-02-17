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

import reactor.core.publisher.Mono;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.reactive.result.view.Rendering;

/**
 * @author Greg Turnquist
 */
@Controller
public class HomeController {

	private ItemRepository repository;

	public HomeController(ItemRepository repository) {
		this.repository = repository;
	}

	@GetMapping
	Mono<Rendering> home(Model model) {
		return Mono.just(Rendering
			.view("home.html")
			.modelAttribute("items", this.repository.findAll())
			.build());
	}

	@PostMapping
	Mono<String> createEmployee(@ModelAttribute Item newItem) {
		return this.repository.save(newItem)
			.then(Mono.just("redirect:/"));
	}

	@GetMapping("/delete/{id}")
	Mono<String> deleteEmployee(@PathVariable long id) {
		return this.repository.deleteById(id)
			.then(Mono.just("redirect:/"));
	}
}
