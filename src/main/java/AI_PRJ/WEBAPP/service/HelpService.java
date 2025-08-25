package AI_PRJ.WEBAPP.service;

import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import AI_PRJ.WEBAPP.model.Help;
import AI_PRJ.WEBAPP.repository.HelpRepo;

@Service
public class HelpService {

    private static final int MAX_SUPPORT_COUNT = 5; // max allowed support times

    @Autowired
    private HelpRepo helpRepo;

    public boolean canSupport(String user, String lab) {
        Help help = helpRepo.findByUserAndLab(user, lab);
        if (help == null) {
            return true;
        }
        return help.getSupportCount() < MAX_SUPPORT_COUNT;
    }

    @Transactional
    public void recordSupport(String user, String lab) throws IllegalStateException {
        Help help = helpRepo.findByUserAndLab(user, lab);
        if (help == null) {
            help = new Help(user, lab, 1);
        } else {
            if (help.getSupportCount() >= MAX_SUPPORT_COUNT) {
                throw new IllegalStateException("Support limit exceeded");
            }
            help.setSupportCount(help.getSupportCount() + 1);
        }
        helpRepo.save(help);
    }

    public int getSupportCount(String user, String lab) {
        Help help = helpRepo.findByUserAndLab(user, lab);
        if (help == null) {
            return 0;
        }
        return help.getSupportCount();
    }

    public java.util.List<Help> getAllHelps() {
        return helpRepo.findAll();
    }

    @Transactional
    public void deleteAllHelps() {
        helpRepo.deleteAll();
    }
}
