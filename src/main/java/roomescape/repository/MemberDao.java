package roomescape.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.member.Member;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

@Repository
public class MemberDao {
    private static final String TABLE_NAME = "member";
    private static final String KEY_COLUMN_NAME = "id";

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;
    private final RowMapper<Member> memberRowMapper;

    public MemberDao(
            final JdbcTemplate jdbcTemplate,
            final DataSource source,
            final RowMapper<Member> memberRowMapper
    ) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(source)
                .withTableName(TABLE_NAME)
                .usingGeneratedKeyColumns(KEY_COLUMN_NAME);
        this.memberRowMapper = memberRowMapper;
    }

    public Optional<Member> findByEmailAndPassword(final String email, final String password) {
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE email = ? " + " AND password= ?";
        return jdbcTemplate.query(sql, memberRowMapper, email, password)
                .stream()
                .findAny();
    }

    public Optional<Member> findById(long memberId) {
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE id = ?";
        return jdbcTemplate.query(sql, memberRowMapper, memberId)
                .stream()
                .findAny();
    }

    public List<Member> findAll() {
        String sql = "SELECT * FROM " + TABLE_NAME;
        return jdbcTemplate.query(sql, memberRowMapper);
    }
}
