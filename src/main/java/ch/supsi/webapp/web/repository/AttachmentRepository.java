package ch.supsi.webapp.web.repository;

import ch.supsi.webapp.web.model.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttachmentRepository extends JpaRepository<Attachment, Long> {

}
