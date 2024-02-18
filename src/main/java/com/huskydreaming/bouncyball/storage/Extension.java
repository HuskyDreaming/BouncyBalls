package com.huskydreaming.bouncyball.storage;

public enum Extension {
    YAML(".yml");

    private final String string;

    Extension(String string) {
        this.string = string;
    }

    @Override
    public String toString() {
        return string;
    }
}