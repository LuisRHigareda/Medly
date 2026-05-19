package com.medical.userservice.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import java.io.UnsupportedEncodingException;
import java.util.Date;

/**
 *
 * @author Leonardo Flores Leyva
 */
public class JWTUtil {

    private static final String SECRET = "skdhfasdhfkhsdfjhsdkjhfjsdhfk";

    public static String generateToken(
            Integer userId,
            String email,
            String userRole
    ) throws
            IllegalArgumentException,
            UnsupportedEncodingException {
        return JWT.create()
                .withSubject("User Details")
                .withClaim("id", userId)
                .withClaim("email", email)
                .withClaim("role", userRole)
                .withIssuedAt(new Date())
                .withIssuer("Medly")
                .sign(Algorithm.HMAC256(SECRET));
    }

    public static DecodedJWT validateToken(String token) throws
            IllegalArgumentException,
            UnsupportedEncodingException {
        return JWT.require(Algorithm.HMAC256(SECRET))
                .withSubject("User Details")
                .withIssuer("Medly")
                .build()
                .verify(token);
    }
}