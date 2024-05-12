package roomescape.member.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import roomescape.member.domain.Email;
import roomescape.member.domain.Member;
import roomescape.member.domain.MemberName;
import roomescape.member.domain.Password;
import roomescape.member.domain.Role;

@Repository
public class MemberH2Repository implements MemberRepository {

    private final JdbcTemplate jdbcTemplate;

    public MemberH2Repository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Member> findById(Long id) {
        try {
            Member member = jdbcTemplate.queryForObject(
                    "SELECT * FROM member WHERE id = ?",
                    getMemberRowMapper(),
                    id
            );
            return Optional.ofNullable(member);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    private RowMapper<Member> getMemberRowMapper() {
        return (resultSet, rowNum) -> new Member(
                resultSet.getLong("id"),
                new MemberName(resultSet.getString("name")),
                new Email(resultSet.getString("email")),
                new Password(resultSet.getString("password")),
                Role.findByDbValue(resultSet.getString("role"))
        );
    }

    @Override
    public Optional<Member> findByEmail(Email email) {
        try {
            Member member = jdbcTemplate.queryForObject(
                    "SELECT * FROM member WHERE email = ?",
                    getMemberRowMapper(),
                    email.email()
            );
            return Optional.ofNullable(member);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Member> findAll() {
        return jdbcTemplate.query(
                "SELECT * FROM member",
                getMemberRowMapper()
        );
    }

    @Override
    public void delete(Long id) {
        jdbcTemplate.update("DELETE FROM member WHERE id = ?", id);
    }
}
