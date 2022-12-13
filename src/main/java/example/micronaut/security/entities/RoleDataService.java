package example.micronaut.security.entities;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.repository.GenericRepository;

import javax.validation.constraints.NotBlank;
import java.util.Optional;

@JdbcRepository(dialect = Dialect.MYSQL)
public interface RoleDataService extends GenericRepository<RoleEntity, String> {

    RoleEntity save(@NonNull RoleEntity roleEntity);

    @NonNull
    Optional<RoleEntity> findByAuthority(@NonNull @NotBlank String authority);

    long countByAuthority(@NonNull @NotBlank String authority);
}
