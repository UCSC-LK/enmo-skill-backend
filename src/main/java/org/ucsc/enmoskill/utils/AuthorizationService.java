package org.ucsc.enmoskill.utils;

import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.JWTParser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;

import java.text.ParseException;

import static java.lang.System.out;
import static org.ucsc.enmoskill.utils.TokenService.getSigningKey;

public class AuthorizationService {

    public AuthorizationResults authorize(String jwtToken, String userLevelID) {
        if (jwtToken != null && jwtToken.startsWith("Bearer ")) {
            jwtToken = jwtToken.substring(7); // Remove "Bearer " prefix

            try {
                out.println("split -- : " + jwtToken);
//                JWT parsedJWT = JWTParser.parse(jwtToken);
//                JWTClaimsSet claimsSet = parsedJWT.getJWTClaimsSet();
//
//                String jwtUserLevelID = claimsSet.getStringClaim("userLevelID");
//                String userID = claimsSet.getStringClaim("userID");

                Claims claims = Jwts.parser().setSigningKey(getSigningKey()).parseClaimsJws(jwtToken).getBody();
                String userId = claims.getSubject();
                String jwtUserLevelID = (String) claims.get("role");

                out.println("jwtUserLevelID JWT -- : " + jwtUserLevelID);
                out.println("userID JWT -- : " + userId);

                if (userLevelID.equals(jwtUserLevelID)) {
                    return new AuthorizationResults(userId, jwtUserLevelID);
                }
            } catch (JwtException e) {
                // Handle exceptions accordingly
            }
        }
        return null;
    }
}
