package guc.bttsBtngan.user.services;

import guc.bttsBtngan.user.data.UserUserInteraction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// this interface is responsible for data access layer in postgresql
@Repository
public interface UserRepository extends JpaRepository<UserUserInteraction, Long> {
}
