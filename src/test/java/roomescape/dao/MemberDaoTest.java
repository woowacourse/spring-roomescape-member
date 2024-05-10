package roomescape.dao;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import roomescape.domain.member.dao.MemberDao;
import roomescape.domain.member.domain.Role;
import roomescape.global.auth.AuthUser;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MemberDaoTest {

    @Autowired
    private MemberDao memberDao;

    @Test
    void findIdByEmailAndPassword() {
        AuthUser authUser = memberDao.findIdByEmailAndPassword("tenny@wooteco.com", "1234").get();

        assertThat(authUser).isEqualTo(new AuthUser(1L, "테니", Role.MEMBER));
    }

    @ParameterizedTest
    @CsvSource(value = {"tenny@wooteco.com,0", "notExist,1234"})
    void findIdByEmailAndPassword_NotExistEmailAndPassword(String email, String password) {
        Optional<AuthUser> optionalAuthUser = memberDao.findIdByEmailAndPassword(email, password);

        assertThat(optionalAuthUser.isEmpty()).isTrue();
    }
}
