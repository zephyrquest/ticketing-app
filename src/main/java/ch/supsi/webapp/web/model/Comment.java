package ch.supsi.webapp.web.model;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = {"id"})
@Entity
public class Comment {
    @Id
    @GeneratedValue
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String text;

    private LocalDateTime creationDate;

    @ManyToOne
    private User user;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Comment> replies;
}
