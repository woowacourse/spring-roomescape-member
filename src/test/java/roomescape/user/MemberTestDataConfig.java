package roomescape.user;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import roomescape.user.domain.Role;
import roomescape.user.domain.User;
import roomescape.user.fixture.UserFixture;
import roomescape.user.repository.UserRepository;

@TestConfiguration
public class MemberTestDataConfig {

    private static final Role ROLE_FIELD = Role.ROLE_MEMBER;
    private static final String NAME_FIELD = "member_dummyName";
    private static final String EMAIL_FIELD = "member_dummyEmail";
    private static final String PASSWORD_FILED = "member_dummyPassword";

    @Autowired
    private UserRepository repository;

    private Long savedId;
    private User savedMember;

    @PostConstruct
    public void setUpTestData() {
        User user = UserFixture.create(ROLE_FIELD, NAME_FIELD, EMAIL_FIELD, PASSWORD_FILED);
        savedMember = repository.save(user);
        savedId = savedMember.getId();
    }

    public Long getSavedId() {
        return savedId;
    }

    public User getSavedMember() {
        return savedMember;
    }
}
