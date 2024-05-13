package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.RestAssured;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import roomescape.dao.RoomThemeDao;
import roomescape.domain.RoomTheme;
import roomescape.dto.request.RoomThemeRequest;
import roomescape.dto.response.RoomThemeResponse;
import roomescape.exception.TargetNotExistException;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RoomThemeServiceTest {
    @LocalServerPort
    private int port;

    @Autowired
    private RoomThemeService roomThemeService;
    @Autowired
    private RoomThemeDao roomThemeDao;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        List<RoomTheme> roomThemes = roomThemeDao.findAll();
        for (RoomTheme roomTheme : roomThemes) {
            roomThemeDao.deleteById(roomTheme.getId());
        }
    }

    @DisplayName("테마 저장")
    @Test
    void save() {
        // given
        RoomThemeRequest roomThemeRequest = new RoomThemeRequest("레벨2 탈출",
                "우테코 레벨2",
                "https://i.pinimg.com/236x/6e");
        // when
        RoomThemeResponse roomThemeResponse = roomThemeService.save(roomThemeRequest);
        // then
        assertAll(
                () -> assertThat(roomThemeResponse.name()).isEqualTo(roomThemeRequest.name()),
                () -> assertThat(roomThemeResponse.description()).isEqualTo(
                        roomThemeRequest.description()),
                () -> assertThat(roomThemeResponse.thumbnail()).isEqualTo(
                        roomThemeRequest.thumbnail())
        );
    }

    @DisplayName("모든 테마 조회")
    @Test
    void findAll() {
        // given & when
        List<RoomThemeResponse> roomThemeResponses = roomThemeService.findAll();
        // then
        assertThat(roomThemeResponses).isEmpty();
    }

    @DisplayName("테마 삭제")
    @Test
    void delete() {
        // given
        RoomThemeRequest roomThemeRequest = new RoomThemeRequest("레벨2 탈출",
                "우테코 레벨2",
                "https://i.pinimg.com/236x/6e");
        RoomThemeResponse roomThemeResponse = roomThemeService.save(roomThemeRequest);
        // when
        roomThemeService.deleteById(roomThemeResponse.id());
        // then
        assertThat(roomThemeService.findAll()).isEmpty();
    }

    @DisplayName("존재하지 않는 id의 대상을 삭제하려 하면 예외가 발생한다.")
    @Test
    void deleteByNotExistingId() {
        assertThatThrownBy(() -> roomThemeService.deleteById(-1L))
                .isInstanceOf(TargetNotExistException.class)
                .hasMessage("삭제할 테마가 존재하지 않습니다.");
    }
}
