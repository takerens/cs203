package csd.grp3.jwt;

import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    private String secretKey = "tempsecretkey";

    public JwtService() {

        try {
            KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256");
            SecretKey sk = keyGen.generateKey();
            secretKey = Base64.getEncoder().encodeToString(sk.getEncoded());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    /*
     * Generate a JWT token for the given user details
     * @param userDetails the user details
     * @return the generated token
     */
    public String generateToken(UserDetails userDetails) {

        final int ONE_HOUR_IN_MILLISECONDS = 1000*60*60;

        Map<String, Object> claims = new HashMap<>();
        claims.put("authorities", userDetails.getAuthorities().stream()
                .map(grantedAuthority -> grantedAuthority.getAuthority())
                .collect(Collectors.toList()));

        return Jwts.builder()
            .claims()
            .add(claims)
            .subject(userDetails.getUsername())
            .issuedAt(new Date(System.currentTimeMillis()))
            .expiration(new Date(System.currentTimeMillis()*ONE_HOUR_IN_MILLISECONDS))
            .and()
            .signWith(getKey())
            .compact();
    }

    /*
     * Get the secret key
     */
    private SecretKey getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /*
     * Extract the username from the given token
     * @param token 
     * @return the username
     */
    public String extractUserName(String token) {
        // extract the username from jwt token
        return extractClaim(token, Claims::getSubject);
    }

    /*
     * Extract the claim from the given token
     * @param token
     * @param claimResolver
     * @return the claim
     */
    private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    /*
     * Extract all claims from the given token
     * @param token
     * @return the claims
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /*
     * Validate the given token for the given user details
     * @param token
     * @param userDetails
     * @return true if the token is valid, false otherwise
     */
    public boolean validateToken(String token, UserDetails userDetails) {
        final String userName = extractUserName(token);
        return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    /*
     * Check if the token is expired
     * @param token
     * @return true if the token is expired, false otherwise
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /*
     * Extract the expiration date from the given token
     * @param token
     * @return the expiration date
     */
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /*
     * Extract the authorities from the given token
     * @param token
     * @return the authorities
     */
    public List<String> extractAuthorities(String token) {
        Claims claims = extractAllClaims(token);
        List<?> rawList = claims.get("authorities", List.class);
        if (rawList == null) {
            return null;
        }
        return rawList.stream()
                      .filter(item -> item instanceof String)
                      .map(item -> (String) item)
                      .collect(Collectors.toList());
    }
}