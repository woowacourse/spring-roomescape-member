package roomescape.repository.member;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.Email;
import roomescape.domain.Member;
import roomescape.domain.Name;
import roomescape.domain.Role;

import java.util.List;
import java.util.Optional;

@Repository
public class MemberH2Repository implements MemberRepository{

    private static final String TABLE_NAME = "member";

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public MemberH2Repository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .usingGeneratedKeyColumns("id")
                .withTableName(TABLE_NAME);
    }

    @Override
    public Member save(Member member) {
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
                .addValue("name", member.getName())
                .addValue("email", member.getEmail().getEmail())
                .addValue("role", member.getRole().name())
                .addValue("password", member.getPassword());
        Long id = simpleJdbcInsert.executeAndReturnKey(sqlParameterSource).longValue();
        return new Member(id, member.getName(), member.getEmail(), member.getRole(), member.getPassword());
    }

    @Override
    public Optional<Member> findByEmail(Email email) {
        try {
            String sql = "SELECT * FROM member WHERE email = ?";
            Member value = jdbcTemplate.queryForObject(sql, getMemberRowMapper(), email.getEmail());
            return Optional.ofNullable(value);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Member> findById(Long id) {
        try {
            String sql = "SELECT * FROM member WHERE id = ?";
            Member value = jdbcTemplate.queryForObject(sql, getMemberRowMapper(), id);
            return Optional.ofNullable(value);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Member> findAll() {
        String sql = "SELECT * FROM member";
        return jdbcTemplate.query(sql, getMemberRowMapper());
    }


    private RowMapper<Member> getMemberRowMapper() {
        return (rs, rowNum) -> {
            Long id = rs.getLong("id");
            Name name = new Name(rs.getString("name"));
            Email email = new Email(rs.getString("email"));
            Role role = Role.valueOf(rs.getString("role"));
            String password = rs.getString("password");
            return new Member(id, name, email, role, password);
        };
    }
}
