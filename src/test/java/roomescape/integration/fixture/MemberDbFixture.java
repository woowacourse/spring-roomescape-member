package roomescape.integration.fixture;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import roomescape.domain.member.Member;
import roomescape.domain.member.MemberEmail;
import roomescape.domain.member.MemberEncodedPassword;
import roomescape.domain.member.MemberName;
import roomescape.domain.member.MemberRole;

@Component
public class MemberDbFixture {

    private final JdbcTemplate jdbcTemplate;
    private final PasswordEncoder encoder;

    public MemberDbFixture(final JdbcTemplate jdbcTemplate, final PasswordEncoder encoder) {
        this.jdbcTemplate = jdbcTemplate;
        this.encoder = encoder;
    }

    public Member 한스_leehyeonsu4888_지메일_일반_멤버() {
        MemberEmail email = new MemberEmail("leenyeonsu4888@gmail.com");
        MemberName name = new MemberName("한스");
        MemberEncodedPassword password = new MemberEncodedPassword(encoder.encode("gustn111!!"));
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("member")
                .usingGeneratedKeyColumns("id");
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("name", name.name())
                .addValue("email", email.email())
                .addValue("password", password.password())
                .addValue("role", MemberRole.MEMBER);
        Long id = simpleJdbcInsert.executeAndReturnKey(parameters).longValue();

        return new Member(id, name, email, password, MemberRole.MEMBER);
    }
}
