package roomescape.user.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import roomescape.user.dto.response.UserSelectElementResponse;

@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    void 모든_사용자_목록_조회_성공() {
        // when
        List<UserSelectElementResponse> users = userService.getAllUsers();

        // then
        assertThat(users).hasSize(6);
    }
}