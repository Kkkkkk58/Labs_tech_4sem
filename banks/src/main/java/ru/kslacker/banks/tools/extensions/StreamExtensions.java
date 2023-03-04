package ru.kslacker.banks.tools.extensions;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;
import lombok.experimental.UtilityClass;

@UtilityClass
public class StreamExtensions {

	/**
	 * StreamAPI extension to get single value from stream corresponding given predicate
	 *
	 * @param stream    stream to filter
	 * @param predicate predicate to correspond
	 * @param <T>       stream type parameter
	 * @return single value
	 */
	public static <T> T single(Stream<T> stream, Predicate<? super T> predicate) {
		return stream
			.filter(predicate)
			.reduce((a, b) -> {
				throw StreamExtensionException.MultipleObjectsInSingleMethod();
			})
			.orElseThrow(StreamExtensionException::SuitableObjectNotFoundInSingle);
	}

	/**
	 * StreamAPI extension to get value from stream distinct by key
	 *
	 * @param stream       stream to filter
	 * @param keyExtractor function to extract key
	 * @param <T>          stream type parameter
	 * @return filtered stream
	 */
	public static <T> Stream<T> distinctBy(Stream<T> stream, Function<? super T, ?> keyExtractor) {

		Map<Object, Boolean> seen = new ConcurrentHashMap<>();
		return stream.filter(t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null);
	}
}
