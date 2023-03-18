package ru.kslacker.cats.dataaccess.dao.api;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface Dao<T, PK extends Serializable> {

	T save(T t);

	T getById(PK id);

	List<T> getAll();

	void update(T t);

	void delete(T t);

	List<T> getByParam(String paramName, Object value);

	List<T> getByParamSet(Map<String, Object> paramSet);
}
