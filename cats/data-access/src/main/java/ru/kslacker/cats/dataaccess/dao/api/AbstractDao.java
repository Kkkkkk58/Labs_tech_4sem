package ru.kslacker.cats.dataaccess.dao.api;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public abstract class AbstractDao<T, PK extends Serializable> implements Dao<T, PK> {

	private final Class<T> clazz;

	@PersistenceContext
	private final EntityManager entityManager;

	public AbstractDao(Class<T> clazz, EntityManager entityManager) {
		this.clazz = clazz;
		this.entityManager = entityManager;
	}

	@Override
	public T save(T t) {
		entityManager.persist(t);
		entityManager.flush();
		return t;
	}

	@Override
	public T getById(PK id) {
		return entityManager.find(clazz, id);
	}

	@Override
	public List<T> getAll() {
		return entityManager.createQuery("from " + clazz.getSimpleName(), clazz).getResultList();
	}

	@Override
	public void update(T t) {
		entityManager.merge(t);
	}

	@Override
	public void delete(T t) {
		entityManager.remove(t);
	}

	@Override
	public List<T> getByParam(String paramName, Object value) {
		return entityManager
			.createQuery(
				"SELECT c FROM " + clazz.getSimpleName() + " c WHERE c." + paramName + " = :param",
				clazz)
			.setParameter("param", value)
			.getResultList();
	}

	@Override
	public List<T> getByParamSet(Map<String, Object> paramSet) {
		Query q = entityManager.createQuery(getQlString(paramSet), clazz);
		for (Map.Entry<String, Object> p : paramSet.entrySet()) {
			q.setParameter(p.getKey(), p.getValue());
		}
		return q.getResultList();
	}

	private String getQlString(Map<String, Object> paramSet) {

		List<String> queryParts = paramSet.keySet().stream().map(key -> "c." + key + " = :" + key)
			.toList();
		return "SELECT c FROM " + clazz.getSimpleName() + " c WHERE " + String.join(" AND ",
			queryParts);
	}
}
