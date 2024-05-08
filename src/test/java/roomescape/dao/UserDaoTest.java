package roomescape.dao;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import roomescape.domain.User;

@SpringBootTest
class UserDaoTest {
    @Autowired
    private UserDao userDao;

    @Test
    @DisplayName("사용자 정보를 저장한다")
    void save_ShouldStorePersistence() {
        // given
        User user = new User("name", "email", "password");

        // when
        User savedUser = userDao.save(user);

        // then
        Assertions.assertThat(userDao.findById(savedUser.getId())).isEqualTo(savedUser);
    }
}
