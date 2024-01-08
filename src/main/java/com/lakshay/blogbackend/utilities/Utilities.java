package com.lakshay.blogbackend.utilities;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.lakshay.blogbackend.entity.User;
import com.lakshay.blogbackend.error.custom_error.token_validation.ValidationException;
import com.lakshay.blogbackend.error.custom_error.token_validation.enums.ValidationExceptionCodes;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Log4j2
public class Utilities {
    private static final String EMAIL_REGEX = "^[\\w+&*-]+(?:\\.[\\w+&*-]+)*@(?:[\\w-]+\\.)+[a-zA-Z]{2,63}$";
    private static final SecretKey SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS512);
    private static final String COOKIE_NAME = "auth";

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

    public static String generateToken(User user) {
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        long expMillis = nowMillis + 86400000; // Token valid for 1 day
        Date exp = new Date(expMillis);

        return Jwts.builder()
                .setSubject(user.getUsername())
                .setIssuedAt(now)
                .setExpiration(exp)
                .signWith(SECRET_KEY) // Updated signing method
                .compact();
    }

    public static String getUserFromRequest(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (COOKIE_NAME.equals(cookie.getName())) {
                    String token = cookie.getValue();
                    try {
                        Jws<Claims> claims = Jwts.parserBuilder()
                                .setSigningKey(SECRET_KEY)
                                .build()
                                .parseClaimsJws(token);

                        return claims.getBody().getSubject();
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw new ValidationException(ValidationExceptionCodes.UNAUTHORIZED.getCode(), "Unauthorized request.");
                    }
                }
            }
        }
        throw new ValidationException(ValidationExceptionCodes.INVALID_TOKEN.getCode(), "Invalid or no cookies."); // or throw an exception, depending on how you want to handle missing/invalid tokens
    }
}
