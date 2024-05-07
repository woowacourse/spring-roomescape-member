package roomescape.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import roomescape.dao.ReservationDao;
import roomescape.dao.ThemeDao;
import roomescape.dao.TimeDao;
import roomescape.domain.theme.Theme;
import roomescape.domain.time.Time;
import roomescape.dto.reservation.ReservationRequest;
import roomescape.global.exception.model.DataConflictException;
import roomescape.global.exception.model.ValidateException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@JdbcTest
@Sql(scripts = "/truncate.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
@Import({TimeDao.class, ThemeDao.class, ReservationService.class, ReservationDao.class})
class ReservationServiceTest {

    @Autowired
    TimeDao timeDao;
    @Autowired
    ThemeDao themeDao;
    @Autowired
    private ReservationService reservationService;

    @Test
    @DisplayName("동일한 날짜와 시간과 테마에 예약을 생성하면 예외가 발생한다")
    void duplicateTimeReservationAddFail() {
        // given
        Time time = timeDao.insert(new Time(LocalTime.of(12, 30)));
        Theme theme = themeDao.insert(new Theme("테마명", "설명", "썸네일URL"));

        // when & then
        reservationService.addReservation(
                new ReservationRequest("예약", LocalDate.now().plusDays(1L), time.getId(), theme.getId()));

        assertThatThrownBy(() -> reservationService.addReservation(
                new ReservationRequest("예약", LocalDate.now().plusDays(1L), time.getId(), theme.getId())))
                .isInstanceOf(DataConflictException.class);
    }

    @Test
    @DisplayName("이미 지난 날짜로 예약을 생성하면 예외가 발생한다")
    void beforeDateReservationFail() {
        // given
        Time time = timeDao.insert(new Time(LocalTime.of(12, 30)));
        Theme theme = themeDao.insert(new Theme("테마명", "설명", "썸네일URL"));
        LocalDate beforeDate = LocalDate.now().minusDays(1L);

        // when & then
        assertThatThrownBy(() -> reservationService.addReservation(
                new ReservationRequest("예약", beforeDate, time.getId(), theme.getId())))
                .isInstanceOf(ValidateException.class);
    }

    @Test
    @DisplayName("현재 날짜가 예약 당일이지만, 이미 지난 시간으로 예약을 생성하면 예외가 발생한다")
    void beforeTimeReservationFail() {
        // given
        LocalDateTime beforeTime = LocalDateTime.now().minusHours(1L);
        Time time = timeDao.insert(new Time(beforeTime.toLocalTime()));
        Theme theme = themeDao.insert(new Theme("테마명", "설명", "썸네일URL"));

        // when & then
        assertThatThrownBy(() -> reservationService.addReservation(
                new ReservationRequest("예약", beforeTime.toLocalDate(), time.getId(), theme.getId())))
                .isInstanceOf(ValidateException.class);
    }
}
