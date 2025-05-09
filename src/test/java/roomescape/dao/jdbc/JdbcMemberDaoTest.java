package roomescape.dao.jdbc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import roomescape.domain.Member;
import roomescape.exception.custom.NotFoundException;

@JdbcTest
@Import(JdbcMemberDao.class)
class JdbcMemberDaoTest {

    @Autowired
    private JdbcMemberDao jdbcMemberDao;

    @Test
    @DisplayName("이메일이 존재한다면 조회할 수 있다.")
    void findMemberByExistedEmail() {
        String email = "sa123";
        Member expected = jdbcMemberDao.findMemberByEmail(email);

        assertThat(expected.getEmail()).isEqualTo(email);
    }

    @Test
    @DisplayName("이메일이 존재하지 않는다면 예외가 발생한다.")
    void findMemberByNotExistedEmail() {
        assertThatThrownBy(() -> jdbcMemberDao.findMemberByEmail("notEmail"))
            .isInstanceOf(NotFoundException.class)
            .hasMessageContaining("member");
    }
}
