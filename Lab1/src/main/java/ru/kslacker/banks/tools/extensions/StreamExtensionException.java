package ru.kslacker.banks.tools.extensions;

public class StreamExtensionException extends RuntimeException {

	private StreamExtensionException(String message) {
		super(message);
	}

	public static StreamExtensionException MultipleObjectsInSingleMethod() {
		return new StreamExtensionException("Found multiple objects suitable to the condition");
	}

	public static StreamExtensionException SuitableObjectNotFoundInSingle() {
		return new StreamExtensionException("No objects are suitable to the condition");
	}
}
