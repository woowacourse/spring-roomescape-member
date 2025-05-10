package roomescape.member.repository;

import javax.sql.DataSource;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import roomescape.member.domain.Member;

@Repository
public class MemberDao {

    private static final String TABLE_NAME = "member";

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public MemberDao(final NamedParameterJdbcTemplate jdbcTemplate, final DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName(TABLE_NAME)
                .usingGeneratedKeyColumns("id");
    }

    public Member insert(final Member member) {
        String name = member.getName();
        String email = member.getEmail();
        String password = member.getPassword();
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", name)
                .addValue("email", email)
                .addValue("password", password);
        long id = jdbcInsert.executeAndReturnKey(params).longValue();

        return new Member(id, name, email, password);
    }
}
