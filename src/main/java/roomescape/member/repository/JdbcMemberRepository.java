package roomescape.member.repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.common.utils.JdbcUtils;
import roomescape.member.domain.Member;
import roomescape.member.domain.MemberEmail;
import roomescape.member.domain.MemberId;
import roomescape.member.domain.MemberName;
import roomescape.member.domain.MemberPassword;
import roomescape.member.domain.Role;

@Repository
@RequiredArgsConstructor
public class JdbcMemberRepository implements MemberRepository {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Member> memberRowMapper = (resultSet, rowNum) -> Member.withId(
            MemberId.from(resultSet.getLong("id")),
            MemberName.from(resultSet.getString("name")),
            MemberEmail.from(resultSet.getString("email")),
            MemberPassword.from(resultSet.getString("password")),
            Role.valueOf(resultSet.getString("role"))
    );

    @Override
    public boolean existsByEmail(MemberEmail email) {
        final String sql = """
                select exists
                    (select 1 from member where email = ?)
                """;

        return Boolean.TRUE.equals(
                jdbcTemplate.queryForObject(sql, Boolean.class, email.getValue()));
    }

    @Override
    public Optional<Member> findByParams(MemberEmail email, MemberPassword password) {
        final String sql = """
                SELECT id, name, email, password, role
                FROM member
                WHERE email = ? AND password = ?
                """;

        return JdbcUtils.queryForOptional(jdbcTemplate, sql, memberRowMapper, email.getValue(), password.getValue());
    }

    @Override
    public Member save(Member member) {
        final String sql = """
                INSERT INTO member (name, email, password, role) VALUES (?, ?, ?, ?)
                """;
        final KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            final PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, member.getName().getValue());
            preparedStatement.setString(2, member.getEmail().getValue());
            preparedStatement.setString(3, member.getPassword().getValue());
            preparedStatement.setString(4, member.getRole().name());

            return preparedStatement;
        }, keyHolder);

        final long generatedId = Objects.requireNonNull(keyHolder.getKey()).longValue();

        return Member.withId(
                MemberId.from(generatedId),
                member.getName(),
                member.getEmail(),
                member.getPassword(),
                member.getRole());
    }
}
