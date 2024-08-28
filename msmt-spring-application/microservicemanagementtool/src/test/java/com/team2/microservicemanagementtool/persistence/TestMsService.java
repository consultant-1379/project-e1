package com.team2.microservicemanagementtool.persistence;

import com.team2.microservicemanagementtool.models.*;
import org.junit.Rule;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Testcontainers
@DataMongoTest()
@Import(MsService.class)
class TestMsService {

    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:4.4.0");

    @Autowired
    private MicroServiceRepository microServiceRepository;

    private static StandardMicroService ms;
    private static StandardMicroService ms1;
    private static StandardMicroService ms2;
    private static StandardMicroService ms3;
    private static StandardVersion v;
    private static StandardVersion v1;
    private static StandardVersion v2;
    private static StandardVersion v3;
    private static StandardVersion v4;


    @Autowired
    private MsService msService;

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry dynamicPropertyRegistry)
    {
        dynamicPropertyRegistry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @BeforeEach
    void init()
    {
        ms = new StandardMicroService("My microservice 3", "Database");
        v2 = new StandardVersion(ms.getId(), new BasicSemanticVersioning(2,0,0),"version 2 MAJOR",new LeadEngineer("mel","mel@mail.ie"), LocalDate.now());
        ms.addVersion(v2);
        microServiceRepository.save(ms);

        ms1 = new StandardMicroService("My microservice 2", "Database");
        v1 = new StandardVersion(ms1.getId(), new BasicSemanticVersioning(1,0,0),"version 1 MAJOR",new LeadEngineer("peter","p@mail.ie"), LocalDate.now());
        v = new StandardVersion(ms1.getId(), new BasicSemanticVersioning(1,0,1),"version 1 PATCH",new LeadEngineer("peter","p@mail.ie"), LocalDate.now());
        v.addDependency(v2);
        ms1.addVersion(v);
        ms1.addVersion(v1);
        microServiceRepository.save(ms1);

        ms2 = new StandardMicroService("My microservice 4", "Web app");
        v3 = new StandardVersion(ms2.getId(), new BasicSemanticVersioning(3,0,1),"version 3 PATCH",new LeadEngineer("paul","paul@mail.ie"), LocalDate.now());
        v3.addDependency(v2);
        v3.addDependency(v1);
        ms2.addVersion(v3);
        microServiceRepository.save(ms2);

        ms3 = new StandardMicroService("My microservice 5", "Cloud");
        v4 = new StandardVersion(ms3.getId(), new BasicSemanticVersioning(2,0,0),"version 2 MAJOR",new LeadEngineer("michael","michael@mail.ie"), LocalDate.now());
        v4.addDependency(v2);
        ms3.addVersion(v4);
        microServiceRepository.save(ms3);
    }

    @AfterEach
    void clearRepo()
    {
        microServiceRepository.deleteAll();
    }

    @Test
    void testGetMicroServiceById()
    {
        MicroService m5 = msService.getMicroServiceById("my_microservice_5");
        assertEquals(ms3,m5);
        assertNull(msService.getMicroServiceById("my_microservice_59"));
    }

    @Test
    void testGetMicrosServicesByCategory()
    {
        List<MicroService> dbs = msService.getMicroServicesByCategory("Database");
        List<MicroService> cloudApps = msService.getMicroServicesByCategory("Cloud");
        List<MicroService> warships = msService.getMicroServicesByCategory("Warship");
        List<MicroService> webApps = msService.getMicroServicesByCategory("Web app");

        assertEquals(2,dbs.size());
        assertEquals(1,cloudApps.size());
        assertEquals(1,webApps.size());
        assertEquals(0,warships.size());

        assertTrue(dbs.stream().map(m -> m.getId()).collect(Collectors.toList()).contains("my_microservice_2"));
        assertTrue(dbs.stream().map(m -> m.getId()).collect(Collectors.toList()).contains("my_microservice_3"));
        assertTrue(webApps.stream().map(m -> m.getId()).collect(Collectors.toList()).contains("my_microservice_4"));
        assertTrue(cloudApps.stream().map(m -> m.getId()).collect(Collectors.toList()).contains("my_microservice_5"));
    }

    @Test
    void testGetAllMicroServices()
    {
        List<MicroService> microServices = msService.getAllMicroServices();
        List<MicroService> microServices1 = new LinkedList<>();
        microServices1.add(ms);
        microServices1.add(ms1);
        microServices1.add(ms2);
        microServices1.add(ms3);
        assertTrue(microServices.size()==microServices1.size() && microServices.containsAll(microServices1) && microServices1.containsAll(microServices));
    }

    @Test
    void testCreateMicroService()
    {
        StandardMicroService ms4 = new StandardMicroService("My microservice 7", "Cloud");
        StandardVersion v5 = new StandardVersion(ms4.getId(), new BasicSemanticVersioning(2,1,0),"version 2 MAJOR 1 MINOR",new LeadEngineer("mel","mel@mail.ie"), LocalDate.now());
        ms4.addVersion(v5);
        assertEquals(ms4.toString(),msService.createMicroService(ms4));
        assertEquals(ms4,msService.getMicroServiceById(ms4.getId()));
        assertEquals(5,msService.getAllMicroServices().size());
    }

    @Test
    void testCreateVersion()
    {
        StandardVersion v5 = new StandardVersion(ms.getId(), new BasicSemanticVersioning(2,1,0),"version 2 MAJOR 1 MINOR",new LeadEngineer("mel","mel@mail.ie"), LocalDate.now());
        assertEquals(v5.toString(),msService.createVersion(ms.getId(),v5));
        assertEquals(v5, msService.getMicroServiceById(ms.getId()).getCurrentVersion());
        assertEquals(2,msService.getMicroServiceById(ms.getId()).getVersions().size());
    }

    @Test
    void testGetVersionOfMicroService()
    {
        Version version =  msService.getVersionOfMicroService(ms1.getId(),v.getVersionNumber().toString());
        Version version1 = msService.getVersionOfMicroService(ms1.getId(),v1.getVersionNumber().toString());

        assertEquals(v,version);
        assertEquals(v1,version1);
    }

    @Test
    void testGetListOfAllVersionsOfMicroService()
    {
        List<String> names = msService.getListOfAllVersionsOfMicroService(ms1.getId());
        String[] ns = {"1.0.0","1.0.1"};
        List<String> names1 = Arrays.asList(ns);
        assertTrue(names.size()==names1.size() && names.containsAll(names1) && names1.containsAll(names));
        assertTrue(msService.getListOfAllVersionsOfMicroService("fake_id").isEmpty());
    }

    @Test
    void testGetDependenciesOfMicroService()
    {
        List<Version> dependencies = msService.getDependenciesOfVersionOfMicroService(v3.getParentId(),v3.getVersionNumber().toString());
        assertEquals(2,dependencies.size());
        assertTrue(dependencies.contains(v2));
        assertTrue(dependencies.contains(v1));

        dependencies = msService.getDependenciesOfVersionOfMicroService(v4.getParentId(),v4.getVersionNumber().toString());
        assertEquals(1,dependencies.size());
        assertTrue(dependencies.contains(v2));

        dependencies = msService.getDependenciesOfVersionOfMicroService(v2.getParentId(),v2.getVersionNumber().toString());
        assertEquals(0,dependencies.size());
    }

    @Test
    void testGetDependentsOfMicroService()
    {
        List<Version> dependents = msService.getDependentsOfVersionOfMicroService(v2.getParentId(),v2.getVersionNumber().toString());
        assertEquals(3,dependents.size());
        assertTrue(dependents.contains(v));
        assertTrue(dependents.contains(v4));
        assertTrue(dependents.contains(v3));

        dependents = msService.getDependentsOfVersionOfMicroService(v1.getParentId(),v1.getVersionNumber().toString());
        assertEquals(1,dependents.size());
        assertTrue(dependents.contains(v3));

        dependents = msService.getDependentsOfVersionOfMicroService(v4.getParentId(),v4.getVersionNumber().toString());
        assertEquals(0,dependents.size());
    }

    @Test
    void testGetAllMicroServiceNames()
    {
        List<String> msn = msService.getAllMicroServiceNames();
        String[] nameList = {"My microservice 3","My microservice 2","My microservice 4","My microservice 5"};
        List<String> names = Arrays.asList(nameList);
        assertTrue(names.size() == msn.size() && names.containsAll(msn) &&  msn.containsAll(names));
    }

    @Test
    void testGetMicroServicesWithoutDependencies()
    {
        List<MicroService> nodep = msService.getMicroServicesWithoutDependencies();
        assertEquals(2,nodep.size());
        assertTrue(nodep.contains(ms));
        assertTrue(nodep.contains(ms1));
    }

}
