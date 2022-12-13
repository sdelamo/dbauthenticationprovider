package example.micronaut.controllers;

import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.JWTParser;
import example.micronaut.security.Roles;
import io.micronaut.core.util.CollectionUtils;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.CookieValue;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Produces;
import io.micronaut.security.token.jwt.cookie.AccessTokenCookieConfiguration;
import io.micronaut.security.token.jwt.cookie.JwtCookieConfigurationProperties;
import io.micronaut.views.View;
import jakarta.inject.Singleton;
import jakarta.annotation.security.RolesAllowed;

import java.text.ParseException;
import java.util.Collections;
import java.util.Map;

@Controller
public class HomeController {
    @RolesAllowed(Roles.ROLE_USER)
    @Get
    @Produces(MediaType.TEXT_HTML)
    @View("index.html")
    Map<String, Object> index(@CookieValue(JwtCookieConfigurationProperties.DEFAULT_COOKIENAME) String jwt) {
        try {
            JWTClaimsSet claims = JWTParser.parse(jwt).getJWTClaimsSet();
            String jwtId = claims.getJWTID();

            return CollectionUtils.mapOf("jwt", jwt, "jwtId", jwtId, "exp", claims.getExpirationTime().getTime());
        } catch (ParseException e) {
            throw new IllegalStateException("could not parse JWT");
        }


    }
}
