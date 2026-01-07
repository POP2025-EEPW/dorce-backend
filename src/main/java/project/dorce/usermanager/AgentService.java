package project.dorce.usermanager;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import project.dorce.usermanager.dto.AddAgentRequest;
import project.dorce.usermanager.dto.AgentDto;

@Service
@Transactional
public class AgentService {

    private final AgentRepository agentRepository;
    private final DataOwnerRepository dataOwnerRepository;
    private final DataSupplierRepository dataSupplierRepository;
    private final AgentTypeRepository agentTypeRepository;

    public AgentService(AgentRepository agentRepository,
                        DataOwnerRepository dataOwnerRepository,
                        DataSupplierRepository dataSupplierRepository,
                        AgentTypeRepository agentTypeRepository) {
        this.agentRepository = agentRepository;
        this.dataOwnerRepository = dataOwnerRepository;
        this.dataSupplierRepository = dataSupplierRepository;
        this.agentTypeRepository = agentTypeRepository;
    }

    /**
     * UC-SM-001: Add agent
     */
    public AgentDto addAgent(AddAgentRequest request) {
        AgentType type = agentTypeRepository.findByName(request.getTypeName())
                .orElseGet(() -> agentTypeRepository.save(new AgentType(request.getTypeName())));

        Agent agent = new Agent(request.getName(), request.getEmail(), type);
        agent = agentRepository.save(agent);

        if (request.isCreateAsDataOwner()) {
            DataOwner owner = new DataOwner(agent.getName(), agent);
            dataOwnerRepository.save(owner);
        }

        if (request.isCreateAsDataSupplier()) {
            DataSupplier supplier = new DataSupplier(agent.getName(), agent);
            dataSupplierRepository.save(supplier);
        }

        agentRepository.flush();
        return AgentDto.from(agentRepository.findById(agent.getId()).orElse(agent));
    }

    /**
     * UC-SM-006: Show data agent list
     */
    @Transactional(readOnly = true)
    public Page<AgentDto> listAgents(int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by("name").ascending());
        Page<Agent> agents = agentRepository.findAll(pageable);
        return agents.map(AgentDto::from);
    }
}