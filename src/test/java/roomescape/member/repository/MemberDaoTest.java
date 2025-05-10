package roomescape.member.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;

import roomescape.member.domain.Member;

@JdbcTest
@Import(MemberDao.class)
class MemberDaoTest {

    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    private MemberDao memberDao;

    @AfterEach
    void tearDown() {
        jdbcTemplate.update("delete from member");
    }

    @DisplayName("사용자를 DB에 저장한다.")
    @Test
    void insertMember() {
        Member member = new Member("이프", "if@posty.com", "12345678");

        Member insertedMember = memberDao.insert(member);

        assertAll(
                () -> assertThat(insertedMember.getId()).isNotZero(),
                () -> assertThat(insertedMember.getName()).isEqualTo("이프"),
                () -> assertThat(insertedMember.getEmail()).isEqualTo("if@posty.com"),
                () -> assertThat(insertedMember.getPassword()).isEqualTo("12345678")
        );
    }
}
