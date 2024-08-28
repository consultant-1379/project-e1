package com.team2.microservicemanagementtool.models;


import java.util.List;
import java.time.LocalDate;

//only needs category and name as rest of info will be parsed into the version object
public class StandardMicroServiceDTO {
    private String name;
    private String category;
    private BasicSemanticVersioning versionNumber;
    private String description;
    private LeadEngineer leadEngineer;
    private List<Version> dependencies;

    public StandardMicroServiceDTO(String name, String category, BasicSemanticVersioning versionNumber, String description,LeadEngineer leadEngineer, List<Version> dependencies)
    {
        this.name=name;
        this.category=category;
        this.versionNumber=versionNumber;
        this.description=description;
        this.leadEngineer=leadEngineer;
        this.dependencies=dependencies;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setDependencies(List<Version> dependencies) {
        this.dependencies = dependencies;
    }
    public List<Version> getDependencies() { return this.dependencies; }

    
    public LeadEngineer getLeadEngineer() {
        return this.leadEngineer;
    }

    public void setLeadEngineer(LeadEngineer leadEngineer) {
        this.leadEngineer = leadEngineer;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BasicSemanticVersioning getVersionNumber() {
        return this.versionNumber;
    }

    public void setVersionNumber(BasicSemanticVersioning versionNumber) { this.versionNumber = versionNumber; }

    

}
