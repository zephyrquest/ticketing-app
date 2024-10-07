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
public class Type {

    public static final Long TASK_ID = 1L;
    public static final Long STORY_ID = 2L;
    public static final Long ISSUE_ID = 3L;
    public static final Long BUG_ID = 4L;
    public static final Long INVESTIGATION_ID = 5L;

    public enum Value {
        TASK, STORY, ISSUE, BUG, INVESTIGATION;
    }

    @Id
    private Long id;

    @Enumerated(EnumType.ORDINAL)
    private Value value;
}
