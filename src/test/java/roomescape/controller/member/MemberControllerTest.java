package roomescape.controller.member;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MemberControllerTest {

    @Autowired
    MemberController memberController;

    @Test
    @DisplayName("멤버 조회")
    void getMembers() {
        assertThat(memberController.getMembers()).hasSize(3);
    }
}
