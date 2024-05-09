package roomescape.dao;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import roomescape.domain.User;

@SpringBootTest
class UserDaoTest {
    @Autowired
    private UserDao userDao;

    private User dummyUser;

    @BeforeEach
    void setUp() {
        dummyUser = new User("name", "email", "password");
    }

    @AfterEach
    void tearDown() {
        userDao.deleteAll();
    }

    @Test
    @DisplayName("사용자 정보를 저장한다")
    void save_ShouldStorePersistence() {
        // given
        dummyUser = new User("name", "email", "password");

        // when
        User savedUser = userDao.save(dummyUser);

        // then
        Assertions.assertThat(userDao.findById(savedUser.getId())).isPresent();
    }

    @Test
    @DisplayName("영속성에 사용자 있으면 참을 반환한다")
    void existsByEmailAndPassword_ShouldGetPersistence() {
        // given
        dummyUser = new User("name", "email", "password");
        userDao.save(dummyUser);

        // when & then
        Assertions.assertThat(userDao.findByEmailAndPassword(dummyUser.getEmail(), dummyUser.getPassword()))
                .isPresent();
    }

    @Test
    @DisplayName("영속성에 사용자 없으면 거짓을 반환한다")
    void existsByEmailAndPassword_ShouldReturnNull_WhenTargetDoesNotExist() {
        // when & then
        Assertions.assertThat(userDao.findByEmailAndPassword(dummyUser.getEmail(), dummyUser.getPassword()))
                .isEmpty();
    }
}
