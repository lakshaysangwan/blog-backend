package com.lakshay.blogbackend.utilities;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.lakshay.blogbackend.error.custom_error.token_validation.ValidationException;
import com.lakshay.blogbackend.error.custom_error.token_validation.enums.ValidationExceptionCodes;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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

    public static String generateToken(String username, HttpServletRequest request) {
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        Date exp = new Date(nowMillis + 3600000); // 1 hour expiry

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(exp)
                .claim("ip", request.getRemoteAddr())
                .claim("userAgent", request.getHeader("User-Agent"))
                .signWith(SECRET_KEY)
                .compact();
    }

    public static String verifyRequest(HttpServletRequest request) {
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

                        String requestIP = request.getRemoteAddr();
                        String userAgent = request.getHeader("User-Agent");

                        String tokenIP = claims.getBody().get("ip", String.class);
                        String tokenUserAgent = claims.getBody().get("userAgent", String.class);

                        if (requestIP.equals(tokenIP) && userAgent.equals(tokenUserAgent))
                            throw new ValidationException(ValidationExceptionCodes.UNAUTHORIZED.getCode(), "You cheating, cheater.");

                        return claims.getBody().getSubject();
                    } catch (Exception e) {
                        throw new ValidationException(ValidationExceptionCodes.UNAUTHORIZED.getCode(), "Unauthorized request.");
                    }
                }
            }
        }
        throw new ValidationException(ValidationExceptionCodes.INVALID_TOKEN.getCode(), "Invalid or no cookies."); // or throw an exception, depending on how you want to handle missing/invalid tokens
    }

    public static void refreshUserCookie(String userName, HttpServletRequest request, HttpServletResponse response) {
        // Step 1: Invalidate old cookie
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (COOKIE_NAME.equals(cookie.getName())) {
                    cookie.setValue("");
                    cookie.setPath("/");
                    cookie.setMaxAge(0); // Invalidate the cookie
                    response.addCookie(cookie);
                    break;
                }
            }
        }

        // Step 2: Create and set new cookie with one-hour expiry
        String newToken = generateToken(userName, request);
        Cookie newCookie = new Cookie(COOKIE_NAME, newToken);
        newCookie.setHttpOnly(true);
        newCookie.setSecure(true); // Set to false if not using HTTPS
        newCookie.setMaxAge(3600); // One hour in seconds
        newCookie.setPath("/");
        response.addCookie(newCookie);
    }


}
