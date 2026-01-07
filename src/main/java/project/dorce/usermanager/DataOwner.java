package project.dorce.usermanager;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.dorce.datasetmanager.Dataset;

@Data
@Entity
@Table(name = "data_owners")
@NoArgsConstructor
public class DataOwner {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "owner_agent_id", nullable = false)
    @JsonIgnore
    private Agent ownerAgent;

    @OneToMany(mappedBy = "dataOwner")
    private List<Dataset> datasets;

    public DataOwner(String name, Agent ownerAgent) {
        this.name = name;
        this.ownerAgent = ownerAgent;
    }
}