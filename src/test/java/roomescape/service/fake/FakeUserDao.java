package roomescape.service.fake;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import roomescape.model.User;
import roomescape.repository.UserDao;

public class FakeUserDao implements UserDao {

    private final List<User> users = new ArrayList<>();
    private final AtomicLong atomicLong = new AtomicLong(1L);

    @Override
    public Optional<User> findUserByEmailAndPassword(String email, String password) {
        return users.stream()
                .filter(user -> user.getEmail() == email && user.getPassword() == password)
                .findAny();
    }

    public void addUser(User user) {
        users.add(user);
    }

    public void clear() {
        atomicLong.set(1L);
        users.clear();
    }
}
