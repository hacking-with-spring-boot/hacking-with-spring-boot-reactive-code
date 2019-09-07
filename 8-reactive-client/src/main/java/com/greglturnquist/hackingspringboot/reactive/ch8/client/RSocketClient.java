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
package com.greglturnquist.hackingspringboot.reactive.ch8.client;

import io.rsocket.RSocket;
import io.rsocket.RSocketFactory;
import io.rsocket.frame.decoder.PayloadDecoder;
import io.rsocket.metadata.WellKnownMimeType;
import io.rsocket.transport.netty.client.TcpClientTransport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.messaging.rsocket.RSocketStrategies;

/**
 * @author Greg Turnquist
 */
@Configuration
public class RSocketClient {

	@Bean
	public RSocket rSocket() {
		return RSocketFactory //
				.connect() //
				.mimeType(WellKnownMimeType.MESSAGE_RSOCKET_ROUTING.toString(), MediaType.APPLICATION_JSON_VALUE) //
				.frameDecoder(PayloadDecoder.ZERO_COPY) //
				.transport(TcpClientTransport.create(7000)) //
				.start() //
				.block();
	}

	@Bean
	public RSocketRequester rSocketRequester(RSocketStrategies rSocketStrategies) {
		return RSocketRequester.wrap(rSocket(), MediaType.APPLICATION_JSON, MediaType.parseMediaType(WellKnownMimeType.MESSAGE_RSOCKET_ROUTING.toString()), rSocketStrategies);

	}
}
