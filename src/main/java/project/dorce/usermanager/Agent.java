package project.dorce.usermanager;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.CascadeType;
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

@Data
@Entity
@Table(name = "agents")
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Agent {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "type_id", nullable = false)
    private AgentType type;

    @OneToMany(mappedBy = "ownerAgent", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<DataOwner> dataOwners;

    @OneToMany(mappedBy = "supplierAgent", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<DataSupplier> dataSuppliers;

    @OneToMany(mappedBy = "agent")
    @JsonIgnore
    private List<User> users;

    public Agent(String name, String email, AgentType type) {
        this.name = name;
        this.email = email;
        this.type = type;
    }
}