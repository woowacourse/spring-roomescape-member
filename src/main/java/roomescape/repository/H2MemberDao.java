package roomescape.repository;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import roomescape.entity.LoginMember;
import roomescape.entity.Role;

import java.util.List;
import java.util.Optional;


@Repository
public class H2MemberDao implements MemberDao {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public H2MemberDao(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<LoginMember> findByEmailAndPassword(String email, String password) {
        String sql = """
            SELECT *
            FROM member
            WHERE email = :email AND password = :password
            """;

        MapSqlParameterSource parameterSource = new MapSqlParameterSource()
            .addValue("email", email)
            .addValue("password", password);
        List<LoginMember> findLoginMembers = jdbcTemplate.query(sql, parameterSource, getMemberRowMapper());
        return findLoginMembers.stream().findFirst();
    }

    @Override
    public Optional<LoginMember> findById(Long memberId) {
        String sql = """
            SELECT id, name, email, role
            FROM member
            WHERE id = :id
            """;

        List<LoginMember> findLoginMembers = jdbcTemplate.query(sql, new MapSqlParameterSource("id", memberId), getMemberRowMapper());
        return findLoginMembers.stream().findFirst();
    }

    private RowMapper<LoginMember> getMemberRowMapper() {
        return (resultSet, rowNum) -> new LoginMember(
            resultSet.getLong("id"),
            resultSet.getString("name"),
            resultSet.getString("email"),
            Role.valueOf(resultSet.getString("role"))
        );
    }
}
