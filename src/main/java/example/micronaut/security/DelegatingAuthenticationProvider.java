package example.micronaut.security;

import example.micronaut.security.services.PasswordEncoderService;
import example.micronaut.security.services.RoleService;
import example.micronaut.security.services.UserRoleService;
import example.micronaut.security.services.UserService;
import io.micronaut.http.HttpRequest;
import io.micronaut.security.authentication.AuthenticationException;
import io.micronaut.security.authentication.AuthenticationFailed;
import io.micronaut.security.authentication.AuthenticationFailureReason;
import io.micronaut.security.authentication.AuthenticationProvider;
import io.micronaut.security.authentication.AuthenticationRequest;
import io.micronaut.security.authentication.AuthenticationResponse;
import io.micronaut.security.authentication.UsernamePasswordCredentials;
import jakarta.inject.Singleton;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;

import java.util.Optional;

@Singleton
public class DelegatingAuthenticationProvider implements AuthenticationProvider {
    private final UserService userService;
    private final UserRoleService userRoleService;

    private final PasswordEncoderService passwordEncoderService;

    public DelegatingAuthenticationProvider(UserService userService,
                                            UserRoleService userRoleService,
                                            PasswordEncoderService passwordEncoderService) {
        this.userService = userService;
        this.userRoleService = userRoleService;
        this.passwordEncoderService = passwordEncoderService;
    }

    @Override
    public Publisher<AuthenticationResponse> authenticate(HttpRequest<?> httpRequest,
                                                          AuthenticationRequest<?, ?> authenticationRequest) {
        return Flux.create(emitter -> {
            Optional<UsernamePasswordCredentials> userOptional = userService.findByEmail(authenticationRequest.getIdentity().toString());
            if (userOptional.isPresent()) {
                UsernamePasswordCredentials user = userOptional.get();
                if (passwordEncoderService.matches(authenticationRequest.getSecret().toString(), user.getPassword())) {
                    AuthenticationResponse success = AuthenticationResponse.success(user.getUsername(), userRoleService.findAllAuthoritiesByEmail(user.getUsername()));
                    emitter.next(success);
                    emitter.complete();
                } else {
                    emitter.error(new AuthenticationException(new AuthenticationFailed(AuthenticationFailureReason.CREDENTIALS_DO_NOT_MATCH)));
                }
            } else {
                emitter.error(new AuthenticationException(new AuthenticationFailed(AuthenticationFailureReason.USER_NOT_FOUND)));
            }
        });
    }
}
