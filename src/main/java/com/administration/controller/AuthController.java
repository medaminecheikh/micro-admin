package com.administration.controller;

import com.administration.dto.UtilisateurResponseDTO;
import com.administration.entity.AuthResponse;
import com.administration.entity.Utilisateur;
import com.administration.security.Jwt.JwtTokenUtil;
import com.administration.security.Jwt.JwtVariables;
import com.administration.service.IUtilisateurService;
import com.administration.service.impl.UserDetailsServiceImpl;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/MICROADMIN/auth")
@Slf4j
@AllArgsConstructor
@Api(tags = "Auth Controller")
public class AuthController {

    private final AuthenticationManager authenticationManager;


    private final UserDetailsServiceImpl userDetailsService;
    private IUtilisateurService utilisateurService;


    @PostMapping("/authenticate")
    public ResponseEntity<?> login(@RequestParam String username, @RequestParam String password) {
        log.info("Received authentication request for username: {}", username);
        try {
            log.info("Attempting authentication for user: " + username);
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            Algorithm algorithm = Algorithm.HMAC256(JwtVariables.SECRET);

            // Perform the expiration check after authentication
            Utilisateur utilisateur = utilisateurService.getUtilisateurbyLogin(username);
            Date today = new Date();
            Date utilisateurExpirationDate = utilisateur.getDate_EXPIRED();
            if (utilisateurExpirationDate == null || utilisateurExpirationDate.after(today)) {
                // Generate tokens and create AuthResponse
                String accessToken = JWT.create()
                        .withSubject(userDetails.getUsername())
                        .withExpiresAt(new Date(System.currentTimeMillis() + JwtVariables.EXPIRE_ACCESS))
                        .withClaim("roles", userDetails.getAuthorities().stream().map(
                                GrantedAuthority::getAuthority).collect(Collectors.toList()))
                        .sign(algorithm);

                String refreshToken = JWT.create()
                        .withSubject(userDetails.getUsername())
                        .withExpiresAt(new Date(System.currentTimeMillis() + JwtVariables.EXPIRE_REFRESH))
                        .withClaim("roles", userDetails.getAuthorities().stream().map(
                                GrantedAuthority::getAuthority).collect(Collectors.toList()))
                        .sign(algorithm);
                UtilisateurResponseDTO utilisateurcaise = utilisateurService.getbyLogin(userDetails.getUsername());
                Integer caisse = null;
                if (utilisateurcaise.getCaisse() != null) {
                    caisse = utilisateurcaise.getCaisse().getNumCaise();
                }
                AuthResponse authResponse = new AuthResponse(userDetails.getUsername(),
                        userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()),
                        accessToken, refreshToken,caisse);
                return ResponseEntity.ok(authResponse);
            } else {
                // Handle the scenario when utilisateur's date has expired
                return ResponseEntity.status(HttpStatus.INSUFFICIENT_SPACE_ON_RESOURCE).build();
            }
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping("/validate")
    public ResponseEntity<String> validateToken(@RequestParam String token) {
        try {
            // Do logic for token validation
            Algorithm algorithm = Algorithm.HMAC256(JwtVariables.SECRET);
            JWTVerifier jwtVerifier = JWT.require(algorithm).build();
            DecodedJWT decodedJWT = jwtVerifier.verify(token);

            // Check the expiration date of the token
            Date expirationDate = decodedJWT.getExpiresAt();
            Date currentDate = new Date();

            if (expirationDate != null && expirationDate.after(currentDate)) {
                // Token is valid
                return ResponseEntity.ok("Token is valid");
            } else {
                // Token is expired
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token has expired");
            }
        } catch (JWTVerificationException e) {
            // If there's any exception during token validation, handle it here
            // You can log the error for debugging purposes
            log.error("Error validating token: {}", e.getMessage());

            // Return an unauthorized response for an invalid token
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }
    }

}

