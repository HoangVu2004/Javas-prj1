package AI_PRJ.WEBAPP.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import AI_PRJ.WEBAPP.model.Help;
import AI_PRJ.WEBAPP.model.Help.HelpId;

@Repository
public interface HelpRepo extends JpaRepository<Help, HelpId> {
    Help findByUserAndLab(String user, String lab);
}
