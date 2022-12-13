package example.micronaut.security;

import example.micronaut.services.IdGenerator;
import io.micronaut.security.token.jwt.generator.claims.JwtIdGenerator;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

@Singleton
public class KsuidJwtIdGenerator implements JwtIdGenerator {
    private static final Logger LOG = LoggerFactory.getLogger(KsuidJwtIdGenerator.class);
    private final IdGenerator idGenerator;

    public KsuidJwtIdGenerator(IdGenerator idGenerator) {
        this.idGenerator = idGenerator;
    }

    @Override
    public String generateJtiClaim() {
        String jwtId = idGenerator.generate().orElse(UUID.randomUUID().toString());

        LOG.info("ID {}", jwtId);

        return jwtId;
    }
}
