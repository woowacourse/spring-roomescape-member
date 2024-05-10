package roomescape.infrastructure;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import roomescape.member.domain.Member;
import roomescape.member.domain.Role;
import roomescape.member.infrastructure.MemberJdbcRepository;

import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@JdbcTest
class MemberJdbcRepositoryTest {
    private MemberJdbcRepository memberJdbcRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        this.memberJdbcRepository = new MemberJdbcRepository(jdbcTemplate);
    }

    @DisplayName("사용자를 저장한다.")
    @Test
    void saveMember() {
        //given
        String email = "lini@email.com";
        String name = "lini";
        String password = "l2n2r2n2";

        //when
        Member member = memberJdbcRepository.save(new Member(name, email, password, Role.GUEST));

        //then
        assertAll(
                () -> assertThat(member.getId()).isNotZero(),
                () -> assertThat(member.getMemberName()).isEqualTo(name),
                () -> assertThat(member.getPassword()).isEqualTo(password),
                () -> assertThat(member.getEmail()).isEqualTo(email)
        );
    }

    @DisplayName("이메일로 사용자를 찾는다.")
    @Test
    void findMemberByEmail() {
        //given
        Member member = memberJdbcRepository.save(new Member("lini","lini@email.com","lini123", Role.GUEST));

        //when
        Optional<Member> result = memberJdbcRepository.findByEmail(member.getEmail());

        //then
        assertThat(result.get().getId()).isEqualTo(member.getId());
    }

    @DisplayName("이메일로 사용자를 찾을 수 없다.")
    @Test
    void cannotFindMemberByEmail() {
        //given
        String email = "lini@email.com";

        //when
        Optional<Member> result = memberJdbcRepository.findByEmail(email);

        //then
        assertThat(result).isEmpty();
    }

    @DisplayName("이메일로 존재하는 사용자를 가져온다.")
    @Test
    void getMemberByEmail() {
        //given
        Member member = memberJdbcRepository.save(new Member("lini","lini@email.com","lini123", Role.GUEST));

        //when
        Member result = memberJdbcRepository.getByEmail(member.getEmail());

        //then
        assertThat(result.getId()).isEqualTo(member.getId());
    }

}
