package com.team2.microservicemanagementtool.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TestStandardVersion {

    static LeadEngineer bob = new LeadEngineer("Robert Carville", "robert.carville@ericsson.com");
    static MicroService ms = new StandardMicroService("Rest Microservice", "APIs");
    static MicroService ms2 = new StandardMicroService("MongoDB Microservice", "Databases");
    static Version version = new StandardVersion(ms.getId(), new BasicSemanticVersioning(2,7,4), "Description", bob, LocalDate.of(2000, 5, 26));
    static Version version2 = new StandardVersion(ms2.getId(), new BasicSemanticVersioning(1,2,12), "Description", bob, LocalDate.of(2000, 5, 26));
    static List<Version> dependencyList = Arrays.asList(version, version2);

    @BeforeEach
    void reset()
    {
        version = new StandardVersion(ms.getId(), new BasicSemanticVersioning(2,7,4), "Description", bob, LocalDate.of(2000, 5, 26));

    }
    @Test
    void testGetVersionNumber(){
        assertEquals(new BasicSemanticVersioning(2,7,4), version.getVersionNumber());
    }

    @Test
    void testGetDescription(){
        assertEquals("Description", version.getDescription());
    }

    @Test
    void testGetLeadEngineer() {
        assertEquals(bob, version.getLeadEngineer());
        assertEquals("Robert Carville", bob.getName());
        assertEquals("robert.carville@ericsson.com", bob.getEmail());
    }

    @Test
    void testGetParentId() {
        assertEquals("rest_microservice", version.getParentId());
    }

    @Test
    void testGetDateCreated(){
        assertEquals(LocalDate.of(2000, 5, 26), version.getDateCreated());
    }

    @Test
    void testAddAndGetDependencies() {
        version.addDependency(version2);
        assertEquals(Arrays.asList(new Version[]{version2}), version.getDependencies());
    }

    @Test
    void testSetDependencies() {
        version.setDependencies(dependencyList);
        assertEquals(dependencyList, version.getDependencies());
    }

    @Test
    void testEquals() {
        Version v1 = new StandardVersion(ms.getId(), new BasicSemanticVersioning(2,7,4), "Description", bob, LocalDate.of(2000, 5, 26));


        assertEquals(v1,v1);
        assertEquals(version,v1);
        v1.setVersionNumber(new BasicSemanticVersioning(0,0,0));
        assertNotEquals(version,v1);
        v1 = new StandardVersion(ms.getId(), new BasicSemanticVersioning(2,7,4), "bad", bob, LocalDate.of(2000, 5, 26));
        assertNotEquals(version,v1);
        v1 = new StandardVersion(ms.getId(), new BasicSemanticVersioning(2,7,4), "Description", new LeadEngineer("bad", "robert.carville@ericsson.com"), LocalDate.of(2000, 5, 26));
        assertNotEquals(version,v1);
        v1 = new StandardVersion(ms.getId(), new BasicSemanticVersioning(2,7,4), "Description", bob, LocalDate.of(2000, 5, 26));
        v1.addDependency(version2);
        assertNotEquals(version,v1);

    }

    @Test
    void testEqualsNullFails() {
        assertFalse(version.equals(null));
    }

    @Test
    void testEqualsNotInstance() {
        assertFalse(version.equals("string"));
        assertFalse(version.equals(new StringBuilder()));
    }

    @Test
    void testHashcode() {
        assertEquals(new StandardVersion("name", new BasicSemanticVersioning(1, 19, 7),"d", new LeadEngineer("p","p"),LocalDate.now()).hashCode(), new StandardVersion("name", new BasicSemanticVersioning(1, 19, 7),"d", new LeadEngineer("p","p"),LocalDate.now()).hashCode());
        assertNotEquals(new StandardVersion("name1", new BasicSemanticVersioning(1, 19, 7),"d", new LeadEngineer("p","p"),LocalDate.now()).hashCode(), new StandardVersion("name", new BasicSemanticVersioning(1, 19, 7),"d", new LeadEngineer("p","p"),LocalDate.now()).hashCode());
    }


}
