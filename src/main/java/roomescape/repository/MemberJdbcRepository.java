package roomescape.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.model.Member;
import roomescape.model.Role;

@Repository
public class MemberJdbcRepository implements MemberRepository {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public MemberJdbcRepository(final DataSource dateSource) {
        this.jdbcTemplate = new JdbcTemplate(dateSource);
        this.simpleJdbcInsert = new SimpleJdbcInsert(dateSource)
                .withTableName("MEMBER")
                .usingGeneratedKeyColumns("ID");
    }

    @Override
    public List<Member> findAll() {
        return List.of();
    }

    @Override
    public Long save(final Member member) {
        return 0L;
    }

    @Override
    public Optional<Member> findById(final Long id) {
        final String sql = "SELECT * FROM MEMBER WHERE ID = ?";

        return jdbcTemplate.query(sql, this::mapToMember, id)
                .stream()
                .findAny();
    }

    @Override
    public Optional<Member> findByEmail(final String email) {
        return Optional.empty();
    }

    @Override
    public Boolean removeById(final Long id) {
        return null;
    }

    private Member mapToMember(ResultSet rs, int rowNum) throws SQLException {
        return new Member(
                rs.getLong("ID"),
                rs.getString("NAME"),
                rs.getString("EMAIL"),
                rs.getString("PASSWORD"),
                Role.findByName(rs.getString("ROLE"))
        );
    }
}
