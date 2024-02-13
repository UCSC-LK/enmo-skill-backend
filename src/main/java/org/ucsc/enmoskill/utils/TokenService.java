package org.ucsc.enmoskill.utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.Date;
import java.util.Map;

public class TokenService {
    private static final Key SIGNING_KEY,SIGNING_KEY2 ;
    private final long EXPIRATION_TIME_HOUR = 3600000;
    private final long EXPIRATION_TIME_DAY = 86400000;
    static {
            String signingKeyString = "TFSysTS6s^8$Sgsr#havtFGse5ajFtaeCAuhAUTAWdc%f";
            SIGNING_KEY = new SecretKeySpec(signingKeyString.getBytes(), SignatureAlgorithm.HS256.getJcaName());
    }

    static {
        String signingKeyString2 = "TFSysTS6s^8$Hagfs425h$$&GdsuhAUTAWdc%f";
        SIGNING_KEY2 = new SecretKeySpec(signingKeyString2.getBytes(), SignatureAlgorithm.HS256.getJcaName());
    }


    public  String generateToken(String userId, String role) {
        return  Jwts.builder()
                .setSubject(userId)
                .claim("role", role)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME_HOUR))
                .signWith(getSigningKey())
                .compact();
    }
    public  String generateDualToken(String userId, String role) {
        return  Jwts.builder()
                .setSubject(userId)
                .claim("role", role)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME_DAY))
                .signWith(getSigningKey())
                .compact();
    }


    public  boolean isTokenValid(String token) {
        try {
            Jwts.parser().setSigningKey(getSigningKey()).parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            e.printStackTrace();
            return false;
        }
    }

    public  String getTokenFromHeader(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }
        return null;
    }


    public  TokenInfo getTokenInfo(String token) {
        Claims claims = Jwts.parser().setSigningKey(getSigningKey()).parseClaimsJws(token).getBody();
        String userId = claims.getSubject();
        String role = (String) claims.get("role");
        return new TokenInfo(userId, role);
    }


    private static Key getSigningKey() {
        return SIGNING_KEY;
    }


    public  class TokenInfo {
        private final String userId;
        private final String role;

        public TokenInfo(String userId, String role) {
            this.userId = userId;
            this.role = role;
        }


        public boolean isClient() {
            if(role.equals("1")){
                return true;
            }else {
                return false;
            }

        }
        public boolean isDesigner() {
            if(role.equals("2")){
                return true;
            }else {
                return false;
            }

        }
        public boolean isAgent() {
            if(role.equals("4")){
                return true;
            }else {
                return false;
            }

        }
        public boolean isAdmin() {
            if(role.equals("3")){
                return true;
            }else {
                return false;
            }

        }

        public String getUserId() {
            return userId;
        }

        public String getRole() {
            return role;
        }
    }
}
