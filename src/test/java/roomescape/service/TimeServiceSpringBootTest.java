package roomescape.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.theme.Theme;
import roomescape.domain.time.Time;
import roomescape.dto.time.TimeRequest;
import roomescape.global.exception.ApplicationException;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ThemeRepository;
import roomescape.repository.TimeRepository;

@SpringBootTest(webEnvironment = WebEnvironment.NONE)
class TimeServiceSpringBootTest {

    @Autowired
    private TimeService timeService;

    @Autowired
    private TimeRepository timeRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ThemeRepository themeRepository;

    @Test
    @DisplayName("중복된 예약 시간을 등록하는 경우 예외가 발생한다.")
    void duplicateTimeFail() {
        // given
        timeRepository.save(new Time(LocalTime.of(12, 30)));

        // when & then
        assertThatThrownBy(() -> timeService.createTime(new TimeRequest(LocalTime.of(12, 30))))
                .isInstanceOf(ApplicationException.class);
    }

    @Test
    @DisplayName("삭제하려는 시간에 예약이 존재하면 예외를 발생한다.")
    void usingTimeDeleteFail() {
        // given
        Time time = timeRepository.save(new Time(LocalTime.now()));
        Theme theme = themeRepository.save(new Theme("테마명", "설명", "썸네일URL"));

        // when
        reservationRepository.save(new Reservation("예약", LocalDate.now().plusDays(1L), time, theme));

        // then
        assertThatThrownBy(() -> timeService.deleteTime(time.getId()))
                .isInstanceOf(ApplicationException.class);
    }
}
