package ch.supsi.webapp.web.runner;

import ch.supsi.webapp.web.model.Status;
import ch.supsi.webapp.web.repository.StatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StatusInitRunner implements CommandLineRunner, Ordered {

    @Autowired private StatusRepository statusRepository;

    @Override
    public void run(String... args) throws Exception {
        if(statusRepository.count() == 0) {
            Status open = new Status(Status.OPEN_ID, Status.Value.OPEN);
            Status inProgress = new Status(Status.IN_PROGRESS_ID, Status.Value.IN_PROGRESS);
            Status done = new Status(Status.DONE_ID, Status.Value.DONE);
            Status closed = new Status(Status.CLOSED_ID, Status.Value.CLOSED);

            statusRepository.saveAll(List.of(open, inProgress, done, closed));
        }
    }

    @Override
    public int getOrder() {
        return 2;
    }
}
