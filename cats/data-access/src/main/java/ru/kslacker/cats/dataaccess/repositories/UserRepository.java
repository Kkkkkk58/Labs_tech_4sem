package ru.kslacker.cats.dataaccess.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import ru.kslacker.cats.dataaccess.entities.UserAccount;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserAccount, Long>, JpaSpecificationExecutor<UserAccount> {

	Optional<UserAccount> findByUsername(String username);
}
