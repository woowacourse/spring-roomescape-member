package roomescape.auth.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import roomescape.auth.domain.User;

public class FakeUserDao implements UserRepository{

    private final Map<Long, User> users = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong();

    @Override
    public Optional<User> findByEmail(String email) {
        List<User> emailUsers = users.values().stream()
                .filter(user -> user.getEmail().equals(email))
                .toList();

        if (emailUsers.isEmpty()) {
            return Optional.empty();
        }
        if(emailUsers.size() > 1){
            throw new IllegalStateException("조회 결과가 2개 이상입니다.");
        }

        return Optional.of(emailUsers.getFirst());
    }

    public Long save(String name, String email, String password){
        Long generatedId = idGenerator.incrementAndGet();
        User user = new User(generatedId, name, email, password);
        users.put(generatedId, user);
        return generatedId;
    }

}
