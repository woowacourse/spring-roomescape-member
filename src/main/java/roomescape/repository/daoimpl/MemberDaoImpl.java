package roomescape.repository.daoimpl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.member.Member;
import roomescape.repository.MemberDao;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

@Repository
public class MemberDaoImpl implements MemberDao {
    private static final String TABLE_NAME = "member";
    private static final String KEY_COLUMN_NAME = "id";

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;
    private final RowMapper<Member> memberRowMapper;

    public MemberDaoImpl(
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

    @Override
    public Optional<Member> findByEmailAndPassword(final String email, final String password) {
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE email = ? " + " AND password= ?";
        return jdbcTemplate.query(sql, memberRowMapper, email, password)
                .stream()
                .findAny();
    }

    @Override
    public Optional<Member> findById(long memberId) {
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE id = ?";
        return jdbcTemplate.query(sql, memberRowMapper, memberId)
                .stream()
                .findAny();
    }

    @Override
    public List<Member> findAll() {
        String sql = "SELECT * FROM " + TABLE_NAME;
        return jdbcTemplate.query(sql, memberRowMapper);
    }
}
