package ru.kslacker.cats.microservices.common.amqp;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.kslacker.cats.microservices.common.exceptions.CatsException;
import java.io.Serializable;

@Data
@NoArgsConstructor
public class TypeAwareExceptionWrapper<T extends Throwable> implements Serializable {

	private Class<? extends Throwable> clazz;
	private T exception;

	public TypeAwareExceptionWrapper(T e) {
		this.exception = e;
		if (CatsException.class.isAssignableFrom(e.getClass())) {
			this.clazz = CatsException.class;
		} else {
			this.clazz = e.getClass();
		}
	}
}
