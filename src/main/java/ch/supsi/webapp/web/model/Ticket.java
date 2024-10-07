package ch.supsi.webapp.web.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = {"id"})
@Entity
//handle circular references during JSON serialization.
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Ticket {
    @Id
    @GeneratedValue
    private Long id;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToOne
    private User author;

    private LocalDateTime creationDate;

    @ManyToOne
    private Status status;

    @ManyToOne
    private Type type;

    @OneToOne(cascade = CascadeType.ALL)
    private Attachment attachment;

    private LocalDate dueDate;

    @ManyToOne
    private User assignee;

    private Double resolutionEstimatedHours;

    private Double resolutionTotalHours;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Comment> comments;
}
