package com.huskydreaming.bouncyball.repositories.interfaces;

import com.huskydreaming.bouncyball.data.projectiles.ProjectileData;
import com.huskydreaming.huskycore.repositories.Repository;

import java.util.Map;

public interface ProjectileRepository extends Repository {

    ProjectileData getProjectileData(String name);

    void addProjectileData(String name, ProjectileData projectileData);

    void removeProjectileData(String name);

    boolean hasProjectileData(String name);

    Map<String, ProjectileData> getProjectileDataMap();
}
