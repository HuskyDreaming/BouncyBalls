package com.huskydreaming.bouncyball.registries;

import com.huskydreaming.bouncyball.BouncyBallPlugin;

public interface Registry {

    default void register(BouncyBallPlugin plugin) {

    }

    default void unregister(BouncyBallPlugin plugin) {

    }

}
