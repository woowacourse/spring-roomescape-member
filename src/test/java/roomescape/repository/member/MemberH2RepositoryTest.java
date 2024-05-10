package roomescape.repository.member;

import static org.assertj.core.api.Assertions.assertThat;
import static roomescape.InitialMemberFixture.MEMBER_1;
import static roomescape.InitialMemberFixture.MEMBER_2;
import static roomescape.InitialMemberFixture.MEMBER_3;
import static roomescape.InitialMemberFixture.MEMBER_4;
import static roomescape.InitialMemberFixture.NOT_SAVED_MEMBER;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import roomescape.domain.member.Member;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Sql(scripts = {"/schema.sql", "/initial_test_data.sql"})
class MemberH2RepositoryTest {

    @Autowired
    private MemberH2Repository memberH2Repository;

    @Test
    @DisplayName("Member를 저장할 때 id를 생성해서 반환한다.")
    void save() {
        Member saved = memberH2Repository.save(NOT_SAVED_MEMBER);

        assertThat(saved.getId()).isNotNull();
    }

    @Test
    @DisplayName("특정 email이 존재하면 true를 반환한다.")
    void trueIfExist() {
        boolean doesExist = memberH2Repository.doesEmailExist(MEMBER_1.getEmail());

        assertThat(doesExist).isTrue();
    }

    @Test
    @DisplayName("특정 email이 존재하지 않으면 false를 반환한다.")
    void falseIfNotExist() {
        boolean doesExist = memberH2Repository.doesEmailExist(NOT_SAVED_MEMBER.getEmail());

        assertThat(doesExist).isFalse();
    }

    @Test
    @DisplayName("모든 Member을 찾는다.")
    void findAll() {
        List<Member> found = memberH2Repository.findAll();

        assertThat(found).containsExactly(MEMBER_1, MEMBER_2, MEMBER_3, MEMBER_4);
    }

    @Test
    @DisplayName("id에 맞는 Member을 찾는다.")
    void findBy() {
        Member found = memberH2Repository.findById(MEMBER_1.getId()).get();

        assertThat(found).isEqualTo(MEMBER_1);
    }

    @Test
    @DisplayName("존재하지 않는 id가 들어오면 빈 Optional 객체를 반환한다.")
    void findEmpty() {
        Optional<Member> member = memberH2Repository.findById(-1L);

        assertThat(member.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("id에 맞는 Member을 제거한다.")
    void delete() {
        memberH2Repository.delete(NOT_SAVED_MEMBER.getId());

        assertThat(memberH2Repository.findById(NOT_SAVED_MEMBER.getId()).isEmpty()).isTrue();
    }
}
