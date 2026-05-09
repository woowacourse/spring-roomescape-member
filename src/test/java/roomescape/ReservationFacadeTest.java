package roomescape;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.dto.ReservationRequest;
import roomescape.facade.ReservationFacade;
import roomescape.service.ReservationTimeService;
import roomescape.service.ThemeService;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ReservationFacadeTest {

    @Autowired
    private ReservationFacade reservationFacade;

    @Autowired
    private ReservationTimeService reservationTimeService;

    @Autowired
    private ThemeService themeService;

    @Test
    void 사용중인_시간_삭제시_시간_관련_메시지와_ID가_포함된다() {
        ReservationTime time = reservationTimeService.addTime(new ReservationTime(null, LocalTime.of(10, 0)));
        Theme theme = themeService.addTheme(new Theme(null, "공포", "무서운 테마", "https://example.com/horror.jpg"));
        reservationFacade.addReservation(new ReservationRequest(
                "브라운", LocalDate.of(2026, 8, 5), time.getId(), theme.getId()));

        assertThatThrownBy(() -> reservationFacade.deleteTime(time.getId()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("시간")
                .hasMessageContaining(String.valueOf(time.getId()));
    }

    @Test
    void 사용중인_테마_삭제시_테마_관련_메시지와_ID가_포함된다() {
        ReservationTime time = reservationTimeService.addTime(new ReservationTime(null, LocalTime.of(10, 0)));
        Theme theme = themeService.addTheme(new Theme(null, "공포", "무서운 테마", "https://example.com/horror.jpg"));
        reservationFacade.addReservation(new ReservationRequest(
                "브라운", LocalDate.of(2026, 8, 5), time.getId(), theme.getId()));

        assertThatThrownBy(() -> reservationFacade.deleteTheme(theme.getId()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("테마")
                .hasMessageContaining(String.valueOf(theme.getId()));
    }

    @Test
    void 시간_삭제_실패_메시지와_테마_삭제_실패_메시지는_서로_다르다() {
        ReservationTime time = reservationTimeService.addTime(new ReservationTime(null, LocalTime.of(10, 0)));
        Theme theme = themeService.addTheme(new Theme(null, "공포", "무서운 테마", "https://example.com/horror.jpg"));
        reservationFacade.addReservation(new ReservationRequest(
                "브라운", LocalDate.of(2026, 8, 5), time.getId(), theme.getId()));

        String timeMessage = catchMessage(() -> reservationFacade.deleteTime(time.getId()));
        String themeMessage = catchMessage(() -> reservationFacade.deleteTheme(theme.getId()));

        org.assertj.core.api.Assertions.assertThat(timeMessage).isNotEqualTo(themeMessage);
    }

    private String catchMessage(Runnable runnable) {
        try {
            runnable.run();
            return null;
        } catch (RuntimeException e) {
            return e.getMessage();
        }
    }
}
