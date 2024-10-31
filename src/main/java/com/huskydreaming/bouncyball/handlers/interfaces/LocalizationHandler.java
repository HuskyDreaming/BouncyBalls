package com.huskydreaming.bouncyball.handlers.interfaces;

import com.huskydreaming.huskycore.handlers.interfaces.Handler;
import com.huskydreaming.huskycore.storage.Yaml;

public interface LocalizationHandler extends Handler {

    Yaml getLocale();

    Yaml getMenu();
}
