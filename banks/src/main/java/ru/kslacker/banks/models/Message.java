package ru.kslacker.banks.models;

import java.time.LocalDateTime;

public record Message(String sender, String topic, String content, LocalDateTime date) {

	public Message {
		if (sender.isEmpty()) {
			throw new IllegalArgumentException();
		}
		if (topic.isEmpty()) {
			throw new IllegalArgumentException();
		}
		if (content.isEmpty()) {
			throw new IllegalArgumentException();
		}
	}
}
