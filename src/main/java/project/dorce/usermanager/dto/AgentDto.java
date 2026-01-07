package project.dorce.usermanager.dto;

import java.util.UUID;

import lombok.Data;
import project.dorce.usermanager.Agent;

@Data
public class AgentDto {
    private UUID id;
    private String name;
    private String typeName;
    private String email;
    
    private boolean isDataOwner;
    private boolean isDataSupplier;

    public static AgentDto from(Agent agent) {
        AgentDto dto = new AgentDto();
        dto.setId(agent.getId());
        dto.setName(agent.getName());
        dto.setEmail(agent.getEmail());
        
        if (agent.getType() != null) {
            dto.setTypeName(agent.getType().getName());
        }

        dto.setDataOwner(agent.getDataOwners() != null && !agent.getDataOwners().isEmpty());
        dto.setDataSupplier(agent.getDataSuppliers() != null && !agent.getDataSuppliers().isEmpty());
        
        return dto;
    }
}