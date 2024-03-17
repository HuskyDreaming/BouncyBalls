package com.huskydreaming.bouncyball.services.interfaces;

import com.huskydreaming.bouncyball.BouncyBallPlugin;
import com.huskydreaming.bouncyball.data.particles.ParticleData;
import com.huskydreaming.huskycore.interfaces.Service;

public interface ParticleService extends Service {

    void addParticle(String key, ParticleData particleData);

    ParticleData getParticle(String key);

    void run(BouncyBallPlugin plugin);
}