package ru.kslacker.cats.dataaccess.repositories.api;

import java.util.List;
import java.util.Map;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.kslacker.cats.dataaccess.entities.Cat;

@Repository
public interface CatRepository extends JpaRepository<Cat, Long> {
	List<Cat> getBy(Map<String, Object> paramSet);

	Cat getEntityById(Long id);
}
