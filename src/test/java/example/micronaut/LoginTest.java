package example.micronaut;

import example.micronaut.controllers.Message;
import example.micronaut.security.Roles;
import example.micronaut.security.entities.UserRoleDataService;
import io.micronaut.context.annotation.Property;
import io.micronaut.core.util.CollectionUtils;
import io.micronaut.core.util.StringUtils;
import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.client.BlockingHttpClient;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.http.uri.UriBuilder;
import io.micronaut.security.token.jwt.cookie.AccessTokenCookieConfiguration;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Property(name = "micronaut.http.client.follow-redirects",  value = StringUtils.FALSE)
@MicronautTest
class LoginTest {

    @Inject
    BootstrapService bootstrapService;

    @Inject
    UserRoleDataService userRoleDataService;

    @Inject
    UserCrudService userCrudService;

    @Inject
    RoleCrudService roleCrudService;

    @Inject
    @Client("/")
    HttpClient httpClient;

    @Inject
    AccessTokenCookieConfiguration accessTokenCookieConfiguration;

    @Test
    void loginIsPossible() {

        String username = "johnsnow@micronaut.example";
        String password = "aegon";
        bootstrapService.save(username, password, Roles.ROLE_USER);


        BlockingHttpClient client = httpClient.toBlocking();

        HttpResponse<?> response = client.exchange(HttpRequest.GET("/").accept(MediaType.TEXT_HTML));
        assertEquals(HttpStatus.SEE_OTHER, response.getStatus());
        assertEquals("/auth", response.getHeaders().get(HttpHeaders.LOCATION));
        assertFalse(response.getCookie(accessTokenCookieConfiguration.getCookieName()).isPresent());

        HttpRequest<?> request = HttpRequest.POST("/login", CollectionUtils.mapOf("username", username, "password", password));

        response = client.exchange(request, String.class);
        assertEquals(HttpStatus.SEE_OTHER, response.getStatus());
        assertEquals("/", response.getHeaders().get(HttpHeaders.LOCATION));
        assertTrue(response.getCookie(accessTokenCookieConfiguration.getCookieName()).isPresent());

        String jwt = response.getCookie(accessTokenCookieConfiguration.getCookieName()).get().getValue();
        URI uri = UriBuilder.of("/api").path("hello").build();

        Executable e = () -> client.retrieve(HttpRequest.GET(uri), Message.class);
        HttpClientResponseException thrown = assertThrows(HttpClientResponseException.class, e);
        assertEquals(HttpStatus.UNAUTHORIZED, thrown.getStatus());

        Message message = client.retrieve(HttpRequest.GET(uri).bearerAuth(jwt), Message.class);
        assertEquals("Hello World", message.getMessage());

        userRoleDataService.deleteAll();
        roleCrudService.deleteAll();
        userCrudService.deleteAll();
    }


}
