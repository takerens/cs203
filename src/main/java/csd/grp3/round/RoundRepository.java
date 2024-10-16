package csd.grp3.round;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoundRepository extends JpaRepository<Round, Long>{
    Optional<Round> findById(Long id);
}   
