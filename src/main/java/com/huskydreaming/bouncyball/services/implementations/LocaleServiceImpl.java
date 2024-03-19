package com.huskydreaming.bouncyball.services.implementations;

import com.huskydreaming.bouncyball.services.interfaces.LocaleService;
import com.huskydreaming.bouncyball.pareseables.Locale;
import com.huskydreaming.bouncyball.pareseables.Menu;
import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.huskycore.storage.Yaml;

public class LocaleServiceImpl implements LocaleService {

    private Yaml locale;
    private Yaml menu;

    @Override
    public void deserialize(HuskyPlugin plugin) {

        // Localization for general messages
        locale = new Yaml("localisation/locale");
        locale.load(plugin);
        Locale.setConfiguration(locale.getConfiguration());

        for (Locale message : Locale.values()) {
            locale.getConfiguration().set(message.toString(), message.parseList() != null ? message.parseList() : message.parse());
        }
        locale.save();

        // Localization for menus
        menu = new Yaml("menus/bouncyballs");
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