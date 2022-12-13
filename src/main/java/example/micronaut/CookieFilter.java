package example.micronaut;

import com.nimbusds.jwt.JWTParser;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.MutableHttpResponse;
import io.micronaut.http.annotation.Filter;
import io.micronaut.http.cookie.Cookie;
import io.micronaut.http.filter.HttpServerFilter;
import io.micronaut.http.filter.ServerFilterChain;
import io.micronaut.http.filter.ServerFilterPhase;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.authentication.ServerAuthentication;
import io.micronaut.security.errors.OauthErrorResponseException;
import io.micronaut.security.errors.ObtainingAuthorizationErrorCode;
import io.micronaut.security.filters.AuthenticationFetcher;
import io.micronaut.security.filters.SecurityFilter;
import io.micronaut.security.token.jwt.cookie.AccessTokenCookieConfiguration;
import io.micronaut.security.token.jwt.generator.AccessRefreshTokenGenerator;
import io.micronaut.security.token.jwt.generator.AccessTokenConfiguration;
import io.micronaut.security.token.jwt.render.AccessRefreshToken;
import io.micronaut.security.token.validator.TokenValidator;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

import java.text.ParseException;
import java.time.Duration;
import java.time.temporal.TemporalAmount;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static io.micronaut.http.annotation.Filter.MATCH_ALL_PATTERN;
import static io.micronaut.security.token.jwt.generator.claims.JwtClaims.ALL_CLAIMS;

@Filter(MATCH_ALL_PATTERN)
public class CookieFilter implements HttpServerFilter {
    private static final Logger LOG = LoggerFactory.getLogger(CookieFilter.class);

    protected final AccessRefreshTokenGenerator accessRefreshTokenGenerator;
    protected final AccessTokenCookieConfiguration accessTokenCookieConfiguration;
    protected final AccessTokenConfiguration accessTokenConfiguration;

    protected final TokenValidator tokenValidator;

    @Override
    public int getOrder() {
        return ServerFilterPhase.SECURITY.order() + 10;
    }

    public CookieFilter(AccessRefreshTokenGenerator accessRefreshTokenGenerator,
                        AccessTokenCookieConfiguration accessTokenCookieConfiguration,
                        AccessTokenConfiguration accessTokenConfiguration,
                        TokenValidator tokenValidator) {
        this.accessRefreshTokenGenerator = accessRefreshTokenGenerator;
        this.accessTokenCookieConfiguration = accessTokenCookieConfiguration;
        this.accessTokenConfiguration = accessTokenConfiguration;
        this.tokenValidator = tokenValidator;
    }

    @Override
    public Publisher<MutableHttpResponse<?>> doFilter(HttpRequest<?> request, ServerFilterChain chain) {
        if (request.getPath().startsWith("/api")) {
            return chain.proceed(request);
        }
        return Mono.from(chain.proceed(request))
                .doOnNext(response -> {
                    if (response.getStatus().getCode() < 300) {
                        Optional<Authentication> authenticationOptional = request.getAttribute(SecurityFilter.AUTHENTICATION, Authentication.class);
                        if (authenticationOptional.isPresent()) {
                            Authentication authentication = authenticationOptional.get();
                            LOG.info("adding cookies");
                            applyCookies(response, getCookies(authenticationWithoutJwtClaims(authentication), request));
                        }
                    }
                });
    }

    private Authentication authenticationWithoutJwtClaims(Authentication authentication) {
        Map<String, Object> attrs = new HashMap<>();
        for (String attrName : authentication.getAttributes().keySet()) {
            if (!ALL_CLAIMS.contains(attrName)) {
                attrs.put(attrName, authentication.getAttributes().get(attrName));
            }
        }
        return new ServerAuthentication(authentication.getName(), authentication.getRoles(), attrs);
    }

    protected MutableHttpResponse<?> applyCookies(MutableHttpResponse<?> response, List<Cookie> cookies) {
        for (Cookie cookie : cookies) {
            response = response.cookie(cookie);
        }
        return response;
    }

    public List<Cookie> getCookies(Authentication authentication, HttpRequest<?> request) {
        LOG.info("Generating JWT");
        AccessRefreshToken accessRefreshToken = accessRefreshTokenGenerator.generate(authentication)
                .orElseThrow(() -> new OauthErrorResponseException(ObtainingAuthorizationErrorCode.SERVER_ERROR, "Cannot obtain an access token", null));
        return getCookies(accessRefreshToken, request);
    }

    protected List<Cookie> getCookies(AccessRefreshToken accessRefreshToken, HttpRequest<?> request) {
        List<Cookie> cookies = new ArrayList<>(1);

        try {
            String jwtId = JWTParser.parse(accessRefreshToken.getAccessToken()).getJWTClaimsSet().getJWTID();
            LOG.info("new JWTID {}", jwtId);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        Cookie jwtCookie = Cookie.of(accessTokenCookieConfiguration.getCookieName(), accessRefreshToken.getAccessToken());
        jwtCookie.configure(accessTokenCookieConfiguration, request.isSecure());
        TemporalAmount maxAge = accessTokenCookieConfiguration.getCookieMaxAge().orElseGet(() -> Duration.ofSeconds(accessTokenConfiguration.getExpiration()));
        jwtCookie.maxAge(maxAge);
        cookies.add(jwtCookie);
        return cookies;
    }
}
