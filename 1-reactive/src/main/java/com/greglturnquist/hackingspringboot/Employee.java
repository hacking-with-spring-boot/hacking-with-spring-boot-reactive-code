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

import java.util.Objects;

/**
 * @author Greg Turnquist
 */
class Employee {

	private long id;
	private String name;
	private String role;

	Employee(String name, String role) {
		this.name = name;
		this.role = role;
	}

	private Employee() { }

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Employee employee = (Employee) o;
		return id == employee.id &&
			Objects.equals(name, employee.name) &&
			Objects.equals(role, employee.role);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, name, role);
	}

	@Override
	public String toString() {
		return "Employee{" +
			"id=" + id +
			", name='" + name + '\'' +
			", role='" + role + '\'' +
			'}';
	}
}
