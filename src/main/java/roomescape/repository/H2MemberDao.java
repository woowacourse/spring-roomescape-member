package roomescape.repository;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import roomescape.entity.Member;
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
    public Optional<Member> findByEmailAndPassword(String email, String password) {
        String sql = """
            SELECT *
            FROM member
            WHERE email = :email AND password = :password
            """;

        MapSqlParameterSource parameterSource = new MapSqlParameterSource()
            .addValue("email", email)
            .addValue("password", password);
        List<Member> findMembers = jdbcTemplate.query(sql, parameterSource, getMemberRowMapper());
        return findMembers.stream().findFirst();
    }

    @Override
    public Optional<Member> findById(Long memberId) {
        String sql = """
            SELECT *
            FROM member
            WHERE id = :id
            """;

        List<Member> findMembers = jdbcTemplate.query(sql, new MapSqlParameterSource("id", memberId), getMemberRowMapper());
        return findMembers.stream().findFirst();
    }

    private RowMapper<Member> getMemberRowMapper() {
        return (resultSet, rowNum) -> new Member(
            resultSet.getLong("id"),
            resultSet.getString("name"),
            resultSet.getString("email"),
            resultSet.getString("password"),
            Role.valueOf(resultSet.getString("role"))
        );
    }
}
