package roomescape.user;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import roomescape.user.domain.User;
import roomescape.user.fixture.UserFixture;
import roomescape.user.repository.UserRepository;

@TestConfiguration
public class UserTestDataConfig {

    private static final String NAME_FIELD = "dummyName";
    private static final String EMAIL_FIELD = "dummyEmail";
    private static final String PASSWORD_FILED = "dummyPassword";

    @Autowired
    private UserRepository repository;

    private Long savedId;
    private User savedUser;

    @PostConstruct
    public void setUpTestData() {
        User user = UserFixture.create(NAME_FIELD, EMAIL_FIELD, PASSWORD_FILED);
        savedUser = repository.save(user);
        savedId = savedUser.getId();
    }

    public Long getSavedId() {
        return savedId;
    }

    public User getSavedUser() {
        return savedUser;
    }
}
