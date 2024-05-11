package roomescape.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import roomescape.domain.Member;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Import(MemberDao.class)
@Sql(scripts = {"/test_schema.sql", "/test_data.sql"})
public class MemberDaoTest {

    @Autowired
    private MemberDao memberDao;

    @Test
    void insertTest() {
        String name = "name";
        String email = "email@email.com";
        String password = "password";

        Long id = memberDao.insert(name, email, password);

        assertThat(id).isNotNull();
    }

    @Test
    void findByEmailTest() {
        Optional<Member> user = memberDao.findByEmail("email@email.com");

        assertThat(user.isPresent()).isTrue();
    }

    @Test
    void findByIdTest() {
        Optional<Member> user = memberDao.findById(1L);

        assertThat(user.isPresent()).isTrue();
    }
}
