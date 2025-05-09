package roomescape.repository;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.Member;

import javax.sql.DataSource;
import java.util.Optional;

@Repository
public class JdbcMemberDao implements MemberRepository {

    private final static RowMapper<Member> rowMapper = (rs, rows) -> {
        long memberId = rs.getLong("member_id");
        String name = rs.getString("name");
        String email = rs.getString("email");
        String role = rs.getString("role");
        String password = rs.getString("password");

        return new Member(memberId, name, email, role, password);
    };

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public JdbcMemberDao(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("member")
                .usingGeneratedKeyColumns("member_id");
    }

    @Override
    public Member save(Member member) {
        try {
            SqlParameterSource params = new MapSqlParameterSource()
                    .addValue("name", member.getName())
                    .addValue("email", member.getEmail())
                    .addValue("role", member.getRole())
                    .addValue("password", member.getPassword());

            long id = jdbcInsert.executeAndReturnKey(params).longValue();
            return new Member(id, member.getName(), member.getEmail(), member.getRole(), member.getPassword());
        } catch (DuplicateKeyException e) {
            throw new IllegalStateException("[ERROR] 이미 등록된 EMAIL 입니다. " + member.getEmail());
        }
    }

    @Override
    public Optional<Member> findByEmailAndPassword(String email, String password) {
        try {
            String sql = """
                    SELECT member_id, name, email, role, password
                    FROM member
                    WHERE email = ? AND password = ?
                    """;
            return Optional.of(jdbcTemplate.queryForObject(sql, rowMapper, email, password));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Member> findById(long id) {
        try {
            String sql = """
                    SELECT member_id, name, email, role, password
                    FROM member
                    WHERE member_id = ?
                    """;
            return Optional.of(jdbcTemplate.queryForObject(sql, rowMapper, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public int deleteById(long id) {
        String sql = "DELETE FROM member WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }
}
