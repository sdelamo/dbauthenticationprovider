package example.micronaut;

import example.micronaut.security.entities.RoleEntity;
import example.micronaut.security.entities.UserEntity;
import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.repository.CrudRepository;

@JdbcRepository(dialect = Dialect.MYSQL)
public interface UserCrudService extends CrudRepository<UserEntity, String> {
}
