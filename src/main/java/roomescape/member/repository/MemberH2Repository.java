package roomescape.member.repository;

import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
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
    public boolean doesEmailExist(Email email) {
        String sql = "SELECT * FROM member WHERE email = ?";
        return !jdbcTemplate.query(sql, (rs, rowNum) -> 0, email.email()).isEmpty();
    }

    // TODO: 비밀번호 보호를 위해 Member 말고 LoginMember를 반환하는 방향으로 리팩토링하기
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
    public Optional<Member> findByEmailAndPassword(Email email, Password password) {
        try {
            Member member = jdbcTemplate.queryForObject(
                    "SELECT * FROM member WHERE email = ? AND password = ?",
                    getMemberRowMapper(),
                    email.email(),
                    password.password()
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
