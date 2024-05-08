package roomescape.dao;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MemberDaoTest {

    @Autowired
    private MemberDao memberDao;

    @Test
    void findIdByEmailAndPassword() {
        Long id = memberDao.findIdByEmailAndPassword("tenny@wooteco.com", "1234").get();

        assertThat(id).isEqualTo(1);
    }

    @ParameterizedTest
    @CsvSource(value = {"tenny@wooteco.com,0", "notExist,1234"})
    void findIdByEmailAndPassword_NotExistEmailAndPassword(String email, String password) {
        Optional<Long> optionalId = memberDao.findIdByEmailAndPassword(email, password);

        assertThat(optionalId.isEmpty()).isTrue();
    }
}
