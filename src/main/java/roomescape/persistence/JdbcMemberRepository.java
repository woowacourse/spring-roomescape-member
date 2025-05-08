package roomescape.persistence;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.domain.Member;
import roomescape.domain.MemberRepository;
import roomescape.persistence.query.CreateMemberQuery;

import java.sql.PreparedStatement;
import java.util.Optional;

@Repository
public class JdbcMemberRepository implements MemberRepository {

    public static final RowMapper<Member> userRowMapper = (rs, rowNum) ->
            new Member(rs.getLong("id"),
                    rs.getString("name"),
                    rs.getString("role"),
                    rs.getString("email"),
                    rs.getString("password")
            );

    private final JdbcTemplate jdbcTemplate;

    public JdbcMemberRepository(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Member> findByEmailAndPassword(final String email, final String password) {
        String sql = "SELECT id, role, name, email, password FROM member WHERE email = ? and password = ?";
        try {
            Member member = jdbcTemplate.queryForObject(sql, userRowMapper, email, password);
            return Optional.of(member);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Member> findById(final Long id) {
        String sql = "SELECT id, role, name, email, password FROM member WHERE id = ?";
        try {
            Member member = jdbcTemplate.queryForObject(sql, userRowMapper, id);
            return Optional.of(member);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Long create(final CreateMemberQuery createMemberQuery) {
        String sql = "INSERT INTO member (name, email, password) VALUES (?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, createMemberQuery.name());
            ps.setString(2, createMemberQuery.email());
            ps.setString(3, createMemberQuery.password());
            return ps;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }
}
