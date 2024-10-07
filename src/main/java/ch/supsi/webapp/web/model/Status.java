package ch.supsi.webapp.web.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id"})
@Entity
public class Status {

    public static final Long OPEN_ID = 1L;
    public static final Long IN_PROGRESS_ID = 2L;
    public static final Long DONE_ID = 3L;
    public static final Long CLOSED_ID = 4L;

    public enum Value {
        OPEN, IN_PROGRESS, DONE, CLOSED;
    }

    @Id
    private Long id;

    @Enumerated(EnumType.ORDINAL)
    private Value value;
}
