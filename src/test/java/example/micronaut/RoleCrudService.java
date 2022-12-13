package example.micronaut;

import example.micronaut.security.entities.RoleEntity;
import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.repository.CrudRepository;
import io.micronaut.data.repository.GenericRepository;

@JdbcRepository(dialect = Dialect.MYSQL)
public interface RoleCrudService extends CrudRepository<RoleEntity, String> {
}
