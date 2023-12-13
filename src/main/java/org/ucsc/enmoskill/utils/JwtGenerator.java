package org.ucsc.enmoskill.utils;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.util.Base64URL;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.ucsc.enmoskill.model.Login;

import java.util.Date;
import java.util.UUID;

public class JwtGenerator {

    public  String generateJWTToken(Login loginDB, String secretKey) {
        try {
            JWSSigner signer = new MACSigner(secretKey);

            JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                    .subject(loginDB.getEmail())
                    .claim("userLevelID", loginDB.getUserLevelID())
                    .claim("userID", loginDB.getId())
                    .expirationTime(new Date(System.currentTimeMillis() + 3600000)) // Token expires in 1 hour
                    .jwtID(UUID.randomUUID().toString())
                    .build();

            SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claimsSet);

            signedJWT.sign(signer);

            return signedJWT.serialize();
        } catch (Exception e) {
            e.printStackTrace();
            return null; // Return null to indicate failure
        }
    }
}
