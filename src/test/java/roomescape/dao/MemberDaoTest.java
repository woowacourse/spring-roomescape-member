package roomescape.dao;

import org.junit.jupiter.api.Test;
import roomescape.domain.Member;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class MemberDaoTest {

    private final MemberDao memberDao = new MemberDao();

    @Test
    void insertTest() {
        String name = "name";
        String email = "email@email.com";
        String password = "password";

        Member member = memberDao.insert(name, email, password);

        assertThat(member).isNotNull();
    }

    @Test
    void findByEmailTest() {
        memberDao.insert("name", "email@email.com", "password");

        Optional<Member> user = memberDao.findByEmail("email@email.com");

        assertThat(user.isPresent()).isTrue();
    }
}
