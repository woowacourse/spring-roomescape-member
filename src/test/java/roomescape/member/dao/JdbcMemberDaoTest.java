package roomescape.member.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import roomescape.member.Member;

import org.junit.jupiter.api.Test;

@Import({JdbcMemberDao.class})
@JdbcTest
@Sql({"/sql/test-schema.sql", "/sql/test-data.sql"})
public class JdbcMemberDaoTest {

    @Autowired
    private JdbcMemberDao memberDao;

    @Test
    void 이메일로_회원을_조회할_수_있다() {
        // given
        String email = "forarium20@gmail.com";

        // when
        Optional<Member> member = memberDao.findMember(email);

        // then
        assertThat(member.isPresent()).isTrue();
        assertThat(member.get().getEmail()).isEqualTo(email);
        assertThat(member.get().getName()).isEqualTo("이뜽연");
    }

    @Test
    void 아이디로_회원을_조회할_수_있다() {
        // given
        Long id = 1L;

        // when
        Optional<Member> member = memberDao.findById(id);

        // then
        assertThat(member.isPresent()).isTrue();
        assertThat(member.get().getId()).isEqualTo(id);
        assertThat(member.get().getName()).isEqualTo("이뜽연");
    }

    @Test
    void 모든_회원을_조회할_수_있다() {
        // when
        List<Member> all = memberDao.findAll();

        // then
        assertThat(all).hasSize(4);
        assertThat(all.get(0).getName()).isEqualTo("이뜽연");
    }
}
