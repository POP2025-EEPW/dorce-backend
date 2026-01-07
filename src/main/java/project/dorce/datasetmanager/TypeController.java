package project.dorce.datasetmanager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/types")
public class TypeController {
    @Autowired
    TypeService typeService;

    @PostMapping("/primitives")
    public ResponseEntity<?> addPrimitive(@RequestBody PrimitiveTypeDto primitiveTypeDto) {
        PrimitiveType primitiveType = typeService.createPrimitiveType(primitiveTypeDto);
        return ResponseEntity.ok(primitiveType);
    }

    @GetMapping
    public ResponseEntity<List<Type>> getAllTypes() {
        return ResponseEntity.ok(typeService.getAllTypes());
    }
}
