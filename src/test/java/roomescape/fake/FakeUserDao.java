package roomescape.fake;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import roomescape.business.domain.Role;
import roomescape.business.domain.User;
import roomescape.persistence.dao.UserDao;

public class FakeUserDao implements UserDao {

    private static final List<User> USERS_FIXTURE = new ArrayList<>(List.of(
            User.createWithId(1L, "hotteok", "111@111.com", "qwe123", Role.USER),
            User.createWithId(2L, "gugu", "222@222.com", "qwe123", Role.ADMIN)
    ));

    @Override
    public Optional<User> find(final Long id) {
        return USERS_FIXTURE.stream()
                .filter(user -> user.getId().equals(id))
                .findAny();
    }

    @Override
    public List<User> findAll() {
        return USERS_FIXTURE;
    }

    @Override
    public Optional<User> findByPrincipal(final String principal) {
        return USERS_FIXTURE.stream()
                .filter(user -> user.getEmail().equals(principal))
                .findAny();
    }
}
