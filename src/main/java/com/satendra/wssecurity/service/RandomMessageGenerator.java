package com.satendra.wssecurity.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class RandomMessageGenerator {

	@Autowired
	private SimpMessagingTemplate simpMessagingTemplate;

	@Scheduled(fixedRate = 3000)
	public void sendMessageToTopic() {
		int randomNumber = generateRandomNumber(1, 500);
		TestMessage message = TestMessage.builder().id(randomNumber).name("satendra-" + randomNumber).build();
		log.info("message sent to exchange {} ", message.toString());
		simpMessagingTemplate.convertAndSend("/topic/news", message);
	}

	public static int generateRandomNumber(int min, int max) {
		return min + (int) (Math.random() * ((max - min) + 1));
	}
}
