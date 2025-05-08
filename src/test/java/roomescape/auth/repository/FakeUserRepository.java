package roomescape.auth.repository;

import roomescape.auth.entity.User;
import roomescape.exception.conflict.ConflictException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FakeUserRepository implements UserRepository {
    private final List<User> users = new ArrayList<>();

    @Override
    public Optional<User> findByEmailAndPassword(String email, String password) {
        return users.stream()
                .filter(user -> user.getEmail().equals(email) && user.getPassword().equals(password))
                .findFirst();
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return users.stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst();
    }

    @Override
    public User save(User user) {
        if (findByEmail(user.getEmail()).isPresent()) {
            throw new ConflictException("이미 존재하는 유저입니다.");
        }
        users.add(user);
        return user;
    }
}
