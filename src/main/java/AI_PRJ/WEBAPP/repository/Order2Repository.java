package AI_PRJ.WEBAPP.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import AI_PRJ.WEBAPP.model.Order2;

@Repository
public interface Order2Repository extends JpaRepository<Order2, Long> {
    List<Order2> findByUser_Id(Long userId);
}