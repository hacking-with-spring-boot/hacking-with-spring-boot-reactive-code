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
package com.greglturnquist.hackingspringboot.reactive.ch1;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @author Greg Turnquist
 */
@Controller
class HomeController {
	
	private EmployeeRepository repository;

	HomeController(EmployeeRepository repository) {
		this.repository = repository;
	}

	@GetMapping
	String home(Model model) {
		model.addAttribute("employees", this.repository.findAll());
		return "home.html";
	}

	@PostMapping
	String createEmployee(@ModelAttribute Employee newEmployee) {
		this.repository.save(newEmployee);
		return "redirect:/";
	}

	@GetMapping("/delete/{id}")
	String deleteEmployee(@PathVariable long id) {
		this.repository.deleteById(id);
		return "redirect:/";
	}
}
