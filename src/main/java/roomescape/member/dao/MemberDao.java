package roomescape.member.dao;

import java.sql.ResultSet;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.member.domain.Member;
import roomescape.member.domain.repository.MemberRepository;

@Repository
public class MemberDao implements MemberRepository {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    private final ResultSetExtractor<Optional<Member>> optionalResultSetExtractor = (ResultSet resultSet) -> {
        if (resultSet.next()) {
            Member member = new Member(
                    resultSet.getLong("id"),
                    resultSet.getString("name"),
                    resultSet.getString("email")
            );
            return Optional.of(member);
        } else {
            return Optional.empty();
        }
    };

    public MemberDao(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("member")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public Member save(Member member) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", member.getName());
        Long id = simpleJdbcInsert.executeAndReturnKey(params).longValue();

        return new Member(id, member.getName());
    }

    @Override
    public Optional<Member> findBy(String email) {
        String sql = "SELECT id, name, email FROM member WHERE email = ?;";
        return jdbcTemplate.query(sql, optionalResultSetExtractor, email);
    }

    @Override
    public boolean existsBy(String email, String password) {
        String sql = """
                SELECT 1
                FROM member 
                WHERE email = ? AND password = ? 
                LIMIT 1;
                """;

        return jdbcTemplate.query(sql, ResultSet::next, email, password);
    }
}
