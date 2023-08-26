package com.ethdc.user.service.api;

import com.ethdc.user.service.model.User;
import com.github.javafaker.Faker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.IntConsumer;
import java.util.stream.IntStream;

/**
 * The type User api.
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserApi {

    private static final Map<Integer, User> USER_MAP = new ConcurrentHashMap<>(1) {
    };

    /**
     * Instantiates a new User api.
     */
    public UserApi() {
        var faker = new Faker();
        IntStream.range(1, 100)
                .forEach(mapUser(faker));
    }

    private static IntConsumer mapUser(Faker faker) {
        return id -> {
            var name = faker.name();
            var userName = name.username();
            USER_MAP.put(id,
                    new User(id, userName, userName + "@gmail.com", name.firstName(), name.lastName(), faker.number().numberBetween(18, 50)));
        };
    }

    /**
     * Gets users.
     *
     * @return the users
     */
    @GetMapping(value = "")
    public List<User> getUsers() {
        log.info("{}::{}","UserApi","getUsers()");
        return USER_MAP.values().stream().toList();
    }
    @GetMapping(value = "/{id}")
    public Mono<User> getUsers(@PathVariable int id) {
        return Mono.just(USER_MAP.get(id));
    }

    @GetMapping(value = "/list-fallback")
    public List<User> getUsersFallback() {
        return List.of();
    }

}
