package com.team2.microservicemanagementtool.models;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document
public class StandardMicroService implements MicroService{

    @Id
    private String id;

    private String name;
    private String category;
    private Version currentVersion;

    private List<Version> versions = new LinkedList<>();


    public StandardMicroService(String name, String category)
    {
        setId(name.trim().toLowerCase().replace(" ","_"));
        setName(name);
        setCategory(category);
    }

    @Override
    public List<Version> getVersions() {
        return versions;
    }

    @Override
    public void setVersions(List<Version> versions) {
        this.versions = versions;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getCategory() {
        return this.category;
    }

    @Override
    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public void addVersion(Version toAdd) {
        this.versions.add(toAdd);
        setCurrentVersion(toAdd);
    }
    
    @Override
    public Version getVersion(BasicSemanticVersioning versionNum) {
        List<Version> toReturn = versions.stream().filter( v -> v.getVersionNumber().equals(versionNum)).collect(Collectors.toList());
        return toReturn.get(0);
    }

    public List<String> getVersionNames()
    {
        return versions.stream().map(v -> v.getVersionNumber().toString()).collect(Collectors.toList());
    }

    @Override
    public void setCurrentVersion(Version newVersion) {
        this.currentVersion = newVersion;
    }

    @Override
    public Version getCurrentVersion() {
        return this.currentVersion;
    }

    @Override
    public String toString() {
        return "{"+name+"-["+currentVersion.getVersionNumber().toString()+"]}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StandardMicroService that = (StandardMicroService) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(category, that.category) &&
                Objects.equals(currentVersion, that.currentVersion) &&
                Objects.equals(versions, that.versions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, category, currentVersion, versions);
    }
}
