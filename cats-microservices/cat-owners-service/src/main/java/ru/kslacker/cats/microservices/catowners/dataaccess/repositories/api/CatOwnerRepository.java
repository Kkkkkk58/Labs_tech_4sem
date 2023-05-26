package ru.kslacker.cats.microservices.catowners.dataaccess.repositories.api;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import ru.kslacker.cats.microservices.jpaentities.entities.CatOwner;

@Repository
public interface CatOwnerRepository extends JpaRepository<CatOwner, Long>,
	JpaSpecificationExecutor<CatOwner> {

}
