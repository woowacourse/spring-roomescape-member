package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import roomescape.dao.ReservationDao;
import roomescape.dao.ReservationTimeDao;
import roomescape.dao.ThemeDao;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.dto.request.ReservationRequest;
import roomescape.dto.response.ReservationResponse;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Transactional
class ReservationServiceTest {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private ReservationTimeDao reservationTimeDao;

    @Autowired
    private ThemeDao themeDao;

    @Autowired
    private ReservationDao reservationDao;

    @Test
    void 예약을_추가한다() {
        // given
        ReservationTime time = saveTime(10, 0);
        Theme theme = saveTheme("방탈출1", "설명", "https://thumb.com");
        ReservationRequest request = new ReservationRequest("브라운", LocalDate.of(2026, 5, 5), time.getId(),
                theme.getId());

        // when
        ReservationResponse response = reservationService.addReservation(request);

        // then
        assertThat(response)
                .extracting(ReservationResponse::name, ReservationResponse::date)
                .containsExactly("브라운", LocalDate.of(2026, 5, 5));
    }

    @Test
    void 존재하지_않는_시간으로_예약하면_예외가_발생한다() {
        // given
        Theme theme = saveTheme("방탈출1", "설명", "https://thumb.com");
        ReservationRequest request = new ReservationRequest("브라운", LocalDate.of(2026, 5, 5), 999L, theme.getId());

        // when & then
        assertThatThrownBy(() -> reservationService.addReservation(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 시간입니다.");
    }

    @Test
    void 존재하지_않는_테마로_예약하면_예외가_발생한다() {
        // given
        ReservationTime time = saveTime(10, 0);
        ReservationRequest request = new ReservationRequest("브라운", LocalDate.of(2026, 5, 5), time.getId(), 999L);

        // when & then
        assertThatThrownBy(() -> reservationService.addReservation(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 테마입니다.");
    }

    @Test
    void 전체_예약을_조회한다() {
        // given
        ReservationTime time = saveTime(10, 0);
        Theme theme = saveTheme("방탈출1", "설명", "https://thumb.com");
        saveReservation("브라운", LocalDate.of(2026, 5, 5), time, theme);
        saveReservation("로지", LocalDate.of(2026, 5, 6), time, theme);

        // when
        List<ReservationResponse> responses = reservationService.getAllReservations();

        // then
        assertThat(responses).hasSize(2);
        assertThat(responses).extracting(ReservationResponse::name).containsExactly("브라운", "로지");
    }

    @Test
    void 예약을_삭제한다() {
        // given
        ReservationTime time = saveTime(10, 0);
        Theme theme = saveTheme("방탈출1", "설명", "https://thumb.com");
        Reservation saved = saveReservation("브라운", LocalDate.of(2026, 5, 5), time, theme);

        // when & then
        assertThatNoException().isThrownBy(() -> reservationService.delete(saved.getId()));
    }

    @Test
    void 존재하지_않는_예약을_삭제하면_예외가_발생한다() {
        // when & then
        assertThatThrownBy(() -> reservationService.delete(999L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 예약입니다.");
    }

    private ReservationTime saveTime(int hour, int minute) {
        return reservationTimeDao.insert(ReservationTime.createWithoutId(LocalTime.of(hour, minute)));
    }

    private Theme saveTheme(String name, String description, String thumbnail) {
        return themeDao.insert(Theme.createWithoutId(name, description, thumbnail));
    }

    private Reservation saveReservation(String name, LocalDate date, ReservationTime time, Theme theme) {
        return reservationDao.insert(Reservation.createWithoutId(name, date, time, theme));
    }
}
