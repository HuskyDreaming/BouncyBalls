package com.huskydreaming.bouncyball.services.implementations;

import com.huskydreaming.bouncyball.BouncyBallPlugin;
import com.huskydreaming.bouncyball.services.interfaces.LocaleService;
import com.huskydreaming.bouncyball.storage.enumeration.Locale;
import com.huskydreaming.bouncyball.storage.enumeration.Menu;
import com.huskydreaming.bouncyball.storage.base.Yaml;

public class LocaleServiceImpl implements LocaleService {

    private Yaml locale;
    private Yaml menu;

    @Override
    public void deserialize(BouncyBallPlugin plugin) {

        // Localization for general messages
        locale = new Yaml("localisation/locale");
        locale.load(plugin);
        Locale.setConfiguration(locale.getConfiguration());

        for (Locale message : Locale.values()) {
            locale.getConfiguration().set(message.toString(), message.parseList() != null ? message.parseList() : message.parse());
        }
        locale.save();

        // Localization for menus
        menu = new Yaml("localisation/menu");
        menu.load(plugin);
        Menu.setConfiguration(menu.getConfiguration());

        for (Menu message : Menu.values()) {
            menu.getConfiguration().set(message.toString(), message.parseList() != null ? message.parseList() : message.parse());
        }
        menu.save();
    }

    @Override
    public Yaml getLocale() {
        return locale;
    }

    @Override
    public Yaml getMenu() {
        return menu;
    }
}