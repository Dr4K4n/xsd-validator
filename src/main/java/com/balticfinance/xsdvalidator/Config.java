package com.balticfinance.xsdvalidator;

import java.io.IOException;
import java.util.Properties;

public class Config extends Properties {

    private static final long serialVersionUID = 1L;

    private static final String KEY_PROGRAM_NAME = "PROGRAM_NAME";
    private static final String KEY_VERSION = "VERSION";

    public Config() {
        try {
            this.load(Config.class.getResourceAsStream("/application.properties"));
        } catch (IOException e) {
            System.out.println("Could not load application.properties:");
            System.out.println(e.getMessage());
        }
    }

    public String getApplicationName() {
        return this.getProperty(KEY_PROGRAM_NAME);
    }

    public String getVersion() {
        return this.getProperty(KEY_VERSION);
    }
}
