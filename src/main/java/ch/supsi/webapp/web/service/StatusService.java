package ch.supsi.webapp.web.service;

import ch.supsi.webapp.web.model.Status;
import ch.supsi.webapp.web.repository.StatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StatusService {

    @Autowired
    private StatusRepository statusRepository;

    public List<Status> getAllStatuses() {
        return statusRepository.findAll();
    }
}
