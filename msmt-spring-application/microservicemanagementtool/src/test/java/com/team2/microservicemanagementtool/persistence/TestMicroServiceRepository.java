package com.team2.microservicemanagementtool.persistence;

import com.team2.microservicemanagementtool.models.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@DataMongoTest()
class TestMicroServiceRepository {

    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:4.4.0");

    @Autowired
    private MicroServiceRepository microServiceRepository;


   @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry dynamicPropertyRegistry)
    {
        dynamicPropertyRegistry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @BeforeEach
    void init()
    {
        StandardMicroService ms = new StandardMicroService("My microservice 3", "Database");
        StandardVersion v2 = new StandardVersion(ms.getId(), new BasicSemanticVersioning(2,0,0),"version 2 MAJOR",new LeadEngineer("mel","mel@mail.ie"), LocalDate.now());
        ms.addVersion(v2);
        microServiceRepository.save(ms);

        ms = new StandardMicroService("My microservice 2", "Database");
        StandardVersion v1 = new StandardVersion(ms.getId(), new BasicSemanticVersioning(1,0,0),"version 1 MAJOR",new LeadEngineer("peter","p@mail.ie"), LocalDate.now());
        StandardVersion v = new StandardVersion(ms.getId(), new BasicSemanticVersioning(1,0,1),"version 1 PATCH",new LeadEngineer("peter","p@mail.ie"), LocalDate.now());
        v.addDependency(v2);
        ms.addVersion(v);
        ms.addVersion(v1);
        microServiceRepository.save(ms);

        ms = new StandardMicroService("My microservice 4", "Web app");
        StandardVersion v3 = new StandardVersion(ms.getId(), new BasicSemanticVersioning(3,0,1),"version 3 PATCH",new LeadEngineer("paul","paul@mail.ie"), LocalDate.now());
        v3.addDependency(v2);
        v3.addDependency(v1);
        ms.addVersion(v3);
        microServiceRepository.save(ms);

        ms = new StandardMicroService("My microservice 5", "Cloud");
        StandardVersion v4 = new StandardVersion(ms.getId(), new BasicSemanticVersioning(2,0,0),"version 2 MAJOR",new LeadEngineer("michael","michael@mail.ie"), LocalDate.now());
        v4.addDependency(v2);
        ms.addVersion(v4);
        microServiceRepository.save(ms);
    }

    @AfterEach
    void clearRepo()
    {
        microServiceRepository.deleteAll();
    }

    @Test
    void testFindAll()
    {
        List<MicroService> all = microServiceRepository.findAll();
        assertEquals(4,all.size());
    }

    @Test
    void testByID()
    {
        Optional<MicroService> ms5cp = microServiceRepository.findById("my_microservice_5");
        assertEquals("my_microservice_5",ms5cp.get().getId());
    }

    @Test
    void testSave()
    {
        StandardMicroService ms = new StandardMicroService("My microservice 6", "Stateless");
        StandardVersion v4 = new StandardVersion(ms.getId(), new BasicSemanticVersioning(2,0,0),"version 2 MAJOR",new LeadEngineer("michael","michael@mail.ie"), LocalDate.now());
        ms.addVersion(v4);
        microServiceRepository.save(ms);
        List<MicroService> all = microServiceRepository.findAll();
        assertEquals(5,all.size());

        Optional<MicroService> ms6cp = microServiceRepository.findById("my_microservice_6");
        assertEquals(true,ms6cp.isPresent());
    }

    @Test
    void testgetForCategory()
    {
        List<MicroService> databases = microServiceRepository.findMicroServicesByCategory("Database");
        List<MicroService> cloud = microServiceRepository.findMicroServicesByCategory("Cloud");
        List<MicroService> warship = microServiceRepository.findMicroServicesByCategory("Warship");
        List<MicroService> webApps = microServiceRepository.findMicroServicesByCategory("Web app");

        assertEquals(2,databases.size());
        assertEquals(1,cloud.size());
        assertEquals(1,webApps.size());
        assertEquals(0,warship.size());

        assertTrue(databases.stream().map(m -> m.getId()).collect(Collectors.toList()).contains("my_microservice_2"));
        assertTrue(databases.stream().map(m -> m.getId()).collect(Collectors.toList()).contains("my_microservice_3"));
        assertTrue(webApps.stream().map(m -> m.getId()).collect(Collectors.toList()).contains("my_microservice_4"));
        assertTrue(cloud.stream().map(m -> m.getId()).collect(Collectors.toList()).contains("my_microservice_5"));
    }

    @Test
    void testGetDependents()
    {
        StandardVersion v2 = new StandardVersion("my_microservice_3", new BasicSemanticVersioning(2,0,0),"version 2 MAJOR",new LeadEngineer("mel","mel@mail.ie"), LocalDate.now());
        List<StandardVersion> ms = microServiceRepository.findDependentsByVersion(v2.getParentId(),v2.getVersionNumber().getMajor(), v2.getVersionNumber().getMinor(), v2.getVersionNumber().getPatch());
        assertEquals(3,ms.size());

        StandardVersion v1 = new StandardVersion("my_microservice_2", new BasicSemanticVersioning(1,0,0),"version 1 MAJOR",new LeadEngineer("peter","p@mail.ie"), LocalDate.now());
        List<StandardVersion> ms1 = microServiceRepository.findDependentsByVersion(v1.getParentId(),v1.getVersionNumber().getMajor(), v1.getVersionNumber().getMinor(), v1.getVersionNumber().getPatch());
        assertEquals(1,ms1.size());

        StandardVersion notADependency = new StandardVersion("my_microservice_5", new BasicSemanticVersioning(2,0,0),"version 1 MAJOR",new LeadEngineer("peter","p@mail.ie"), LocalDate.now());
        List<StandardVersion> ms2= microServiceRepository.findDependentsByVersion(notADependency.getParentId(),notADependency.getVersionNumber().getMajor(), notADependency.getVersionNumber().getMinor(), notADependency.getVersionNumber().getPatch());
        assertEquals(0,ms2.size());
    }

    @Test
    void testGetIndependents()
    {
        List<MicroService> ms = microServiceRepository.findIndependentCurrentVersion();
        assertEquals(2,ms.size());
    }

    @Test
    void testGetVersion()
    {
        StandardVersion v = new StandardVersion("my_microservice_2", new BasicSemanticVersioning(1,0,1),"versionCH",new LeadEngineer("peter","p@mail.ie"), LocalDate.now());
        StandardVersion v1 = microServiceRepository.findVersionOfMicroService(v.getParentId(),v.getVersionNumber().getMajor(), v.getVersionNumber().getMinor(), v.getVersionNumber().getPatch());
        assertEquals(v.toString(),v1.toString());
        v1 = microServiceRepository.findVersionOfMicroService("not_real",v.getVersionNumber().getMajor(), v.getVersionNumber().getMinor(), v.getVersionNumber().getPatch());
        assertNull(v1);
    }

    @Test
    void testGetMicroServiceNames()
    {
        List<String> names = microServiceRepository.findAllMicroServiceNames();
        String[] nameList = {"My microservice 3","My microservice 2","My microservice 4","My microservice 5"};
        List<String> names1 = Arrays.asList(nameList);
        assertTrue(names.size() == names1.size() && names.containsAll(names1) && names1.containsAll(names));
    }

    @Test
    void testGetDependencies()
    {
        StandardVersion v3 = new StandardVersion("my_microservice_4", new BasicSemanticVersioning(3,0,1),"version 3 PATCH",new LeadEngineer("paul","paul@mail.ie"), LocalDate.now());
        List<StandardVersion> ms = microServiceRepository.findDependenciesByVersion(v3.getParentId(),v3.getVersionNumber().getMajor(), v3.getVersionNumber().getMinor(), v3.getVersionNumber().getPatch());
        assertEquals(2,ms.size());

        StandardVersion v = new StandardVersion("my_microservice_2", new BasicSemanticVersioning(1,0,1),"version 1 PATCH",new LeadEngineer("peter","p@mail.ie"), LocalDate.now());
        List<StandardVersion> ms1 = microServiceRepository.findDependenciesByVersion(v.getParentId(),v.getVersionNumber().getMajor(), v.getVersionNumber().getMinor(), v.getVersionNumber().getPatch());
        assertEquals(1,ms1.size());

        StandardVersion v4 = new StandardVersion("my_microservice_5", new BasicSemanticVersioning(2,0,0),"version 2 MAJOR",new LeadEngineer("michael","michael@mail.ie"), LocalDate.now());
        List<StandardVersion> ms2 = microServiceRepository.findDependenciesByVersion(v4.getParentId(),v4.getVersionNumber().getMajor(), v4.getVersionNumber().getMinor(), v4.getVersionNumber().getPatch());
        assertEquals(1,ms2.size());

        StandardVersion v1 = new StandardVersion("my_microservice_2", new BasicSemanticVersioning(1,0,0),"version 1 MAJOR",new LeadEngineer("peter","p@mail.ie"), LocalDate.now());
        List<StandardVersion> ms3 = microServiceRepository.findDependenciesByVersion(v1.getParentId(),v1.getVersionNumber().getMajor(), v1.getVersionNumber().getMinor(), v1.getVersionNumber().getPatch());
        assertEquals(0,ms3.size());
    }

}
