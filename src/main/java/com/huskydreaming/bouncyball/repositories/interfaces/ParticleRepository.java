package com.huskydreaming.bouncyball.repositories.interfaces;

import com.huskydreaming.bouncyball.data.particles.ParticleData;
import com.huskydreaming.huskycore.repositories.Repository;

public interface ParticleRepository extends Repository {

    void addParticleData(String key, ParticleData particleData);

    ParticleData getParticleData(String name);
}
