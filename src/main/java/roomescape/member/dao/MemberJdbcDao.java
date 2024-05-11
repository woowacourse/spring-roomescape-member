package roomescape.member.dao;

import java.util.List;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.member.domain.Member;
import roomescape.member.dto.MemberRegistrationInfo;

@Repository
public class MemberJdbcDao implements MemberDao {

    public static final RowMapper<MemberRegistrationInfo> MEMBER_REGISTRATION_INFO_ROW_MAPPER = (resultSet, rowNum)
            -> new MemberRegistrationInfo(
            resultSet.getString("name"),
            resultSet.getString("email"),
            resultSet.getString("password")
    );

    private static final RowMapper<Member> MEMBER_ROW_MAPPER = (resultSet, rowNum)
            -> new Member(
            resultSet.getLong("id"),
            resultSet.getString("email"),
            resultSet.getString("password"),
            resultSet.getString("name")

    );

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public MemberJdbcDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.jdbcInsert = new SimpleJdbcInsert(dataSource).withTableName("registration")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public Member save(Member member) {
        SqlParameterSource sqlParameterSource = new BeanPropertySqlParameterSource(member);
        long id = jdbcInsert.executeAndReturnKey(sqlParameterSource)
                .longValue();
        return member.toIdAssigned(id);
    }

    @Override
    public List<Member> findAll() {
        String findAllSql = "SELECT * FROM registration";
        return jdbcTemplate.query(findAllSql, MEMBER_ROW_MAPPER);
    }

    @Override
    public MemberRegistrationInfo findRegistrationInfoByEmail(String email) {
        String findRegistrationInfSql = "SELECT * FROM registration WHERE email = ?";
        return jdbcTemplate.queryForObject(findRegistrationInfSql, MEMBER_REGISTRATION_INFO_ROW_MAPPER, email);
    }

    @Override
    public void delete(long id) {
        String deleteSql = "DELETE FROM registration WHERE id = ?";
        jdbcTemplate.update(deleteSql, id);
    }

}
