package wildan.learn.springsecurity.repository;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import wildan.learn.springsecurity.domain.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends  JpaRepository<User, Long>,JpaSpecificationExecutor<User> {
    List<User> findUserByLogin(String login);
}
