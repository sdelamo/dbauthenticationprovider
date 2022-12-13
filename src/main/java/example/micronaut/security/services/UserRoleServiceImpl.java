package example.micronaut.security.services;

import io.micronaut.core.annotation.NonNull;
import example.micronaut.security.entities.RoleDataService;
import example.micronaut.security.entities.RoleEntity;
import example.micronaut.security.entities.UserDataService;
import example.micronaut.security.entities.UserEntity;
import example.micronaut.security.entities.UserRoleDataService;
import example.micronaut.security.entities.UserRoleEntity;
import example.micronaut.security.entities.UserRoleId;
import jakarta.inject.Singleton;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Optional;

@Singleton
public class UserRoleServiceImpl implements UserRoleService {
    private final UserRoleDataService userRoleDataService;
    private final RoleDataService roleDataService;
    private final UserDataService userDataService;

    public UserRoleServiceImpl(UserRoleDataService userRoleDataService,
                               RoleDataService roleDataService,
                               UserDataService userDataService) {
        this.userRoleDataService = userRoleDataService;
        this.roleDataService = roleDataService;
        this.userDataService = userDataService;
    }
    @Override
    @NonNull
    public List<String> findAllAuthoritiesByEmail(@NonNull @NotBlank @Email String email) {
        return userRoleDataService.findAllAuthoritiesByEmail(email);
    }

    @Override
    public void grant(@NonNull @NotBlank String authority, @NonNull String userId) {

        Optional<RoleEntity> roleOptional = roleDataService.findByAuthority(authority);
        if (roleOptional.isPresent()) {
            RoleEntity role = roleOptional.get();
            Optional<UserEntity> userOptional = userDataService.findById(userId);
            if (userOptional.isPresent()) {
                UserEntity userEntity = userOptional.get();
                userRoleDataService.save(new UserRoleEntity(new UserRoleId(userEntity, role)));
            }
        }
    }
}
