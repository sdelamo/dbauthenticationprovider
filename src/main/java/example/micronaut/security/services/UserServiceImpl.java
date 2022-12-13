package example.micronaut.security.services;

import io.micronaut.core.annotation.NonNull;
import example.micronaut.security.entities.UserDataService;
import example.micronaut.security.entities.UserEntity;
import example.micronaut.services.IdGenerator;
import io.micronaut.security.authentication.UsernamePasswordCredentials;
import jakarta.inject.Singleton;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.Optional;

@Singleton
public class UserServiceImpl implements UserService {
    private final IdGenerator idGenerator;
    private final PasswordEncoderService passwordEncoderService;
    private final UserDataService userDataService;

    public UserServiceImpl(IdGenerator idGenerator,
                           PasswordEncoderService passwordEncoderService,
                           UserDataService userDataService) {
        this.idGenerator = idGenerator;
        this.passwordEncoderService = passwordEncoderService;
        this.userDataService = userDataService;
    }

    @Override
    @NonNull
    public Optional<String> save(@NonNull @NotBlank @Email String email,
                                    @NonNull @NotBlank String rawPassword) {
        return idGenerator.generate().map(id -> {
            String password = passwordEncoderService.encode(rawPassword);
            UserEntity userEntity = new UserEntity(id, email, password);
            userDataService.save(userEntity);
            return id;
        });
    }

    @NonNull
    public Boolean existsByEmail(@NonNull @NotBlank @Email String email) {
        return userDataService.countByEmail(email) > 0;
    }

    @Override
    @NonNull
    public Optional<UsernamePasswordCredentials> findByEmail(@NonNull @NotBlank @Email String email) {
        return userDataService.findByEmail(email)
                .map(userEntity -> new UsernamePasswordCredentials(userEntity.getEmail(),
                        userEntity.getPassword()));
    }
}
