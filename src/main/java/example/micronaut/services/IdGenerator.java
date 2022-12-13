package example.micronaut.services;

import io.micronaut.core.annotation.NonNull;

import java.util.Optional;

@FunctionalInterface
public interface IdGenerator {

    @NonNull
    Optional<String> generate();
}
