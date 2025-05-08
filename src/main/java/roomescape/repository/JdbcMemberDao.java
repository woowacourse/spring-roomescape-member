package roomescape.repository;

import javax.sql.DataSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.RegistrationDetails;

@Repository
public class JdbcMemberDao implements MemberRepository {

    private final SimpleJdbcInsert jdbcInsert;

    public JdbcMemberDao(DataSource source) {
        this.jdbcInsert = new SimpleJdbcInsert(source)
                .withTableName("member")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public void save(final RegistrationDetails registrationDetails) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", registrationDetails.name())
                .addValue("email", registrationDetails.email())
                .addValue("password", registrationDetails.password());

        jdbcInsert.executeAndReturnKey(params).longValue();
    }
}
