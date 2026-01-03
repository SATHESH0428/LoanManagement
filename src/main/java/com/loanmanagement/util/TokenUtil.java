package com.loanmanagement.util;

import java.util.UUID;

public class TokenUtil {
	 public static String generateToken(String username) {

	        String raw =
	                username + ":" +
	                UUID.randomUUID() + ":" +
	                System.currentTimeMillis();

	        return CryptoUtil.encrypt(raw);
	    }

}
