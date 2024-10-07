package ch.supsi.webapp.web.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id"})
@Entity
public class Role {
    public static final Long ADMIN_ID = 1L;
    public static final Long USER_ID = 2L;

    public enum Value {
        ROLE_ADMIN, ROLE_USER;
    }

    @Id
    private Long id;

    @Enumerated(EnumType.ORDINAL)
    private Value value;

    public Role(Long id) {
        this.id = id;
    }
}
