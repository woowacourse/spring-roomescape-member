package roomescape.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import roomescape.RepositoryTest;
import roomescape.domain.member.Member;
import roomescape.repository.rowmapper.MemberRowMapper;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class MemberDaoTest extends RepositoryTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private RowMapper<Member> rowMapper;
    private MemberDao memberDao;


    @BeforeEach
    void setUp() {
        rowMapper = new MemberRowMapper();
        memberDao = new MemberDao(jdbcTemplate, rowMapper);
    }

    @Test
    @DisplayName("비밀번호와 이메일이 일치하는 회원을 조회할 수 있다")
    void should_FindByEmailAndPassword() {
        //given
        String targetName = "coli1";
        String targetEmail = "a@a.com";
        String targetPassword = "userpassword";

        //when
        Optional<Member> foundMember = memberDao.findByEmailAndPassword(targetEmail, targetPassword);

        //then
        assertThat(foundMember.isPresent()).isTrue();
        assertThat(foundMember.get().getEmail()).isEqualTo(targetEmail);
        assertThat(foundMember.get().getPassword()).isEqualTo(targetPassword);
        assertThat(foundMember.get().getName()).isEqualTo(targetName);
    }

    @Test
    @DisplayName("id가 일치하는 회원을 조회할 수 있다")
    void should_FindById() {
        //given
        long targetId = 1;
        String targetName = "coli1";
        String targetEmail = "a@a.com";
        String targetPassword = "userpassword";

        //when
        Optional<Member> foundMember = memberDao.findById(targetId);

        //then
        assertThat(foundMember.isPresent()).isTrue();
        assertThat(foundMember.get().getEmail()).isEqualTo(targetEmail);
        assertThat(foundMember.get().getPassword()).isEqualTo(targetPassword);
        assertThat(foundMember.get().getName()).isEqualTo(targetName);
    }

    @Test
    @DisplayName("전체 회원을 조회할 수 있다")
    void should_findAllMembers() {
        //given
        int expectedSize = 2;

        //when
        List<Member> members = memberDao.findAll();

        //then
        assertThat(members).hasSize(expectedSize);
    }
}