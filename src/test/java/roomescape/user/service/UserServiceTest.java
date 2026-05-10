package roomescape.user.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import roomescape.user.dto.UserRequest;
import roomescape.user.dto.UserResponse;
import roomescape.user.repository.UserRepository;

@SpringBootTest
@Transactional
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Test
    void 유저를_생성하면_DB에_실제로_저장되다() {
        UserRequest request = new UserRequest("루크");

        UserResponse response = userService.create(request);

        assertThat(response.getId()).isNotNull();
        assertThat(response.getName()).isEqualTo("루크");
    }
}
