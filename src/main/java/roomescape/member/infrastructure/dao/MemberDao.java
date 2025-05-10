package roomescape.member.infrastructure.dao;

import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import roomescape.member.application.repository.MemberRepository;
import roomescape.member.domain.Member;
import roomescape.member.domain.Role;

@Repository
public class MemberDao implements MemberRepository {

    private final JdbcTemplate jdbcTemplate;

    public MemberDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Member> findByEmail(String email) {
        String sql = """
                SELECT id, name, email, password, role FROM `member`
                WHERE email = ?
                """;
        try {
            Member member = jdbcTemplate.queryForObject(sql,
                    (resultSet, rowNum) -> {
                        Role role = Role.valueOf(resultSet.getString("role"));
                        return new Member(
                                resultSet.getLong("id"),
                                resultSet.getString("name"),
                                resultSet.getString("email"),
                                resultSet.getString("password"),
                                role
                        );
                    }, email);
            return Optional.ofNullable(member);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}
