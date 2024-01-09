package com.lakshay.blogbackend.utilities;

import com.lakshay.blogbackend.exception.custom_exception.authentication_exception.AuthenticationException;
import com.lakshay.blogbackend.exception.custom_exception.authentication_exception.AuthenticationExceptionCodes;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.crypto.SecretKey;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;

public class CookieUtils {
    private static final SecretKey SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS512);
    private static final String COOKIE_NAME = "auth";

    private CookieUtils() {
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


    public static void verifyRequest(String username, HttpServletRequest request) {
        Cookie[] cookies = Optional.ofNullable(request.getCookies()).orElse(new Cookie[0]);

        Optional<Cookie> tokenCookie = Arrays.stream(cookies)
                .filter(cookie -> COOKIE_NAME.equals(cookie.getName()))
                .findFirst();

        if (tokenCookie.isEmpty()) {
            throw new AuthenticationException(AuthenticationExceptionCodes.INVALID_TOKEN.getCode(), "Invalid or no cookies.");
        }

        String token = tokenCookie.get().getValue();
        try {
            Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY)
                    .build()
                    .parseClaimsJws(token);

            validateRequest(request, claims, username);
        } catch (JwtException e) {
            throw new AuthenticationException(AuthenticationExceptionCodes.UNAUTHORIZED.getCode(), e.getMessage());
        }
    }

    private static void validateRequest(HttpServletRequest request, Jws<Claims> claims, String username) {
        String requestIP = request.getRemoteAddr();
        String userAgent = request.getHeader("User-Agent");

        String tokenIP = claims.getBody().get("ip", String.class);
        String tokenUserAgent = claims.getBody().get("userAgent", String.class);

        if (!requestIP.equals(tokenIP)) {
            throw new AuthenticationException(AuthenticationExceptionCodes.UNAUTHORIZED.getCode(), "Request IP is mismatched.");
        }
        if (!claims.getBody().getSubject().equals(username)) {
            throw new AuthenticationException(AuthenticationExceptionCodes.UNAUTHORIZED.getCode(), "Username is mismatched.");
        }
        if (!userAgent.equals(tokenUserAgent)) {
            throw new AuthenticationException(AuthenticationExceptionCodes.UNAUTHORIZED.getCode(), "User agent is mismatched.");
        }
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
