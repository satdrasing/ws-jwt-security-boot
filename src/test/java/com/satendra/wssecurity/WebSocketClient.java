package com.satendra.wssecurity;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;
import org.springframework.web.socket.sockjs.frame.Jackson2SockJsMessageCodec;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WebSocketClient {

	private final static WebSocketHttpHeaders headers = new WebSocketHttpHeaders();

	private final static StompHeaders stompHeaders= new StompHeaders();
	
	public ListenableFuture<StompSession> connect() {

		Transport webSocketTransport = new WebSocketTransport(new StandardWebSocketClient());
		List<Transport> transports = Collections.singletonList(webSocketTransport);

		SockJsClient sockJsClient = new SockJsClient(transports);
		sockJsClient.setMessageCodec(new Jackson2SockJsMessageCodec());

		WebSocketStompClient stompClient = new WebSocketStompClient(sockJsClient);

		String accessToken = "Bearer " + "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c2VyIiwiaWF0IjoxNjYxNjc1Nzg0LCJleHAiOjE2NjE2NzU5MDR9.1Nd0o0bmmKurJXRa2Z6TcR4pCq2tYiLTqqDxdKt40AzRNr1E2EC7uyIacCGQTw6SaaRpbgxkOkUXvrZirI4U1g";
		stompHeaders.add("Authorization", accessToken);
		
		String url = "ws://{host}:{port}/websocket";
		return stompClient.connect(url, headers, stompHeaders, new MyHandler(), "localhost", 8080);
	}

	public void subscribeGreetings(StompSession stompSession) throws ExecutionException, InterruptedException {
		stompSession.subscribe("/topic/news", new StompFrameHandler() {

			public Type getPayloadType(StompHeaders stompHeaders) {
				return byte[].class;
			}

			public void handleFrame(StompHeaders stompHeaders, Object o) {
				log.info("Received greeting " + new String((byte[]) o));
			}
		});
	}

	private class MyHandler extends StompSessionHandlerAdapter {
		public void afterConnected(StompSession stompSession, StompHeaders stompHeaders) {
			log.info("Now connected");
		}
	}

	public static void main(String[] args) throws Exception {
		WebSocketClient helloClient = new WebSocketClient();

		ListenableFuture<StompSession> f = helloClient.connect();
		StompSession stompSession = f.get();

		log.info("Subscribing  topic using session " + stompSession);
		helloClient.subscribeGreetings(stompSession);
		
		Thread.sleep(240000);
	}
	

}
