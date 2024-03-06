package com.huskydreaming.bouncyball.services.base;

import com.huskydreaming.bouncyball.BouncyBallPlugin;

public interface ServiceInterface {
    default void serialize(BouncyBallPlugin plugin) {

    }

    default void deserialize(BouncyBallPlugin plugin) {

    }
}