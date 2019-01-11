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
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Greg Turnquist
 */
@Controller
class HomeController {

	private static final Logger LOG = LoggerFactory.getLogger(HomeController.class);

	private final CartRepository cartRepository;
	private final ItemRepository itemRepository;
	private final CartItemRepository cartItemRepository;

	HomeController(CartRepository cartRepository,
				   ItemRepository itemRepository, CartItemRepository cartItemRepository) {
		this.cartRepository = cartRepository;
		this.itemRepository = itemRepository;
		this.cartItemRepository = cartItemRepository;
	}

	@GetMapping
	String home(Model model, @RequestParam(required = false) Long cartId) {
		Cart cart;

		if (cartId != null) {
			cart = this.cartRepository.findById(cartId)
				.orElseThrow(() -> new CartNotFoundException(cartId));
		} else {
			cart = new Cart();
			for (Item item : this.itemRepository.findAll()) {
				CartItem cartItem = this.cartItemRepository.save(new CartItem(item));
				cart.getCartItems().add(cartItem);
			}
			cart = this.cartRepository.save(cart);
		}

		model.addAttribute("cartId", cart.getId());
		model.addAttribute("cartItems", cart.getCartItems());

		return "home.html";
	}

	@PostMapping("/remove/{cartId}/{itemId}")
	String removeFromCart(@PathVariable Long cartId, @PathVariable Long itemId) {

		Cart cart = this.cartRepository.findById(cartId)
			.orElseThrow(() -> new CartNotFoundException(cartId));

		for (CartItem cartItem : cart.getCartItems()) {
			if (cartItem.getItem().getId().equals(itemId)) {
				if (cartItem.getQuantity() > 0) {
					cartItem.setQuantity(cartItem.getQuantity() - 1);
					LOG.info("Dropping one " + cartItem.getItem() + " from the cart.");
				}
			}
		}

		this.cartRepository.save(cart);

		return "redirect:/?cartId=" + cartId;
	}

	@PostMapping("/add/{cartId}/{itemId}")
	String addToCart(@PathVariable Long cartId, @PathVariable Long itemId) {

		Cart cart = this.cartRepository.findById(cartId)
			.orElseThrow(() -> new CartNotFoundException(cartId));

		for (CartItem cartItem : cart.getCartItems()) {
			if (cartItem.getItem().getId().equals(itemId)) {
				cartItem.setQuantity(cartItem.getQuantity() + 1);
				LOG.info("Adding one " + cartItem.getItem() + " to the cart.");
			}
		}

		this.cartRepository.save(cart);

		return "redirect:/?cartId=" + cartId;
	}

	@PostMapping("/order/{cartId}")
	String placeOrder(@PathVariable Long cartId) {
		Cart cart = this.cartRepository.findById(cartId)
			.orElseThrow(() -> new CartNotFoundException(cartId));

		LOG.info("Firing off some other service to fulfill " + cart);

		this.cartRepository.delete(cart);

		return "redirect:/";
	}

	@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "No such cart")
	class CartNotFoundException extends RuntimeException {

		public CartNotFoundException(Long cartId) {
			super("Could not find cart " + cartId);
		}
	}
}
