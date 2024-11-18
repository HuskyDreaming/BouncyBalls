package com.huskydreaming.bouncyball.handlers.implementations;

import com.huskydreaming.bouncyball.handlers.interfaces.LocalizationHandler;
import com.huskydreaming.bouncyball.enumerations.Locale;
import com.huskydreaming.bouncyball.enumerations.Menu;
import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.huskycore.storage.Yaml;

public class LocalizationHandlerImpl implements LocalizationHandler {

    private Yaml locale;
    private Yaml menu;

    @Override
    public void initialize(HuskyPlugin plugin) {
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
    public void reload(HuskyPlugin plugin) {
        locale.load(plugin);
        Locale.setConfiguration(locale.getConfiguration());

        for (Locale message : Locale.values()) {
            locale.getConfiguration().set(message.toString(), message.parseList() != null ? message.parseList() : message.parse());
        }

        menu.load(plugin);
        Menu.setConfiguration(menu.getConfiguration());

        for (Menu message : Menu.values()) {
            menu.getConfiguration().set(message.toString(), message.parseList() != null ? message.parseList() : message.parse());
        }
    }
}
