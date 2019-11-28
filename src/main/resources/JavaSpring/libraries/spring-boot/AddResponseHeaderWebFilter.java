package io.swagger.configuration;

import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import reactor.core.publisher.Mono;


/**
 * DEAL WITH CORB issues
 * https://www.chromium.org/Home/chromium-security/corb-for-developers
 * @author John McMahon ~ github: SpaceGhost69 | twitter: @TechnoCharms
 *
 */
@Component
public class AddResponseHeaderWebFilter implements WebFilter {

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
		exchange.getResponse().getHeaders()
				.add("X-Content-Type-Options", "nosniff");
		return chain.filter(exchange);
	}
}
