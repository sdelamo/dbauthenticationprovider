package example.micronaut.dev;

import example.micronaut.security.Roles;
import example.micronaut.security.services.RoleService;
import example.micronaut.security.services.UserRoleService;
import example.micronaut.security.services.UserService;
import io.micronaut.context.annotation.Requires;
import io.micronaut.context.env.Environment;
import io.micronaut.context.event.ApplicationEventListener;
import io.micronaut.context.event.StartupEvent;
import jakarta.inject.Singleton;

import java.util.Optional;

@Requires(Environment.DEVELOPMENT)
@Singleton
public class Bootstrap implements ApplicationEventListener<StartupEvent> {

    private final RoleService roleService;
    private final UserService userService;
    private final UserRoleService userRoleService;

    public Bootstrap(RoleService roleService,
                     UserService userService,
                     UserRoleService userRoleService) {
        this.roleService = roleService;
        this.userService = userService;
        this.userRoleService = userRoleService;
    }

    @Override
    public void onApplicationEvent(StartupEvent event) {
        String username = "johnsnow@micronaut.example";
        String password = "aegon";
        save(username, password, Roles.ROLE_USER);
    }

    private void save(String username, String password, String... authorities) {
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
