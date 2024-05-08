package roomescape.domain.login.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import roomescape.domain.login.domain.User;
import roomescape.domain.login.repository.UserRepository;

public class FakeUserRepository implements UserRepository {

    Map<Long, User> users = new HashMap<>();
    AtomicLong atomicLong = new AtomicLong(0);

    public User insert(User user) {
        Long id = atomicLong.incrementAndGet();

        User addUser = new User(id, user.getName(), user.getEmail(), user.getPassword());
        users.put(id, addUser);
        return addUser;
    }

    @Override
    public Optional<User> findById(Long id) {
        if (users.containsKey(id)) {
            return Optional.of(users.get(id));
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> findByEmailAndPassword(String email, String password) {
        return Optional.empty();
    }
}
