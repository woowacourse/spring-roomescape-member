package roomescape.common.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import roomescape.domain.member.Member;
import roomescape.domain.member.Role;

public class MemberMapper implements RowMapper<Member> {
    @Override
    public Member mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        return new Member(
                resultSet.getLong("id"),
                resultSet.getString("name"),
                resultSet.getString("email"),
                Role.valueOf(resultSet.getString("role"))
        );
    }
}
