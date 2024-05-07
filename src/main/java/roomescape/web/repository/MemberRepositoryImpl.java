package roomescape.web.repository;

import java.util.List;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.core.domain.Member;
import roomescape.core.repository.MemberRepository;

@Repository
public class MemberRepositoryImpl implements MemberRepository {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public MemberRepositoryImpl(final JdbcTemplate jdbcTemplate, final DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("member")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public Member findByEmailAndPassword(final String email, final String password) {
        final String query = "SELECT id, name, email, password FROM member WHERE email = ? AND password = ?";
        return jdbcTemplate.queryForObject(query, getUserRowMapper(), email, password);
    }

    @Override
    public Member findByEmail(final String email) {
        final String query = "SELECT id, name, email, password FROM member WHERE email = ?";
        return jdbcTemplate.queryForObject(query, getUserRowMapper(), email);
    }

    @Override
    public List<Member> findAll() {
        final String query = "SELECT id, name, email, password FROM member";
        return jdbcTemplate.query(query, getUserRowMapper());
    }

    private RowMapper<Member> getUserRowMapper() {
        return (resultSet, rowNum) -> {
            final Long memberId = resultSet.getLong("id");
            final String name = resultSet.getString("name");
            final String email = resultSet.getString("email");
            final String password = resultSet.getString("password");

            return new Member(memberId, name, email, password);
        };
    }
}
