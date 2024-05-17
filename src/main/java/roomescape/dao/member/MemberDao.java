package roomescape.dao.member;

import java.util.List;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.member.Member;
import roomescape.domain.member.MemberInfo;
import roomescape.domain.member.Role;

@Repository
public class MemberDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertActor;

    public MemberDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertActor = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("member")
                .usingGeneratedKeyColumns("id");
    }

    public List<MemberInfo> findAll() {
        String findAllSql = "SELECT id, name, role FROM member";
        return jdbcTemplate.query(findAllSql, memberInfoRowMapper());
    }

    public MemberInfo insert(Member member) {
        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("name", member.getName())
                .addValue("email", member.getEmail())
                .addValue("password", member.getPassword())
                .addValue("role", member.getRole().getRoleName());

        long id = insertActor.executeAndReturnKey(parameterSource).longValue();

        return new MemberInfo(id, member.getName(), member.getRole());
    }

    public Optional<MemberInfo> findByEmail(String email) {
        String sql = "SELECT id, name, role FROM member WHERE email = ? LIMIT 1";

        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, memberInfoRowMapper(), email));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Optional<MemberInfo> findById(Long id) {
        String sql = "SELECT id, name, role FROM member WHERE id = ? LIMIT 1";

        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, memberInfoRowMapper(), id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Boolean isMemberNotExist(String email, String password) {
        String sql = "SELECT EXISTS(SELECT * FROM member WHERE email = ? AND password = ?)";

        return Boolean.FALSE.equals(jdbcTemplate.queryForObject(sql, Boolean.class, email, password));
    }

    public Boolean isEmailExist(String email) {
        String sql = "SELECT EXISTS(SELECT * FROM member WHERE email = ?)";

        return jdbcTemplate.queryForObject(sql, Boolean.class, email);
    }

    private RowMapper<MemberInfo> memberInfoRowMapper() {
        return (rs, rowNum) -> new MemberInfo(
                rs.getLong("id"),
                rs.getString("name"),
                Role.of(rs.getString("role"))
        );
    }
}
