package roomescape.domain.login.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.RepositoryTest;
import roomescape.domain.login.domain.User;

class UserRepositoryImplTest extends RepositoryTest {

    private final static User ADMIN_USER = new User(1L, "어드민", "admin@gmail.com", "123456");

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private UserRepository userRepository;

    UserRepositoryImplTest() {

    }

    @BeforeEach
    void setUp() {
        userRepository = new UserRepositoryImpl(jdbcTemplate);
        jdbcTemplate.update(
                "insert into escape_user (name, email, password) values ('어드민','admin@gmail.com','123456')");
    }

    @AfterEach
    void setDown() {
        jdbcTemplate.update("delete from escape_user");
    }

    @DisplayName("유저를 table에 넣을 수 있습니다.")
    @Test
    void should_insert_user() {
        User testUser = userRepository.insert(new User(null, "testName1", "testEmail1@gmail.com", "123123"));

        assertThat(testUser.getId()).isNotNull();
    }

    @DisplayName("원하는 id의 User를 찾을 수 있습니다.")
    @Test
    void should_find_user_by_id() {
        User expectedUser = ADMIN_USER;

        User actualUser = userRepository.findById(1L).get();

        assertThat(actualUser).isEqualTo(expectedUser);
    }

    @DisplayName("email과 password로 user를 찾을 수 있습니다.")
    @Test
    void should_find_user_by_email_and_password() {
        User expectedUser = ADMIN_USER;

        User actualUser = userRepository.findByEmailAndPassword("admin@gmail.com", "123456").get();

        assertThat(actualUser).isEqualTo(expectedUser);
    }
}
