package com.loanmanagement.util;

import java.util.UUID;

public class TokenUtil {

    private TokenUtil() {
        
    }

    public static String generateToken(String username) {

       
        return username + "-" + UUID.randomUUID().toString();
    }
}
