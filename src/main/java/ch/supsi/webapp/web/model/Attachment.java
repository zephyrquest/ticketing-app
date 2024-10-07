package ch.supsi.webapp.web.model;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = {"id"})
@Entity
public class Attachment {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @Lob
    @Column(columnDefinition = "MEDIUMBLOB")
    private byte[] data;

    private String contentType;
}
