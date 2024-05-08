package roomescape.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.Member;
import roomescape.domain.MemberRepository;
import roomescape.domain.Role;

@Repository
public class MemberJdbcRepository implements MemberRepository {

    private static final RowMapper<Member> MEMBER_ROW_MAPPER = (resultSet, rowNum) -> new Member(
            resultSet.getLong("id"),
            resultSet.getString("name"),
            resultSet.getString("email"),
            resultSet.getString("password"),
            Role.valueOf(resultSet.getString("role"))
    );

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public MemberJdbcRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("member")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public Member save(Member member) {
        final BeanPropertySqlParameterSource memberParameters = new BeanPropertySqlParameterSource(member);
        final Long savedThemeId = jdbcInsert.executeAndReturnKey(memberParameters).longValue();

        return new Member(savedThemeId, member.getName(), member.getEmail(), member.getPassword(), member.getRole());
    }

    @Override
    public Optional<Member> findById(Long id) {
        final String sql = "SELECT * FROM member WHERE id = ?";
        try {
            final Member member = jdbcTemplate.queryForObject(sql, MEMBER_ROW_MAPPER, id);
            return Optional.ofNullable(member);
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    @Override
    public List<Member> findAll() {
        final String sql = "SELECT * FROM member";
        return jdbcTemplate.query(sql, MEMBER_ROW_MAPPER);
    }

    @Override
    public boolean existByEmailAndPassword(String email, String password) {
        String sql = """
                SELECT
                CASE WHEN EXISTS (
                        SELECT 1
                        FROM member
                        WHERE email = ? AND password = ?
                    )
                    THEN TRUE
                    ELSE FALSE
                END""";

        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, Boolean.class, email, password));
    }

    @Override
    public Optional<Member> findByEmail(String email) {
        final String sql = "SELECT * FROM member WHERE email = ?";
        try {
            final Member member = jdbcTemplate.queryForObject(sql, MEMBER_ROW_MAPPER, email);
            return Optional.ofNullable(member);
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }
}
