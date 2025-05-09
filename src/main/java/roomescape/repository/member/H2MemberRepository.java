package roomescape.repository.member;

import java.util.List;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import roomescape.domain.Member;
import roomescape.domain.MemberRole;

@Repository
public class H2MemberRepository implements MemberRepository {

    private static final RowMapper<Member> mapper;
    private final JdbcTemplate template;

    static {
        mapper = (resultSet, resultNumber) -> new Member(
                resultSet.getLong("id"),
                resultSet.getString("name"),
                resultSet.getString("email"),
                resultSet.getString("password"),
                MemberRole.valueOf(resultSet.getString("role"))
        );
    }

    public H2MemberRepository(JdbcTemplate template) {
        this.template = template;
    }

    @Override
    public List<Member> findAllByRole(MemberRole role) {
        String sql = """
                SELECT *
                FROM member
                WHERE member.role LIKE ?
                ORDER BY member.id ASC
                """;
        return template.query(sql, mapper, role.toString());
    }

    @Override
    public Optional<Member> findById(long id) {
        String sql = """
                SELECT *
                FROM member
                WHERE member.id = ?
                """;
        try {
            Member member = template.queryForObject(sql, mapper, id);
            return Optional.of(member);
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Member> findByEmail(String email) {
        String sql = "SELECT * FROM member WHERE member.email = ?";
        try {
            Member member = template.queryForObject(sql, mapper, email);
            return Optional.of(member);
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }
}
