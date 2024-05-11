package roomescape.repository.role;

import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.MemberRole;
import roomescape.domain.Role;

@Repository
public class RoleDao implements RoleRepository {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    private static final RowMapper<MemberRole> rowMapper = (rs, rowNum) -> new
            MemberRole(
            rs.getLong("member_id"),
            Role.valueOf(rs.getString("role"))
    );

    public RoleDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("role")
                .usingColumns("member_id", "role");
    }

    @Override
    public MemberRole save(MemberRole role) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("member_id", role.getMemberId())
                .addValue("role", role.getRole().name());
        simpleJdbcInsert.execute(params);
        return role;
    }

    @Override
    public Optional<MemberRole> findByMemberId(long memberId) {
        String sql = "SELECT MEMBER_ID, ROLE FROM ROLE WHERE MEMBER_ID = ?";
        try {
            MemberRole role = jdbcTemplate.queryForObject(sql, rowMapper, memberId);
            return Optional.ofNullable(role);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}
