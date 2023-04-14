package ru.kslacker.cats.dataaccess.repositories.api;

import java.util.List;
import java.util.Map;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import ru.kslacker.cats.dataaccess.entities.CatOwner;

@Repository
public interface CatOwnerRepository extends JpaRepository<CatOwner, Long>, JpaSpecificationExecutor<CatOwner> {

	CatOwner getEntityById(Long id);
}
