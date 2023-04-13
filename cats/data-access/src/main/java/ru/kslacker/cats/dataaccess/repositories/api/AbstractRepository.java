package ru.kslacker.cats.dataaccess.repositories.api;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import ru.kslacker.cats.dataaccess.exceptions.EntityException;

public abstract class AbstractRepository<T, ID> {
	private final Class<T> clazz;

	@PersistenceContext
	private EntityManager entityManager;

	public AbstractRepository(Class<T> clazz) {
		this.clazz = clazz;
	}

	public T getEntityById(ID id) {
		Optional<T> entity = Optional.ofNullable(entityManager.find(clazz, id));
		return entity.orElseThrow(() -> EntityException.entityNotFound(clazz, id));
	}

	public List<T> getBy(Map<String, Object> paramSet) {
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
