package project.dorce.dataqualitymanager;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Dataset dataset;

    public Comment() {}

    public Comment(String content, Dataset dataset) {
        this.content = content;
        this.dataset = dataset;
    }
}
