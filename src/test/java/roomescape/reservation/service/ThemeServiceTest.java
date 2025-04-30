package roomescape.reservation.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.NoSuchElementException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import roomescape.common.BaseTest;
import roomescape.reservation.fixture.ReservationDbFixture;
import roomescape.reservation.fixture.ReservationTimeDbFixture;
import roomescape.reservation.fixture.ThemeDbFixture;
import roomescape.theme.controller.request.ThemeCreateRequest;
import roomescape.theme.controller.response.ThemeResponse;
import roomescape.theme.domain.Theme;
import roomescape.theme.service.ThemeService;
import roomescape.time.domain.ReservationTime;

public class ThemeServiceTest extends BaseTest {

    @Autowired
    private ReservationDbFixture reservationDbFixture;

    @Autowired
    private ReservationTimeDbFixture reservationTimeDbFixture;

    @Autowired
    private ThemeDbFixture themeDbFixture;
    @Autowired
    private ThemeService themeService;

    @Test
    void 테마를_생성한다() {
        ThemeCreateRequest request = new ThemeCreateRequest("공포", "공포 테마", "공포.jpg");

        ThemeResponse response = themeService.create(request);

        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.name()).isEqualTo("공포");
        assertThat(response.description()).isEqualTo("공포 테마");
        assertThat(response.thumbnail()).isEqualTo("공포.jpg");
    }

    @Test
    void 테마를_조회한다() {
        Theme theme = themeDbFixture.공포();
        ThemeResponse response = themeService.getAll().get(0);

        assertThat(response.id()).isEqualTo(theme.getId());
        assertThat(response.name()).isEqualTo(theme.getName());
        assertThat(response.description()).isEqualTo(theme.getDescription());
        assertThat(response.thumbnail()).isEqualTo(theme.getThumbnail());

    }

    @Test
    void 테마를_삭제한다() {
        themeDbFixture.공포();
        themeService.deleteById(1L);

        assertThat(themeService.getAll()).isEmpty();
    }

    @Test
    void 이미_해당_테마의_예약이_존재한다면_삭제할_수_없다() {
        Theme theme = themeDbFixture.공포();
        ReservationTime reservationTime = reservationTimeDbFixture.예약시간_10시();
        reservationDbFixture.예약_한스_25_4_22_10시_공포(reservationTime, theme);

        assertThatThrownBy(() -> themeService.deleteById(theme.getId()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 존재하지_않는_테마를_삭제할_수_없다() {
        assertThatThrownBy(() -> themeService.deleteById(3L))
                .isInstanceOf(NoSuchElementException.class);
    }
}
