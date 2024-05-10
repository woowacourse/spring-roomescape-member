package roomescape.repository.rowmapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import roomescape.domain.member.Member;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class MemberRowMapper implements RowMapper<Member> {
    @Override
    public Member mapRow(ResultSet resultSet, int rowNum) {
        try {
            return new Member(
                    resultSet.getLong("id"),
                    resultSet.getString("member_name"),
                    resultSet.getString("email"),
                    resultSet.getString("password"),
                    resultSet.getString("role")
            );
        } catch (SQLException exception) {
            throw new RuntimeException("[ERROR] member 테이블 접근 오류", exception);
        }
    }
}
