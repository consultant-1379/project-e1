package com.team2.microservicemanagementtool.persistence;

import com.team2.microservicemanagementtool.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.*;

@Component
public class MsService
{
    @Autowired
    private MicroServiceRepository microServiceRepository;

    public void test()
    {
        // sonarqube complained about repetition of the string literal "database"
        // so moved to variable
        String databaseString = "database";
        StandardMicroService ms = new StandardMicroService("My microservice 2", databaseString);
        StandardVersion v1 = new StandardVersion(ms.getId(), new BasicSemanticVersioning(1,0,0),"version 1",new LeadEngineer("peter","p@mail.ie"), LocalDate.now());
        ms.addVersion(v1);
        StandardVersion v = new StandardVersion(ms.getId(), new BasicSemanticVersioning(1,0,1),"version 453",new LeadEngineer("peter","p@mail.ie"), LocalDate.now());
        ms.addVersion(v);
        microServiceRepository.save(ms);
        ms = new StandardMicroService("My microservice 3", databaseString);
        StandardVersion v2 = new StandardVersion(ms.getId(), new BasicSemanticVersioning(2,0,0),"version 1",new LeadEngineer("peter","p@mail.ie"), LocalDate.now());
        ms.addVersion(v2);
        microServiceRepository.save(ms);
        ms = new StandardMicroService("My microservice 4", databaseString);
        StandardVersion v3 = new StandardVersion(ms.getId(), new BasicSemanticVersioning(1,0,0),"version 1",new LeadEngineer("peter","p@mail.ie"), LocalDate.now());
        v3.addDependency(v2);
        v3.addDependency(v1);
        ms.addVersion(v3);
        microServiceRepository.save(ms);
        ms = new StandardMicroService("My microservice 5", "database");
        StandardVersion v4 = new StandardVersion(ms.getId(), new BasicSemanticVersioning(2,0,0),"version 1",new LeadEngineer("peter","p@mail.ie"), LocalDate.now());
        ms.addVersion(v4);
        microServiceRepository.save(ms);

        microServiceRepository.save(ms);
    }


    public MicroService getMicroServiceById(String name)
    {
        Optional<MicroService> queried = microServiceRepository.findById(name.trim().toLowerCase().replace(" ","_"));
        if(queried.isPresent())
           return queried.get();
        else
            return null;
    }

    public List<MicroService> getMicroServicesByCategory(String category)
    {
        return microServiceRepository.findMicroServicesByCategory(category);
    }

    public List<MicroService> getAllMicroServices()
    {
        return microServiceRepository.findAll();
    }

    public String createMicroService(StandardMicroService standardMicroService)
    {
        try{
            microServiceRepository.save(standardMicroService);
            return standardMicroService.toString();
        }catch(Exception e)
        {
            return "ERROR: error adding microservice "+standardMicroService+" to database";
        }

    }

    public String createVersion(String id, StandardVersion standardVersion)
    {
        try{
            MicroService toAddTo = getMicroServiceById(id);
            toAddTo.addVersion(standardVersion);
            microServiceRepository.save(toAddTo);   //may needd tto     change to updatee
            return standardVersion.toString();
        }catch(Exception e)
        {
            return "ERROR: error adding version "+standardVersion+" to database";
        }
    }

    public Version getVersionOfMicroService(String msId, String versionNum)
    {
        BasicSemanticVersioning v = parseVersion(versionNum);
        return microServiceRepository.findVersionOfMicroService(msId, v.getMajor(), v.getMinor(), v.getPatch());
    }

    public List<String> getListOfAllVersionsOfMicroService(String id)
    {
        Optional<MicroService> queried = microServiceRepository.findById(id);
        if(queried.isPresent())
            return ((StandardMicroService)queried.get()).getVersionNames();
        else
            return new LinkedList<>();
    }

    public List<MicroService> getMicroServicesWithoutDependencies()
    {
        return microServiceRepository.findIndependentCurrentVersion();
    }

    public List<Version> getDependenciesOfVersionOfMicroService(String msId, String versionNum) {
        BasicSemanticVersioning v = parseVersion(versionNum);
        return new LinkedList<>(microServiceRepository.findDependenciesByVersion(msId, v.getMajor(), v.getMinor(), v.getPatch()));
    }

    public List<Version> getDependentsOfVersionOfMicroService(String msId, String versionNum)
    {
        BasicSemanticVersioning v = parseVersion(versionNum);
        return new LinkedList<>(microServiceRepository.findDependentsByVersion(msId, v.getMajor(), v.getMinor(), v.getPatch()));
    }

    public List<String> getAllMicroServiceNames()
    {
        return microServiceRepository.findAllMicroServiceNames();
    }

    private BasicSemanticVersioning parseVersion(String versionNumber)
    {
        String[] mmp = versionNumber.split("\\.|-");
        return new BasicSemanticVersioning(Integer.parseInt(mmp[0]), Integer.parseInt(mmp[1]), Integer.parseInt(mmp[2]));
    }

}