package example.micronaut;

import example.micronaut.security.services.RoleService;
import example.micronaut.security.services.UserRoleService;
import example.micronaut.security.services.UserService;
import jakarta.inject.Singleton;

import java.util.Optional;

@Singleton
public class BootstrapService {

    private final RoleService roleService;
    private final UserService userService;
    private final UserRoleService userRoleService;

    public BootstrapService(RoleService roleService,
                     UserService userService,
                     UserRoleService userRoleService) {
        this.roleService = roleService;
        this.userService = userService;
        this.userRoleService = userRoleService;
    }

    public void save(String username, String password, String... authorities) {
        if (!userService.existsByEmail(username)) {
            Optional<String> userIdOptional = userService.save(username, password);
            userIdOptional.ifPresent(userId -> {
                for (String authority : authorities) {
                    if (!roleService.existsByAuthority(authority)) {
                        Optional<String> roleIdOptional = roleService.save(authority);
                        roleIdOptional.ifPresent(roleId -> userRoleService.grant(authority, userId));
                    } else {
                        userRoleService.grant(authority, userId);
                    }
                }
            });
        }
    }
}
