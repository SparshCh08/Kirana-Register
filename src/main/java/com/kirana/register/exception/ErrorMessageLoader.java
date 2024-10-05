package com.kirana.register.exception;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class ErrorMessageLoader {

    @Autowired
    private Environment env;

    public String getMessage(String key) {
        return env.getProperty("error.messages." + key + ".message");
    }

    public String getDescription(String key) {
        return env.getProperty("error.messages." + key + ".description");
    }

    public String getErrorCode(String key) {
        return env.getProperty("error.messages." + key + ".code");
    }
}
