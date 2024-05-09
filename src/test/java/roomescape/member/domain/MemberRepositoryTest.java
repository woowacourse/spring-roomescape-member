package roomescape.member.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import roomescape.common.RepositoryTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static roomescape.TestFixture.MIA_EMAIL;
import static roomescape.TestFixture.USER_MIA;

class MemberRepositoryTest extends RepositoryTest {
    @Autowired
    private MemberRepository memberRepository;

    private SimpleJdbcInsert jdbcInsert;

    @BeforeEach
    void setUp() {
        jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("member")
                .usingGeneratedKeyColumns("id");
    }

    @Test
    @DisplayName("사용자를 저장한다.")
    void save() {
        // given
        Member member = USER_MIA();

        // when
        Member savedMember = memberRepository.save(member);

        // then
        assertThat(savedMember.getId()).isNotNull();
    }

    @Test
    @DisplayName("이메일로 사용자를 조회한다.")
    void findByEmail() {
        // given
        Member member = USER_MIA();
        SqlParameterSource params = new BeanPropertySqlParameterSource(member);
        jdbcInsert.execute(params);

        // when
        Optional<Member> foundMember = memberRepository.findByEmail(MIA_EMAIL);

        // then
        assertThat(foundMember).isNotEmpty();
    }

    @Test
    @DisplayName("Id로 사용자를 조회한다.")
    void findById() {
        // given
        Member member = USER_MIA();
        SqlParameterSource params = new BeanPropertySqlParameterSource(member);
        Long id = jdbcInsert.executeAndReturnKey(params).longValue();

        // when
        Optional<Member> foundMember = memberRepository.findById(id);

        // then
        assertThat(foundMember).isNotEmpty();
    }
}
