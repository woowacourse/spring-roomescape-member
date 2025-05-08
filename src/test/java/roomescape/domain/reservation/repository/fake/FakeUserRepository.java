package roomescape.domain.reservation.repository.fake;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import roomescape.common.exception.EntityNotFoundException;
import roomescape.domain.auth.entity.Name;
import roomescape.domain.auth.entity.Roles;
import roomescape.domain.auth.entity.User;
import roomescape.domain.auth.repository.UserRepository;

public class FakeUserRepository implements UserRepository {
    private static final Long INITIAL_ID = 1L;

    private final AtomicLong id = new AtomicLong(INITIAL_ID);
    private final Map<Long, User> users = new ConcurrentHashMap<>();

    @Override
    public User save(final User user) {
        if (user.existId() && !users.containsKey(user.getId())) {
            throw new EntityNotFoundException("user with id " + user.getId() + " not found");
        }

        if (user.existId()) {
            users.put(user.getId(), user);
            return user;
        }

        return getUser(user);
    }

    private User getUser(final User user) {
        final User userWithId = new User(id.getAndIncrement(), new Name(user.getName()), user.getEmail(),
                user.getPassword(), Roles.USER);
        users.put(userWithId.getId(), userWithId);
        return userWithId;
    }

    @Override
    public boolean existsByEmail(final String email) {
        return users.values()
                .stream()
                .anyMatch(user -> user.getEmail()
                        .equals(email));
    }

    @Override
    public List<User> findAll() {
        return users.values()
                .stream()
                .toList();
    }

    @Override
    public Optional<User> findById(final Long userId) {
        return users.values()
                .stream()
                .filter(user -> user.getId()
                        .equals(userId))
                .findFirst();
    }

    @Override
    public Optional<User> findByEmail(final String email) {
        return users.values()
                .stream()
                .filter(user -> user.getEmail()
                        .equals(email))
                .findFirst();
    }

    public void deleteById(final Long userId) {
        users.remove(userId);
    }

    public void deleteAll() {
        users.clear();
    }
}
