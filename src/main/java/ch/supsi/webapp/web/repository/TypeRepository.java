package ch.supsi.webapp.web.repository;

import ch.supsi.webapp.web.model.Type;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TypeRepository extends JpaRepository<Type, Long> {
}
