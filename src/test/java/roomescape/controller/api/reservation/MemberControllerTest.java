package roomescape.controller.api.reservation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import roomescape.domain.Member;
import roomescape.repository.member.MemberRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Sql("/initial_test_data.sql")
class MemberControllerTest {

    @Autowired
    private MemberController memberController;
    @Autowired
    private MemberRepository memberRepository;

    @DisplayName("회원 정보들을 가져온다.")
    @Test
    void findAllTest() {
        List<Member> memberList = memberRepository.findAll();
        assertThat(memberController.findAll()).hasSize(memberList.size());
    }
}