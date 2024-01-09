package com.lakshay.blogbackend.utilities;

import at.favre.lib.crypto.bcrypt.BCrypt;
import lombok.extern.log4j.Log4j2;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Log4j2
public class Utilities {
    private static final String EMAIL_REGEX = "^[\\w+&*-]+(?:\\.[\\w+&*-]+)*@(?:[\\w-]+\\.)+[a-zA-Z]{2,63}$";


    private Utilities() {
    }

    public static boolean isValidEmail(String email) {
        if (email == null) {
            return true; // Consider null as valid (optional email)
        }

        Pattern pattern = Pattern.compile(EMAIL_REGEX);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static String hashPassword(String plainTextPassword) {
        return BCrypt.withDefaults().hashToString(12, plainTextPassword.toCharArray());
    }

    public static boolean verifyPassword(String plainTextPassword, String hashedPassword) {
        BCrypt.Result result = BCrypt.verifyer().verify(plainTextPassword.toCharArray(), hashedPassword);
        return result.verified;
    }
}
