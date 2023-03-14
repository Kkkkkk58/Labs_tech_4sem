package ru.kslacker.cats.dataaccess.dao.api;

import java.io.Serializable;
import java.util.List;
import org.hibernate.SessionFactory;

public abstract class AbstractDao<T, PK extends Serializable> {

	private final Class<T> clazz;
	private final SessionFactory sessionFactory;

	public AbstractDao(Class<T> clazz, SessionFactory sessionFactory) {
		this.clazz = clazz;
		this.sessionFactory = sessionFactory;
	}

	public T create(T t) {
		sessionFactory.getCurrentSession().persist(t);
		sessionFactory.getCurrentSession().flush();
		return t;
	}

	public T getById(PK id) {
		return sessionFactory.getCurrentSession().get(clazz, id);
	}

	public List<T> getAll() {
		return sessionFactory.getCurrentSession().createQuery("from " + clazz.getSimpleName(), clazz).list();
	}

	public void update(T t) {
		sessionFactory.getCurrentSession().merge(t);
	}

	public void delete(T t) {
		sessionFactory.getCurrentSession().remove(t);
	}
}
