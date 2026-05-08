package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import roomescape.dao.ReservationTimeDao;
import roomescape.dao.ThemeDao;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.dto.request.ReservationRequest;
import roomescape.dto.response.ReservationResponse;
import roomescape.exception.code.ReservationErrorCode;
import roomescape.exception.code.ReservationTimeErrorCode;
import roomescape.exception.code.ThemeErrorCode;
import roomescape.exception.domain.ReservationException;
import roomescape.exception.domain.ReservationTimeException;
import roomescape.exception.domain.ThemeException;

@SpringBootTest(webEnvironment = WebEnvironment.NONE)
class ReservationServiceTest {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private ReservationTimeDao reservationTimeDao;

    @Autowired
    private ThemeDao themeDao;

    @Test
    void 예약을_생성할_수_있다() {
        // given
        ReservationTime reservationTime = saveReservationTime(LocalTime.of(13, 0));
        Theme theme = saveTheme("테마1");

        ReservationRequest request = createReservationRequest(reservationTime.getId(), theme.getId());

        // when
        ReservationResponse response = reservationService.create(request);

        // then
        assertThat(response)
                .extracting(
                        ReservationResponse::name,
                        ReservationResponse::date,
                        reservationResponse -> reservationResponse.time().id()
                )
                .containsExactly(
                        request.name(),
                        request.date(),
                        request.timeId()
                );
    }

    @Test
    void 예약_생성시_예약시간이_존재하지_않으면_예외가_발생한다() {
        // given
        Theme theme = saveTheme("테마1");
        ReservationRequest request = createReservationRequest(0L, theme.getId());

        // when & then
        assertThatThrownBy(() -> reservationService.create(request))
                .isInstanceOf(ReservationTimeException.class)
                .hasMessage(ReservationTimeErrorCode.RESERVATION_TIME_NOT_FOUND.getMessage());
    }

    @Test
    void 예약_생성시_테마가_존재하지_않으면_예외가_발생한다() {
        // given
        ReservationTime reservationTime = saveReservationTime(LocalTime.of(13, 0));
        ReservationRequest request = createReservationRequest(reservationTime.getId(), 0L);

        // when & then
        assertThatThrownBy(() -> reservationService.create(request))
                .isInstanceOf(ThemeException.class)
                .hasMessage(ThemeErrorCode.THEME_NOT_FOUND.getMessage());
    }

    @Test
    void 예약을_삭제할_수_있다() {
        // given
        ReservationTime reservationTime = saveReservationTime(LocalTime.of(13, 0));
        Theme theme = saveTheme("테마1");

        ReservationRequest request = createReservationRequest(reservationTime.getId(), theme.getId());
        ReservationResponse response = reservationService.create(request);

        int beforeSize = reservationService.getReservations().size();

        // when
        reservationService.delete(response.id());

        // then
        List<ReservationResponse> reservations = reservationService.getReservations();
        assertAll(
                () -> assertThat(reservations).hasSize(beforeSize - 1),
                () -> assertThat(reservations)
                        .extracting(ReservationResponse::id)
                        .doesNotContain(response.id())
        );
    }

    @Test
    void 삭제할_예약이_존재하지_않으면_예외를_반환한다() {
        // when & then
        assertThatThrownBy(() -> reservationService.delete(0L))
                .isInstanceOf(ReservationException.class)
                .hasMessage(ReservationErrorCode.RESERVATION_NOT_FOUND.getMessage());
    }

    private ReservationTime saveReservationTime(LocalTime startAt) {
        ReservationTime reservationTime = ReservationTime.createWithoutId(startAt);
        return reservationTimeDao.save(reservationTime);
    }

    private Theme saveTheme(String name) {
        Theme theme = Theme.createWithoutId(name, "설명", "https://adsf.dsaf");
        return themeDao.save(theme);
    }

    private ReservationRequest createReservationRequest(long timeId, long themeId) {
        return new ReservationRequest(
                "예약1",
                LocalDate.of(2025, 5, 8),
                timeId,
                themeId
        );
    }
}
