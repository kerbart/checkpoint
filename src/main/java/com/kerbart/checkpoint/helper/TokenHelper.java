package com.kerbart.checkpoint.helper;

import org.apache.commons.lang3.RandomStringUtils;

public class TokenHelper {

    public static String generateToken() {
        return RandomStringUtils.randomAlphanumeric(42);
    }

}
