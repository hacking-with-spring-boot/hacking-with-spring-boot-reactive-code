package com.greglturnquist.hackingspringboot.reactive.ch4;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.util.JdkIdGenerator;
import org.springframework.web.filter.reactive.HiddenHttpMethodFilter;
import reactor.BlockHound;

@SpringBootApplication
public class HackingSpringBootApplicationBlockHoundCustomized {

	// tag::blockhound[]
	public static void main(String[] args) {
		BlockHound.builder() // <1>
				.allowBlockingCallsInside(JdkIdGenerator.class.getCanonicalName(), "generateId") // <2>
				.install(); // <3>

		SpringApplication.run(HackingSpringBootApplicationBlockHoundCustomized.class, args);
	}
	// end::blockhound[]

	@Bean
	HiddenHttpMethodFilter hiddenHttpMethodFilter() {
		return new HiddenHttpMethodFilter();
	}
}
