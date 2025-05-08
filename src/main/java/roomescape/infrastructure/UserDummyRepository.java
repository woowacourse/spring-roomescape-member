package roomescape.infrastructure;

import java.util.Optional;
import org.springframework.stereotype.Repository;
import roomescape.domain.User;
import roomescape.domain.UserRole;
import roomescape.domain.repository.UserRepository;

@Repository
public class UserDummyRepository implements UserRepository{

    public static final User DUMMY_USER = new User(
        1L,
        "어드민",
        UserRole.ADMIN,
        "admin@email.com",
        "password");

    @Override
    public Optional<User> findById(final long id) {
        if (id != DUMMY_USER.id()) {
            return Optional.empty();
        }
        return Optional.of(DUMMY_USER);
    }

    @Override
    public long save(final User user) {
        return 1L;
    }

    @Override
    public Optional<User> findByEmail(final String email) {
        if (!email.equals(DUMMY_USER.email())) {
            return Optional.empty();
        }
        return Optional.of(DUMMY_USER);
    }
}
