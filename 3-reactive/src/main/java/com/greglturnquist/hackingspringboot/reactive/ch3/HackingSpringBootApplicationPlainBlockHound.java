package com.greglturnquist.hackingspringboot.reactive.ch3;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.BlockHound;

@SpringBootApplication
public class HackingSpringBootApplicationPlainBlockHound {

	// tag::blockhound[]
	public static void main(String[] args) {
		BlockHound.install(); // <1>

		SpringApplication.run(HackingSpringBootApplicationPlainBlockHound.class, args);
	}
	// end::blockhound[]
}
