package com.huskydreaming.bouncyball.services.interfaces;

import com.huskydreaming.bouncyball.services.base.ServiceInterface;
import com.huskydreaming.bouncyball.storage.Yaml;

public interface LocaleService extends ServiceInterface {


    Yaml getLocale();

    Yaml getMenu();
}
