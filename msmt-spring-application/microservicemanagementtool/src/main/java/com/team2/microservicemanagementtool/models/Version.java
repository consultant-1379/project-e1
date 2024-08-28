package com.team2.microservicemanagementtool.models;

import java.time.LocalDate;
import java.util.List;

public interface Version {

    String getParentId();

    void setParentId(String parentId);

    void setDependencies(List<Version> dependencies);

    LeadEngineer getLeadEngineer();
    void setLeadEngineer(LeadEngineer leadEngineer);

    String getDescription();
    void setDescription(String description);

    LocalDate getDateCreated();
    void setDateCreated(LocalDate dateCreated);

    BasicSemanticVersioning getVersionNumber();
    void setVersionNumber(BasicSemanticVersioning versionNumber);

    void addDependency(Version toadd);

    List<Version> getDependencies();
}
