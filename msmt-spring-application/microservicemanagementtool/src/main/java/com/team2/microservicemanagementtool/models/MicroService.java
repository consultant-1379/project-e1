package com.team2.microservicemanagementtool.models;

import java.util.List;

public interface MicroService {

    List<Version> getVersions();

    void setVersions(List<Version> versions);

    String getId();
    void setId(String id);

    String getName();
    void setName(String name);

    String getCategory();
    void setCategory(String category);

    void addVersion(Version toAdd);

    Version getVersion(BasicSemanticVersioning versionNum);

    void setCurrentVersion(Version newVersion);
    Version getCurrentVersion();
}
