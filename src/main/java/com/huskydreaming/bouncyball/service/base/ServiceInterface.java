package com.huskydreaming.bouncyball.service.base;

import com.huskydreaming.bouncyball.BouncyBallPlugin;

public interface ServiceInterface {
    default void serialize(BouncyBallPlugin plugin) {

    }

    default void deserialize(BouncyBallPlugin plugin) {

    }
}