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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.reactive.result.view.Rendering;

/**
 * @author Greg Turnquist
 */
@Controller
public class HomeController {

	private static final Logger LOG = LoggerFactory.getLogger(HomeController.class);

	private final CartRepository cartRepository;
	private final ItemRepository itemRepository;

	HomeController(CartRepository cartRepository,
				   ItemRepository itemRepository) {
		this.cartRepository = cartRepository;
		this.itemRepository = itemRepository;
	}

	@GetMapping
	Mono<Rendering> home(Model model, @RequestParam(required = false) String cartId) {

		return Mono.justOrEmpty(cartId)
			.flatMap(id -> this.cartRepository.findById(cartId)
				.doOnError(throwable -> new CartNotFoundException(cartId)))
			.switchIfEmpty(this.itemRepository.findAll()
				.map(CartItem::new)
				.collectList()
				.map(Cart::new)
				.flatMap(this.cartRepository::save))
			.map(cart -> Rendering
				.view("home.html")
				.modelAttribute("cartId", cart.getId())
				.modelAttribute("cartItems", cart.getCartItems())
				.build());
	}

	@PostMapping("/remove/{cartId}/{itemId}")
	Mono<Rendering> removeFromCart(@PathVariable String cartId, @PathVariable String itemId) {

		return this.cartRepository.findById(cartId)
			.doOnError(throwable -> new CartNotFoundException(cartId))
			.map(cart -> {
				for (CartItem cartItem : cart.getCartItems()) {
					if (cartItem.getItem().getId().equals(itemId)) {
						if (cartItem.getQuantity() > 0) {
							cartItem.setQuantity(cartItem.getQuantity() - 1);
							LOG.info("Dropping one " + cartItem.getItem() + " from the cart.");
						}
					}
				}
				return cart;
			})
			.flatMap(this.cartRepository::save)
			.map(cart -> Rendering
				.redirectTo("/?cartId=" + cartId)
				.build());
	}

	@PostMapping("/add/{cartId}/{itemId}")
	Mono<Rendering> addToCart(@PathVariable String cartId, @PathVariable String itemId) {

		return this.cartRepository.findById(cartId)
			.doOnError(throwable -> new CartNotFoundException(cartId))
			.map(cart -> {
				for (CartItem cartItem : cart.getCartItems()) {
					if (cartItem.getItem().getId().equals(itemId)) {
						cartItem.setQuantity(cartItem.getQuantity() + 1);
						LOG.info("Dropping one " + cartItem.getItem() + " from the cart.");
					}
				}
				return cart;
			})
			.flatMap(this.cartRepository::save)
			.map(cart -> Rendering
				.redirectTo("/?cartId=" + cartId)
				.build());
	}

	@PostMapping("/order/{cartId}")
	Mono<Rendering> placeOrder(@PathVariable String cartId) {
		return this.cartRepository.findById(cartId)
			.doOnSuccess(cart -> LOG.info("Firing off some other service to fulfill " + cart))
			.doOnError(throwable -> new CartNotFoundException(cartId))
			.flatMap(this.cartRepository::delete)
			.then(Mono.just(Rendering
				.redirectTo("/")
				.build()));
	}

	@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "No such cart")
	class CartNotFoundException extends RuntimeException {

		public CartNotFoundException(String cartId) {
			super("Could not find cart " + cartId);
		}
	}
}
