package com.team2.microservicemanagementtool.persistence;


import com.team2.microservicemanagementtool.models.MicroService;
import com.team2.microservicemanagementtool.models.StandardVersion;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MicroServiceRepository extends MongoRepository<MicroService, String>
{
    List<MicroService> findMicroServicesByCategory(String category);

    @Aggregation(pipeline = {"{'$unwind':'$versions'}","{'$match':{'versions.dependencies.parentId':?0,'versions.dependencies.versionNumber.major':?1,'versions.dependencies.versionNumber.minor':?2,'versions.dependencies.versionNumber.patch':?3}}","{'$replaceRoot':{ newRoot :'$versions'}}"})
    List<StandardVersion> findDependentsByVersion(String parentId, int major, int minor, int patch);

    @Aggregation(pipeline = {"{'$unwind':'$versions'}","{'$match':{'versions.parentId':?0,'versions.versionNumber.major':?1,'versions.versionNumber.minor':?2,'versions.versionNumber.patch':?3}}","{'$unwind':'$versions.dependencies'}","{'$replaceRoot':{ newRoot :'$versions.dependencies'}}"})
    List<StandardVersion> findDependenciesByVersion(String parentId, int major, int minor, int patch);

    @Query("{'currentVersion.dependencies':{'$size':0}}")
    List<MicroService> findIndependentCurrentVersion();

    @Aggregation(pipeline = {"{'$unwind':'$versions'}","{'$match':{'versions.parentId':?0,'versions.versionNumber.major':?1,'versions.versionNumber.minor':?2,'versions.versionNumber.patch':?3}}","{'$replaceRoot':{ newRoot :'$versions'}}"})
    StandardVersion findVersionOfMicroService(String parentId, int major, int minor, int patch);

    @Aggregation(pipeline = {"{'$project': { '_id' : '$name' }}"})
    List<String> findAllMicroServiceNames();
}

