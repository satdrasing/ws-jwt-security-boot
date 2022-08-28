package com.satendra.wssecurity.ws;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import com.satendra.wssecurity.security.JwtUtils;

@Configuration
@EnableWebSocketMessageBroker
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
public class WebSocketAuthenticationConfig implements WebSocketMessageBrokerConfigurer {

	private static final Logger logger = LoggerFactory.getLogger(WebSocketAuthenticationConfig.class);

	// @Autowired
	// private JwtDecoder jwtDecoder;

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private JwtUtils jwtUtils;

	@Override
	public void configureClientInboundChannel(ChannelRegistration registration) {
		registration.interceptors(new ChannelInterceptor() {
			@Override
			public Message<?> preSend(Message<?> message, MessageChannel channel) {
				StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
				if (StompCommand.CONNECT.equals(accessor.getCommand())) {
					List<String> authorization = accessor.getNativeHeader("Authorization");
					logger.debug("X-Authorization: {}", authorization);

					String accessToken = authorization.get(0).split(" ")[1];
					// Jwt jwt = jwtDecoder.decode(accessToken);
					// JwtAuthenticationConverter converter = new JwtAuthenticationConverter();

					String username = jwtUtils.getUserNameFromJwtToken(accessToken);

					UserDetails userDetails = userDetailsService.loadUserByUsername(username);

					UsernamePasswordAuthenticationToken converter = new UsernamePasswordAuthenticationToken(userDetails,
							null, userDetails.getAuthorities());

					// Authentication authentication = converter.
					accessor.setUser(converter);
				}
				return message;
			}
		});
	}

}
