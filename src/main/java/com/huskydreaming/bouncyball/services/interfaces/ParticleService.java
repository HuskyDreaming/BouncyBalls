package com.huskydreaming.bouncyball.services.interfaces;

import com.huskydreaming.bouncyball.data.ParticleData;
import com.huskydreaming.bouncyball.services.base.ServiceInterface;

public interface ParticleService extends ServiceInterface {

    void update(String name, ParticleData particleData);

    ParticleData getParticle(String key);

    void addParticle(String key, ParticleData particleData);
}