package ru.kslacker.banks.console.exceptions;

public class HandlerException extends BanksConsoleException {

	private HandlerException(String message) {
		super(message);
	}

	public static HandlerException invalidRequestType(String handledRequest, String actualRequest)
	{
		return new HandlerException("Handler " + handledRequest + " doesn't handle " + actualRequest + " requests");
	}

	public static HandlerException invalidRequestParametersLength(int argsLength)
	{
		return new HandlerException("Invalid number of request parameters: " + argsLength);
	}
}
