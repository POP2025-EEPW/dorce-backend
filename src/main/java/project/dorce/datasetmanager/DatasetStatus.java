package project.dorce.datasetmanager;

public enum DatasetStatus {
    /**
     * w konfiguracji
     */
    DRAFT,
    /**
     * go przegladu przez quality managera
     */
    SUBMITTED,
    /**
     * aktywny
     */
    ACTIVE,
    /**
     * zarchiwizowany
     */
    DELETED
}