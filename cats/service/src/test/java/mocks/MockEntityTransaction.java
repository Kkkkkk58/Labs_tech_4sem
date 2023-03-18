package mocks;

import jakarta.persistence.EntityTransaction;

public class MockEntityTransaction implements EntityTransaction {

	@Override
	public void begin() {

	}

	@Override
	public void commit() {

	}

	@Override
	public void rollback() {

	}

	@Override
	public void setRollbackOnly() {

	}

	@Override
	public boolean getRollbackOnly() {
		return false;
	}

	@Override
	public boolean isActive() {
		return false;
	}
}
