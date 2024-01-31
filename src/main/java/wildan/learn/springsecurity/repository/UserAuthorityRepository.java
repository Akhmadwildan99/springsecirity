package wildan.learn.springsecurity.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import wildan.learn.springsecurity.domain.UserAuthoryty;

@Repository
public interface UserAuthorityRepository extends JpaRepository<UserAuthoryty, Long> {
}
