package roomescape.repository;

import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import roomescape.domain.member.Member;
import roomescape.domain.member.MemberEmail;
import roomescape.domain.member.MemberEncodedPassword;
import roomescape.domain.member.MemberName;

@Repository
public class MemberRepository {
    private final JdbcTemplate jdbcTemplate;

    public MemberRepository(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        save(
                new MemberEmail("leehyeonsu4888@gmail.com"),
                new MemberName("한스"),
                new MemberEncodedPassword(encoder.encode("gustn111!!"))
        );
    }

    public Member save(MemberEmail email, MemberName name, MemberEncodedPassword password) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("member")
                .usingGeneratedKeyColumns("id");
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("name", name.name())
                .addValue("email", email.email())
                .addValue("password", password.password());
        Long id = simpleJdbcInsert.executeAndReturnKey(parameters).longValue();

        return new Member(id, name, email, password);
    }

    public Optional<Member> findByEmail(MemberEmail email) {
        System.out.println(email.email());
        String sql = "select * from member where email = ? limit 1";
        return jdbcTemplate.query(sql, (resultSet, rowNum) -> new Member(
                resultSet.getLong("id"),
                new MemberName(resultSet.getString("name")),
                new MemberEmail(resultSet.getString("email")),
                new MemberEncodedPassword(resultSet.getString("password"))
        ), email.email()).stream().findFirst();
    }
}
