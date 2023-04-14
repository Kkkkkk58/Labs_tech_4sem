package ru.kslacker.cats.dataaccess.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import ru.kslacker.cats.dataaccess.entities.CatOwner;

@Repository
public interface CatOwnerRepository extends JpaRepository<CatOwner, Long>, JpaSpecificationExecutor<CatOwner> {

}
