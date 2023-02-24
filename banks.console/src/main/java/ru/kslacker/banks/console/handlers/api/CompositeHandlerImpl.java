package ru.kslacker.banks.console.handlers.api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.experimental.ExtensionMethod;
import ru.kslacker.banks.console.exceptions.HandlerException;
import ru.kslacker.banks.tools.extensions.StreamExtensions;

@ExtensionMethod(StreamExtensions.class)
public abstract class CompositeHandlerImpl implements CompositeHandler {

	private final List<Handler> subHandlers;
	private final String handledRequest;

	protected CompositeHandlerImpl(String handledRequest) {
		this.subHandlers = new ArrayList<>();
		this.handledRequest = handledRequest;
	}

	@Override
	public void handle(String... args) throws IOException {

		if (args.length < 2) {
			throw HandlerException.invalidRequestParametersLength(args.length);
		}
		if (!handledRequest.equalsIgnoreCase(args[0])) {
			throw HandlerException.invalidRequestType(handledRequest, args[0]);
		}

		subHandlers.stream()
			.single(handler -> handler.getHandledRequest().equalsIgnoreCase(args[1]))
			.handle(Arrays.stream(args).skip(1).toArray(String[]::new));
	}

	@Override
	public String getHandledRequest() {
		return handledRequest;
	}

	@Override
	public CompositeHandler addSubHandler(Handler handler) {
		subHandlers.add(handler);
		return this;
	}
}
