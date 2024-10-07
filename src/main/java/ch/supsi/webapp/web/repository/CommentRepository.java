package ch.supsi.webapp.web.repository;

import ch.supsi.webapp.web.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
