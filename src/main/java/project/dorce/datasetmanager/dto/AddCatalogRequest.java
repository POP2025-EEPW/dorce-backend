package project.dorce.datasetmanager.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NonNull;
import project.dorce.datasetmanager.Catalog;
import project.dorce.usermanager.Role;

import java.util.List;
import java.util.UUID;

@Data
public class AddCatalogRequest {
    @Data
    public static class CatalogDto {
        @NotNull
        @NotBlank
        private String description;

        @NotNull
        @NotBlank
        private String title;

        @NotNull
        private List<UUID> datasets;
    }

    private UUID parentCatalogId;

    @NotNull
    private CatalogDto catalog;
}
