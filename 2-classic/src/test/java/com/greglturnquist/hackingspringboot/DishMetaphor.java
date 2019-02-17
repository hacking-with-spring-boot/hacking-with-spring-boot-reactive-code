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

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Greg Turnquist
 */
public class DishMetaphor {

	static
	// tag::service[]
	class KitchenService {

		List<Dish> getDishes() {
			// We could model a ChefService, but let's just
			// hard code some tasty dishes.
			return Arrays.asList(
				new Dish("Sesame chicken"),
				new Dish("Lo mein noodles, plain"),
				new Dish("Sweet & sour beef"));
		}
	}
	// end::service[]

	// tag::simple-server[]
	class SimpleServer {

		private final KitchenService kitchen;

		SimpleServer(KitchenService kitchen) {
			this.kitchen = kitchen;
		}

		List<Dish> doingMyJob() {
			return this.kitchen.getDishes().stream()
				.map(dish -> this.deliver(dish))
				.collect(Collectors.toList());
		}

		Dish deliver(Dish dish) {
			dish.setDelivered(true);
			return dish;
		}
	}
	// end::simple-server[]

	static
	// tag::polite-server[]
	class PoliteServer {

		private final KitchenService kitchen;

		PoliteServer(KitchenService kitchen) {
			this.kitchen = kitchen;
		}

		List<Dish> doingMyJob() {
			return this.kitchen.getDishes().stream()
				.map(dish -> {
					System.out.println("Thank you for " + dish + "!");
					return dish;
				})
				.map(this::deliver)
				.collect(Collectors.toList());
		}

		Dish deliver(Dish dish) {
			dish.setDelivered(true);
			return dish;
		}
	}
	// end::polite-server[]

	class BusyServer {

		private final KitchenService kitchen;

		BusyServer(KitchenService kitchen) {
			this.kitchen = kitchen;
		}

		List<Dish> doingMyJob() {
			// tag::multiple-side-effects[]
			return this.kitchen.getDishes().stream()
				.map(dish -> {
					System.out.println("Thank you for " + dish + "!");
					System.out.println("Marking the ticket as done.");
					System.out.println("Grabbing some silverware.");
					return dish;
				})
				.map(this::deliver)
				.collect(Collectors.toList());
			// end::multiple-side-effects[]
		}

		Dish deliver(Dish dish) {
			dish.setDelivered(true);
			return dish;
		}
	}

	class BusyServer2 {

		private final KitchenService kitchen;

		BusyServer2(KitchenService kitchen) {
			this.kitchen = kitchen;
		}

		List<Dish> doingMyJob() {
			// tag::multiple-side-effects2[]
			return this.kitchen.getDishes().stream()
				.map(dish -> {
					System.out.println("Thank you for " + dish + "!");
					return dish;
				})
				.map(this::deliver)
				.collect(Collectors.toList());
			// end::multiple-side-effects2[]
		}

		Dish deliver(Dish dish) {
			dish.setDelivered(true);
			return dish;
		}
	}

	static
	// tag::dish[]
	class Dish {
		private String description;
		private boolean delivered = false;

		Dish(String description) {
			this.description = description;
		}

		public boolean isDelivered() {
			return delivered;
		}

		public void setDelivered(boolean delivered) {
			this.delivered = delivered;
		}

		@Override
		public String toString() {
			return "Dish{" +
				"description='" + description + '\'' +
				", delivered=" + delivered +
				'}';
		}
	}
	// end::dish[]

	static
	// tag::example[]
	class PoliteRestaurant {

		public static void main(String... args) {
			PoliteServer server = new PoliteServer(new KitchenService());

			server.doingMyJob().forEach(dish -> {
				System.out.println("Consuming " + dish);
			});
		}
	}
	// end::example[]

}
