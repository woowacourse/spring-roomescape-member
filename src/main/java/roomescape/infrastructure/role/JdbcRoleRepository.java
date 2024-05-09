package roomescape.infrastructure.role;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.role.MemberRole;
import roomescape.domain.role.RoleRepository;
import roomescape.infrastructure.role.rowmapper.RoleRowMapper;

@Repository
public class JdbcRoleRepository implements RoleRepository {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public JdbcRoleRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("role")
                .usingColumns("member_id", "role");
    }

    @Override
    public MemberRole save(MemberRole memberRole) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("member_id", memberRole.getMemberId())
                .addValue("role", memberRole.getRole().getRoleName());
        jdbcInsert.execute(params);
        return memberRole;
    }

    @Override
    public boolean isAdminByMemberId(long memberId) {
        String sql = "select member_id, role from role where member_id = ?";
        try {
            MemberRole role = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> RoleRowMapper.mapRow(rs), memberId);
            return role != null && role.isAdmin();
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
    }
}
