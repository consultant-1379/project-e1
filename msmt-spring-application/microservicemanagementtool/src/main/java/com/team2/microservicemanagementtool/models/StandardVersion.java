package com.team2.microservicemanagementtool.models;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class StandardVersion implements Version{

    private String parentId;
    private BasicSemanticVersioning versionNumber;
    private String description;
    private LeadEngineer leadEngineer;
    private LocalDate dateCreated;
    private List<Version> dependencies;

    public StandardVersion(String parentId, BasicSemanticVersioning versionNumber, String description, LeadEngineer leadEngineer, LocalDate dateCreated)
    {
        setParentId(parentId.trim().toLowerCase().replace(" ","_"));
        setVersionNumber(versionNumber);
        setDescription(description);
        setLeadEngineer(leadEngineer);
        setDateCreated(dateCreated);
        dependencies = new ArrayList<>();
    }

    @Override
    public String getParentId() {
        return parentId;
    }

    @Override
    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    @Override
    public void setDependencies(List<Version> dependencies) {
        this.dependencies = dependencies;
    }

    @Override
    public LeadEngineer getLeadEngineer() {
        return this.leadEngineer;
    }

    @Override
    public void setLeadEngineer(LeadEngineer leadEngineer) {
        this.leadEngineer = leadEngineer;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public LocalDate getDateCreated() {
        return this.dateCreated;
    }

    @Override
    public void setDateCreated(LocalDate dateCreated) {
        this.dateCreated = dateCreated;
    }

    @Override
    public BasicSemanticVersioning getVersionNumber() {
        return this.versionNumber;
    }

    @Override
    public void setVersionNumber(BasicSemanticVersioning versionNumber) { this.versionNumber = versionNumber; }

    @Override
    public void addDependency(Version toAdd) { this.dependencies.add(toAdd); }

    @Override
    public List<Version> getDependencies() { return this.dependencies; }

    @Override
    public String toString() {
        return "VER:{"+parentId+"}-["+versionNumber.toString()+"]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StandardVersion version = (StandardVersion) o;
        return Objects.equals(parentId, version.parentId) &&
                Objects.equals(versionNumber, version.versionNumber) &&
                Objects.equals(description, version.description) &&
                Objects.equals(leadEngineer, version.leadEngineer) &&
                Objects.equals(dependencies, version.dependencies);
    }

    @Override
    public int hashCode() {
        return Objects.hash(parentId, versionNumber, description, leadEngineer, dependencies);
    }
}
