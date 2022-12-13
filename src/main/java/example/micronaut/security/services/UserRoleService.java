package example.micronaut.security.services;

import io.micronaut.core.annotation.NonNull;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.List;

public interface UserRoleService {
    @NonNull
    List<String> findAllAuthoritiesByEmail(@NonNull @NotBlank @Email String email);

    void grant(@NonNull @NotBlank String authority, @NonNull String userId);
}
