package ru.kslacker.banks.tools.extensions;

public class StreamExtensionException extends RuntimeException {

	private StreamExtensionException(String message) {
		super(message);
	}

	/**
	 * Exception thrown when stream has multiple elements matching the predicate in .single()
	 *
	 * @return exception with corresponding message
	 */
	public static StreamExtensionException MultipleObjectsInSingleMethod() {
		return new StreamExtensionException("Found multiple objects suitable to the condition");
	}

	/**
	 * Exception thrown when no elements match the predicate in .single()
	 *
	 * @return exception with corresponding message
	 */
	public static StreamExtensionException SuitableObjectNotFoundInSingle() {
		return new StreamExtensionException("No objects are suitable to the condition");
	}
}
