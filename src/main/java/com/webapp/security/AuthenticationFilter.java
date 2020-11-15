package com.webapp.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.webapp.domain.UserAccountSecurity;
import com.webapp.json.TokenMessage;
import com.webapp.service.UserAccountService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import com.webapp.domain.UserAccount;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    public AuthenticationFilter(AuthenticationManager authenticationManager){
        this.authenticationManager = authenticationManager;
        setFilterProcessesUrl("/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response)
            throws AuthenticationException
    {
        try (var req = request.getInputStream()){
            UserAccount creds = new ObjectMapper().readValue(req,
                    UserAccount.class);
            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(creds.getEmail(), creds.getPassword(),new ArrayList<>()));
        }
        catch(IOException e) {
            throw new RuntimeException("Could not read request " + e);
        }
    }

    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain filterChain,
                                            Authentication authentication) {
        String token = Jwts.builder()
                .setSubject(((UserAccountSecurity) authentication.getPrincipal()).getUsername())
                .setExpiration(new Date(System.currentTimeMillis() + 864_000_000))
                .signWith(SignatureAlgorithm.HS512, "SecretKeyToGenJWTs".getBytes())
                .compact();
        response.addHeader("Authorization","Bearer " + token);

        try (var writer = response.getWriter()){
            var id = ((UserAccountSecurity) authentication.getPrincipal()).getId();
            var objectMapper = new ObjectMapper();
            writer.write(objectMapper.writeValueAsString(new TokenMessage(id,
                    "Bearer " + token)));
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

