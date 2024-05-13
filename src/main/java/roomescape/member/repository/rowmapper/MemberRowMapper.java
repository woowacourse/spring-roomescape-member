package roomescape.member.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import roomescape.member.domain.Member;

@Component
public class MemberRowMapper implements RowMapper<Member> {
    @Override
    public Member mapRow(final ResultSet resultSet, final int rowNumber) {
        try {
            return Member.of(
                    resultSet.getLong("member_id"),
                    resultSet.getString("member_name"),
                    resultSet.getString("email"),
                    resultSet.getString("member_role"),
                    resultSet.getString("password")
            );
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }
}
