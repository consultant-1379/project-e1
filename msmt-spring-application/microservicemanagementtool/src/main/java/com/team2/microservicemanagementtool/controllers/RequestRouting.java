package com.team2.microservicemanagementtool.controllers;

import com.team2.microservicemanagementtool.models.*;
import com.team2.microservicemanagementtool.persistence.MsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.util.List;

@RestController
@CrossOrigin
public class RequestRouting {

    @Autowired
    private MsService msService;

   /* @PostConstruct
    public void tester()
    {
        System.out.println(getAllMicroServices());
        System.out.println("===================================");
        System.out.println(getMicroServiceById("my_microservice_4").getCurrentVersion().getDependencies());
        System.out.println("===================================");
        System.out.println(getForCategory("Web app"));
        System.out.println("===================================");
        System.out.println(getVersionOfMicroService("my_microservice_2","1-0-1").getDescription());
        System.out.println("===================================");
        System.out.println(getDependencies("my_microservice_4","1-0-0"));
        System.out.println("===================================");
        System.out.println(getListOfAllMicroServiceNames());
        System.out.println("===================================");
        System.out.println(getListVersions("my_microservice_2"));
        System.out.println("===================================");
        System.out.println(getMicroServicesWithoutDependencies());
        StandardMicroService ms = new StandardMicroService("My microservice 6", "database");
        StandardVersion v1 = new StandardVersion(ms.getId(), new BasicSemanticVersioning(1,0,7),"version 3",new LeadEngineer("peter","p@mail.ie"), LocalDate.now());
        createMicroService(ms,v1);
        System.out.println(getAllMicroServices());
        System.out.println(getListVersions("my_microservice_6"));
        System.out.println("===================================");
        v1 = new StandardVersion(ms.getId(), new BasicSemanticVersioning(1,0,8),"version 3",new LeadEngineer("peter","p@mail.ie"), LocalDate.now());
        createVersion(ms.getId(),v1);
        System.out.println(getListVersions("my_microservice_6"));
        System.out.println("===================================");
    }*/

    @GetMapping("/microservices")
    public List<MicroService> getAllMicroServices()
    {
        return msService.getAllMicroServices();
    }

    @GetMapping("/microservices/{id}")
    public MicroService getMicroServiceById(@PathVariable String id){
        return  msService.getMicroServiceById(id);
    }

    @GetMapping("/microservices/category")
    public List<MicroService> getForCategory(@RequestParam(value = "category") String category)
    {
        return msService.getMicroServicesByCategory(category);
    }

    @PostMapping("/microservices")
    public String createMicroService(@RequestBody StandardMicroServiceDTO standardMicroServiceDTO)
    {
        StandardMicroService standardMicroService = new StandardMicroService(standardMicroServiceDTO.getName(),standardMicroServiceDTO.getCategory());
        StandardVersion standardVersion = new StandardVersion(standardMicroServiceDTO.getName(), standardMicroServiceDTO.getVersionNumber(), standardMicroServiceDTO.getDescription(), standardMicroServiceDTO.getLeadEngineer(), LocalDate.now());
        standardVersion.setDependencies(standardMicroServiceDTO.getDependencies());
        standardMicroService.addVersion(standardVersion);
        System.out.println("===================================\nstandardMicroService\n===================================\n");
        return msService.createMicroService(standardMicroService);
    }

    @PostMapping("/microservices/{id}/versions")
    public String createVersion(@PathVariable String id, @RequestBody StandardVersion standardVersion){
        return msService.createVersion(id, standardVersion);
    }

    @GetMapping("/microservices/{msId}/versions/{versionNum}")
    public Version getVersionOfMicroService(@PathVariable String msId, @PathVariable String versionNum){
        return  msService.getVersionOfMicroService(msId, versionNum);
    }

    @GetMapping("/microservices/{msId}/versions/{versionNum}/dependencies")
    public List<Version> getDependencies(@PathVariable String msId, @PathVariable String versionNum){
        return msService.getDependenciesOfVersionOfMicroService(msId, versionNum);
    }

    @GetMapping("/microservices/{msId}/versions/{versionNum}/dependents")
    public List<Version> getDependents(@PathVariable String msId, @PathVariable String versionNum){
        return msService.getDependentsOfVersionOfMicroService(msId, versionNum);
    }

    @GetMapping("/microservices/names")
    public List<String> getListOfAllMicroServiceNames(){
        return msService.getAllMicroServiceNames();
    }

    //currently returns version string, can be made return version objects either
    @GetMapping("/microservices/{id}/versions")
    public List<String> getListVersions(@PathVariable String id){
        return msService.getListOfAllVersionsOfMicroService(id);
    }

    @GetMapping("/microservices/independent")
    public List<MicroService> getMicroServicesWithoutDependencies(){
        return msService.getMicroServicesWithoutDependencies();
    }
}
