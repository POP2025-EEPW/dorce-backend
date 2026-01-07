package project.dorce.datasetmanager;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class TypeService {

    private final TypeRepository typeRepository;

    public TypeService(TypeRepository typeRepository) {
        this.typeRepository = typeRepository;
    }

    public PrimitiveType createPrimitiveType(PrimitiveTypeDto dto) {
        // Check if it already exists to prevent duplicates
        // (Assuming you might want unique names)

        PrimitiveType primitive = new PrimitiveType();
        primitive.setName(dto.getName());
        return typeRepository.save(primitive);
    }

    public List<Type> getAllTypes() {
        return typeRepository.findAll();
    }
}