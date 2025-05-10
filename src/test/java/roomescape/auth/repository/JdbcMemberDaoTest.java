package roomescape.auth.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import javax.sql.DataSource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.member.domain.Member;
import roomescape.member.repository.JdbcMemberDao;
import roomescape.util.repository.TestDataSourceFactory;

class JdbcMemberDaoTest {

    private JdbcMemberDao jdbcMemberDao;
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setup() {
        DataSource dataSource = TestDataSourceFactory.getEmbeddedDataSource();
        jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcMemberDao = new JdbcMemberDao(jdbcTemplate);
    }

    @AfterEach
    void dropTable() {
        String dropSql = "DROP TABLE IF EXISTS reservation, reservation_time, theme, member";
        jdbcTemplate.execute(dropSql);
    }

    @DisplayName("이메일에 해당하는 사용자를 조회한다")
    @Test
    void find_by_email_test() {
        // given
        String email = "rookie@woowa.com";

        // when
        Member findMember = jdbcMemberDao.findByEmail(email).get();

        // then
        assertAll(
                () -> assertThat(findMember.getId()).isEqualTo(1L),
                () -> assertThat(findMember.getEmail()).isEqualTo("rookie@woowa.com"),
                () -> assertThat(findMember.getName()).isEqualTo("루키")
        );
    }

}
