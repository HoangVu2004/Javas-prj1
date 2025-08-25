package AI_PRJ.WEBAPP.repository;

import AI_PRJ.WEBAPP.model.Ship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShipRepo extends JpaRepository<Ship, Integer> {
}
