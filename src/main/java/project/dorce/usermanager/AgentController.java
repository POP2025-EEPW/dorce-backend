package project.dorce.usermanager;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import project.dorce.usermanager.dto.AddAgentRequest;
import project.dorce.usermanager.dto.AgentDto;

@RestController
@RequestMapping("/api/agents")
@Tag(name = "Agent Management", description = "Endpoints for managing Agents, Data Owners and Suppliers")
public class AgentController {

    private final AgentService agentService;

    public AgentController(AgentService agentService) {
        this.agentService = agentService;
    }

    @PostMapping
    @SecurityRequirement(name = "AuthToken")
    @Operation(summary = "Add a new Agent", description = "Creates a new Agent. Use flags 'createAsDataOwner' / 'createAsDataSupplier' to assign roles.")
    public ResponseEntity<AgentDto> addAgent(@Valid @RequestBody AddAgentRequest request) {
        AgentDto created = agentService.addAgent(request);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping
    @SecurityRequirement(name = "AuthToken")
    @Operation(summary = "Show data agent list", description = "Retrieves paginated list of agents including their boolean status (isDataOwner, isDataSupplier).")
    public ResponseEntity<Page<AgentDto>> listAgents(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int pageSize
    ) {
        return ResponseEntity.ok(agentService.listAgents(page, pageSize));
    }
}