package roomescape.infrastructure;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.test.context.jdbc.Sql;
import roomescape.domain.Member;

import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@JdbcTest
class MemberJdbcRepositoryTest {

    private static final String MEMBER_TABLE_NAME = "member";
    private MemberJdbcRepository memberJdbcRepository;
    private SimpleJdbcInsert simpleJdbcInsert;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        this.memberJdbcRepository = new MemberJdbcRepository(jdbcTemplate);
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName(MEMBER_TABLE_NAME)
                .usingGeneratedKeyColumns("id");
    }

    @DisplayName("이메일로 사용자를 찾는다.")
    @Test
    void findMemberByEmail() {
        //given
        Member member = new Member("lini", "lini@email.com", "linirini");
        Map<String, ?> params = Map.of(
                "name", member.getMemberName(),
                "email", member.getEmail(),
                "password", member.getPassword());
        long id = simpleJdbcInsert.executeAndReturnKey(params).longValue();

        //when
        Optional<Member> result = memberJdbcRepository.findByEmail(member.getEmail());

        //then
        assertThat(result.get().getId()).isEqualTo(id);
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
        Member member = new Member("lini", "lini@email.com", "linirini");
        Map<String, ?> params = Map.of(
                "name", member.getMemberName(),
                "email", member.getEmail(),
                "password", member.getPassword());
        long id = simpleJdbcInsert.executeAndReturnKey(params).longValue();

        //when
        Member result = memberJdbcRepository.getByEmail(member.getEmail());

        //then
        assertThat(result.getId()).isEqualTo(id);
    }

}
