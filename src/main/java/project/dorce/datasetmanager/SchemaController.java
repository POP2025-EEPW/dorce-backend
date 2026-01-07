package project.dorce.datasetmanager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.dorce.datasetmanager.dto.SchemaDto;

import java.util.UUID;

@RestController
@RequestMapping("/api/schemas")
public class SchemaController {
    @Autowired
    SchemaService schemaService;

    @PostMapping
    public ResponseEntity<?> addSchema(@RequestBody SchemaDto schema) {
        try{
            var createdSchema = schemaService.createSchema(schema);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdSchema);
        }catch(Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public ResponseEntity<?> getSchemas() {
        return ResponseEntity.ok(schemaService.getAllSchemas());
    }
}
