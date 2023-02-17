package ru.kslacker.banks.tools.extensions;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;
import lombok.experimental.UtilityClass;

@UtilityClass
public class StreamExtensions {

	public static <T> T single(Stream<T> stream, Predicate<? super T> predicate) {
		return stream
			.filter(predicate)
			.reduce((a, b) -> { throw StreamExtensionException.MultipleObjectsInSingleMethod(); })
			.orElseThrow(StreamExtensionException::SuitableObjectNotFoundInSingle);
	}

	public static <T> Stream<T> distinctBy(Stream<T> stream, Function<? super T, ?> keyExtractor) {

		Map<Object, Boolean> seen = new ConcurrentHashMap<>();
		return stream.filter(t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null);
	}
}
