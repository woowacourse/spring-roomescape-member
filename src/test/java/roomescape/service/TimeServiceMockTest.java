package roomescape.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.domain.member.Member;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.theme.Theme;
import roomescape.domain.time.Time;
import roomescape.dto.time.TimeRequest;
import roomescape.global.exception.ApplicationException;
import roomescape.repository.ReservationRepository;
import roomescape.repository.TimeRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TimeServiceMockTest {

    @InjectMocks
    private TimeService timeService;

    @Mock
    private TimeRepository timeRepository;

    @Mock
    private ReservationRepository reservationRepository;

    @Test
    @DisplayName("중복된 예약 시간을 등록하는 경우 예외가 발생한다.")
    void duplicateTimeFail() {
        // given
        Time time = new Time(LocalTime.of(12, 30));

        when(timeRepository.findByStartAt(time.getStartAt())).thenReturn(List.of(time));

        // when & then
        assertThatThrownBy(() -> timeService.createTime(new TimeRequest(time.getStartAt())))
                .isInstanceOf(ApplicationException.class);
    }

    @Test
    @DisplayName("삭제하려는 시간에 예약이 존재하면 예외를 발생한다.")
    void usingTimeDeleteFail() {
        // given
        Time time = new Time(1L, LocalTime.now());
        Theme theme = new Theme(1L, "테마명", "설명", "썸네일URL");
        Member member = new Member(1L, "ddang", "user", "ddang@google.com", "password");
        Reservation reservation = new Reservation(LocalDate.now().plusDays(1L), time, theme, member);

        when(reservationRepository.findByTimeId(time.getId())).thenReturn(List.of(reservation));

        // when & then
        assertThatThrownBy(() -> timeService.deleteTime(time.getId()))
                .isInstanceOf(ApplicationException.class);
    }
}
