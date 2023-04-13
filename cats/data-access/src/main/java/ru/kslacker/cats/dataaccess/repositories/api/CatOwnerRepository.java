package ru.kslacker.cats.dataaccess.repositories.api;

import java.util.List;
import java.util.Map;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.kslacker.cats.dataaccess.entities.CatOwner;

@Repository
public interface CatOwnerRepository extends JpaRepository<CatOwner, Long> {
	List<CatOwner> getBy(Map<String, Object> paramSet);

	CatOwner getEntityById(Long id);
//
//	default CatOwner getEntityById(Long id) {
//		return findById(id).orElseThrow(() -> CatOwnerException.catOwnerNotFound(id));
//	}
}
