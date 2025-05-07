package roomescape.fake;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import roomescape.dao.UserDao;
import roomescape.model.User;

public class FakeUserDao implements UserDao {

    private final Map<Long, User> database = new HashMap<>();
    private final AtomicLong nextId = new AtomicLong(1L);

    @Override
    public Optional<User> findByEmail(String email) {
        return database.values().stream()
                .filter(user -> user.getEmail().equals(email))
                .findAny();
    }

    public Long add(User user) {
        Long id = nextId.getAndIncrement();
        database.put(id, new User(
                user.getName(),
                user.getEmail(),
                user.getPassword()
        ));
        return id;
    }
}
