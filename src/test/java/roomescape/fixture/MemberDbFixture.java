package roomescape.fixture;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import roomescape.domain.Member;

@Component
public class MemberDbFixture {

    private final SimpleJdbcInsert jdbcInsert;

    public MemberDbFixture(JdbcTemplate jdbcTemplate) {
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("member")
                .usingGeneratedKeyColumns("id");
    }

    public Member 듀이() {
        String name = "듀이";
        String email = "test@test.com";
        String password = "pass1";

        Long id = jdbcInsert.executeAndReturnKey(new MapSqlParameterSource()
                .addValue("name", name)
                .addValue("email", email)
                .addValue("password", password)
        ).longValue();

        return new Member(id, name, email, password);
    }
}
