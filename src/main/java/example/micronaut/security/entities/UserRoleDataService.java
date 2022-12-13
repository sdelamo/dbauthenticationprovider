package example.micronaut.security.entities;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.data.annotation.Query;
import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.repository.CrudRepository;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.List;

@JdbcRepository(dialect = Dialect.MYSQL)
public interface UserRoleDataService extends CrudRepository<UserRoleEntity, UserRoleId> {

    @Query("select role.authority from user_role inner join role on role.id = user_role.id_role_id inner join user on user.id = user_role.id_user_id where user.email = :email")
    List<String> findAllAuthoritiesByEmail(@NonNull @NotBlank @Email String email);
}
