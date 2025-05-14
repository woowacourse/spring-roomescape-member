package roomescape.repository;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.model.Member;
import roomescape.model.MemberName;
import roomescape.model.Role;

@Repository
public class JdbcMemberRepository implements MemberRepository {
    private final JdbcTemplate jdbcTemplate;

    private static final RowMapper<Member> MEMBER_ROW_MAPPER = (rs, rowNum) -> {
        MemberName name = new MemberName(rs.getString("name"));
        return new Member(
                rs.getLong("id"),
                Role.valueOf(rs.getString("role")),
                name,
                rs.getString("email"),
                rs.getString("password")
        );
    };

    public JdbcMemberRepository(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Member> findByEmail(final String email) {
        String sql = "SELECT * FROM member WHERE email = ?";
        return jdbcTemplate.query(sql, MEMBER_ROW_MAPPER, email)
                .stream()
                .findFirst();
    }

    @Override
    public Optional<Member> findById(final Long id) {
        String sql = "SELECT * FROM member WHERE id = ?";
        return jdbcTemplate.query(sql, MEMBER_ROW_MAPPER, id)
                .stream()
                .findFirst();
    }

    @Override
    public List<Member> findAll() {
        String sql = "SELECT * FROM member";
        return jdbcTemplate.query(sql, MEMBER_ROW_MAPPER);
    }

    @Override
    public Member saveMember(final Member member) {
        String sql = "insert into member(role,name,email,password) values (?,?,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    sql, new String[]{"id"});
            ps.setString(1, member.getRole().toString());
            ps.setString(2, member.getMemberName().getName());
            ps.setString(3, member.getEmail());
            ps.setString(4, member.getPassword());
            return ps;
        }, keyHolder);
        return new Member(
                Objects.requireNonNull(keyHolder.getKey()).longValue(),
                member.getRole(),
                member.getMemberName(),
                member.getEmail(),
                member.getPassword()
        );
    }
}
