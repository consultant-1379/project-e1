package com.team2.microservicemanagementtool.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TestStandardMicroService {

    static LeadEngineer bob = new LeadEngineer("Robert Carville", "robert.carville@ericsson.com");
    static StandardMicroService ms = new StandardMicroService("Rest Microservice", "APIs");
    static Version version = new StandardVersion(ms.getId(), new BasicSemanticVersioning(1, 0, 0), "Description", bob, LocalDate.of(2000, 5, 26));
    static Version version2 = new StandardVersion(ms.getId(), new BasicSemanticVersioning(1, 0, 1), "Description", bob, LocalDate.of(2000, 5, 27));
    static LinkedList<Version> sample = new LinkedList<>();

    @BeforeEach
    void init() {
        ms = new StandardMicroService("Rest Microservice", "APIs");
        sample = new LinkedList<>();
    }

    @Test
    void testGetName() {
        assertEquals("Rest Microservice", ms.getName());
    }

    @Test
    void testGetCategory() {
        assertEquals("APIs", ms.getCategory());
    }

    @Test
    void testGetVersion() {
        System.out.println(version.getVersionNumber());
        ms.addVersion(version);
        ms.addVersion(version2);

        assertEquals(version, ms.getVersion(version.getVersionNumber()));
        assertEquals(version2, ms.getVersion(version2.getVersionNumber()));
    }

    @Test
    void testGetAllVersions() {
        sample.add(version);
        sample.add(version2);
        ms.addVersion(version);
        ms.addVersion(version2);
        assertEquals(sample, ms.getVersions());
    }

    @Test
    void testSetVersions() {
        LinkedList<Version> expected = new LinkedList<>();
        sample.add(version);
        sample.add(version2);

        expected.add(version);
        expected.add(version2);

        ms.setVersions(sample);
        assertEquals(expected, ms.getVersions());
    }

    @Test
    void testGetCurrentVersion() {
        ms.addVersion(version2);
        assertEquals(version2, ms.getCurrentVersion());
    }

    @Test
    void testToString() {
        ms.addVersion(version);
        ms.addVersion(version2);
        assertEquals("{Rest Microservice-[1.0.1]}",
                ms.toString());
    }

    @Test
    void testGetVersionNames() {
        ms.addVersion(version);
        ms.addVersion(version2);
        String[] versionNums = {"1.0.0", "1.0.1"};
        List<String> versionNumList = Arrays.asList(versionNums);
        List<String> returned = ms.getVersionNames();
        assertTrue(versionNumList.size() == returned.size() && versionNumList.containsAll(returned) && returned.containsAll(versionNumList));
    }

    @Test
    void testEquals() {
        ms.addVersion(version);
        LeadEngineer lead = new LeadEngineer("Robert Carville", "robert.carville@ericsson.com");
        StandardMicroService ms1 = new StandardMicroService("Rest Microservice", "APIs");
        Version v1 = new StandardVersion(ms1.getId(), new BasicSemanticVersioning(1, 0, 0), "Description", lead, LocalDate.of(2000, 5, 26));
        ms1.addVersion(v1);

        assertEquals(ms, ms1);
        assertEquals(ms, ms);
        ms1.setName("bad");
        assertNotEquals(ms, ms1);
        ms1 = new StandardMicroService("Rest Microservice", "bad");
        v1 = new StandardVersion(ms1.getId(), new BasicSemanticVersioning(1, 0, 0), "Description", lead, LocalDate.of(2000, 5, 26));
        ms1.addVersion(v1);
        assertNotEquals(ms, ms1);
        ms1 = new StandardMicroService("Rest Microservice", "APIs");
        v1 = new StandardVersion(ms1.getId(), new BasicSemanticVersioning(1, 0, 0), "Description", lead, LocalDate.of(2000, 5, 26));
        ms1.addVersion(version2);
        assertNotEquals(ms, ms1);
        ms1.addVersion(version);
        assertNotEquals(ms,ms1);

    }

    @Test
    void testEqualsNullFails() {
        assertFalse(ms.equals(null));
    }

    @Test
    void testEqualsNotInstance() {
        assertFalse(ms.equals("string"));
        assertFalse(ms.equals(new StringBuilder()));
    }

    @Test
    void testHashcode() {
        assertEquals(new StandardMicroService("name", "cat").hashCode(), new StandardMicroService("name", "cat").hashCode());
        assertNotEquals(new StandardMicroService("name", "dog").hashCode(), new StandardMicroService("name", "cat").hashCode());
    }
}