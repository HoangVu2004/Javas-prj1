package AI_PRJ.WEBAPP.repository;

import AI_PRJ.WEBAPP.model.Help;
import AI_PRJ.WEBAPP.model.Help.HelpId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HelpRepo extends JpaRepository<Help, HelpId> {
    Help findByUserAndLab(String user, String lab);
    
    // New method to find all responses for a specific ticket
    java.util.List<Help> findAllByUserAndLab(String user, String lab);
}
