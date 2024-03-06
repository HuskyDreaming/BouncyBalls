package com.huskydreaming.bouncyball.registries;

import com.huskydreaming.bouncyball.BouncyBallPlugin;
import com.huskydreaming.bouncyball.services.base.ServiceInterface;
import com.huskydreaming.bouncyball.services.implementations.InventoryServiceImpl;
import com.huskydreaming.bouncyball.services.implementations.LocaleServiceImpl;
import com.huskydreaming.bouncyball.services.implementations.ParticleServiceImpl;
import com.huskydreaming.bouncyball.services.implementations.ProjectileServiceImpl;
import com.huskydreaming.bouncyball.services.interfaces.InventoryService;
import com.huskydreaming.bouncyball.services.interfaces.LocaleService;
import com.huskydreaming.bouncyball.services.interfaces.ParticleService;
import com.huskydreaming.bouncyball.services.interfaces.ProjectileService;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ServiceRegistry implements Registry {

    private final Map<Class<?>, ServiceInterface> services = new ConcurrentHashMap<>();

    @Override
    public void register(BouncyBallPlugin plugin) {
        services.put(LocaleService.class, new LocaleServiceImpl());
        services.put(InventoryService.class, new InventoryServiceImpl());
        services.put(ProjectileService.class, new ProjectileServiceImpl());
        services.put(ParticleService.class, new ParticleServiceImpl(plugin));
        services.values().forEach(serviceInterface -> serviceInterface.deserialize(plugin));
    }

    @Override
    public void unregister(BouncyBallPlugin plugin) {
        services.values().forEach(serviceInterface -> serviceInterface.serialize(plugin));
    }

    public Map<Class<?>, ServiceInterface> getServices() {
        return Collections.unmodifiableMap(services);
    }
}
