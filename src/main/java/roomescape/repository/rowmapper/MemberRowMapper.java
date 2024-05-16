package roomescape.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import roomescape.domain.Member;

@Component
public class MemberRowMapper implements RowMapper<Member> {

    @Override
    public Member mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        return new Member(
                resultSet.getLong("id"),
                resultSet.getString("name"),
                resultSet.getString("role"),
                resultSet.getString("email"),
                resultSet.getString("password")
        );
    }
}
