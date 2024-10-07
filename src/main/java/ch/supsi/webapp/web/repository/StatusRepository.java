package ch.supsi.webapp.web.repository;

import ch.supsi.webapp.web.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;


public interface StatusRepository extends JpaRepository<Status, Long> {

}
