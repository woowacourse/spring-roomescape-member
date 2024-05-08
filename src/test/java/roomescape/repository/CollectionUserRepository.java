package roomescape.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import roomescape.domain.User;

public class CollectionUserRepository implements UserRepository {

    private final List<User> users;
    private final AtomicLong index;

    public CollectionUserRepository() {
        this.users = new ArrayList<>();
        this.index = new AtomicLong(0);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return users.stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst();
    }

    @Override
    public User save(User user) {
        User savedUser = new User(index.incrementAndGet(), user);
        users.add(savedUser);
        return savedUser;
    }
}
