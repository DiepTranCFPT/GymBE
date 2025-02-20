package com.gymsystem.cyber.service;


import com.gymsystem.cyber.entity.User;
import com.gymsystem.cyber.exception.AuthException;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@Service
@PropertySource("classpath:application.properties")
public class TokenService {

    @Value("${jwt.secret}")
    private String SECRET_KEY;
    private final long EXPIRATION = 1 * 24 * 60 * 60 * 1000;


    String generateToken(User user) {
        try {
            List<String> roles = List.of("ROLE_" + user.getRole().name());

            JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);
            JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                    .subject(user.getEmail())
                    .issueTime(new Date())
                    .expirationTime(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000))
                    .claim("roles", roles)
                    .build();

            Payload payload = new Payload(jwtClaimsSet.toJSONObject());

            JWSObject jwsObject = new JWSObject(header, payload);

            jwsObject.sign(new MACSigner(SECRET_KEY.getBytes()));
            return jwsObject.serialize();

        } catch (JOSEException e) {
            throw new RuntimeException("Error generating token", e);
        }
    }


    public JWTClaimsSet verifyToken(String token) throws ParseException, JOSEException {

        SignedJWT signedJWT = SignedJWT.parse(token);
        JWSVerifier verifier = new MACVerifier(Base64.getDecoder().decode(SECRET_KEY));

        if (signedJWT.verify(verifier)) {
            return signedJWT.getJWTClaimsSet();
        } else {
            throw new AuthException("Token invalid");
        }

    }

    // Validate the token (check expiration and signature)
    public boolean validateToken(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            JWSVerifier verifier = new MACVerifier(Base64.getDecoder().decode(SECRET_KEY));
            if (signedJWT.verify(verifier) && signedJWT.getJWTClaimsSet().getExpirationTime().after(new Date())) {
                return true; // Token is valid
            } else {
                return false;
            }
        } catch (ParseException | JOSEException e) {
            return false;
        }
    }
}
