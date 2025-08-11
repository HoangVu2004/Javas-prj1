package AI_PRJ.WEBAPP.repository;

import AI_PRJ.WEBAPP.model.Lab;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LabRepo extends JpaRepository<Lab, Long> {
    // Tìm tất cả labs theo kit_id
    List<Lab> findByKitId(Long kitId);
}
