package roomescape.auth.repository;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.auth.entity.Member;

import java.util.List;
import java.util.Optional;

@Repository
public class JdbcMemberRepository implements MemberRepository {
    private static final RowMapper<Member> rowMapper = (resultSet, rowNum) ->
            new Member(
                    resultSet.getLong("id"),
                    resultSet.getString("name"),
                    resultSet.getString("role"),
                    resultSet.getString("email"),
                    resultSet.getString("password"));
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public JdbcMemberRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
    }

    @Override
    public List<Member> findAll() {
        String query = "SELECT * FROM member";
        return jdbcTemplate.query(query, rowMapper);
    }

    // TODO: (모든 Repository, Service에 적용) try-catch 삭제 및 Service에서 DataAccessException 핸들링하도록
    @Override
    public Optional<Member> findById(Long memberId) {
        String query = "SELECT * FROM member WHERE id = :memberId";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("memberId", memberId);

        try {
            Member member = jdbcTemplate.queryForObject(query, params, rowMapper);
            return Optional.of(member);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    // TODO: (모든 Repository, Service에 적용) try-catch 삭제 및 Service에서 DataAccessException 핸들링하도록
    @Override
    public Optional<Member> findByEmail(String email) {
        String query = "SELECT * FROM member WHERE email = :email";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("email", email);
        try {
            Member member = jdbcTemplate.queryForObject(query, params, rowMapper);
            return Optional.of(member);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Member save(Member member) {
        String query = "INSERT INTO member (name, email, role, password) VALUES (:name, :email, :role, :password)";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", member.getName())
                .addValue("email", member.getEmail())
                .addValue("role", member.getRole().name())
                .addValue("password", member.getPassword());
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(query, params, keyHolder);
        final long id = keyHolder.getKey().longValue();
        return new Member(
                id,
                member.getName(),
                member.getRole().name(),
                member.getEmail(),
                member.getPassword()
        );
    }
}
