package roomescape.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import roomescape.domain.member.Member;
import roomescape.domain.theme.Theme;
import roomescape.domain.time.Time;
import roomescape.dto.reservation.request.UserReservationRequest;
import roomescape.global.exception.ApplicationException;
import roomescape.repository.MemberRepository;
import roomescape.repository.ThemeRepository;
import roomescape.repository.TimeRepository;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(webEnvironment = WebEnvironment.NONE)
class ReservationServiceSpringBootTest {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private TimeRepository timeRepository;

    @Autowired
    private ThemeRepository themeRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("동일한 날짜와 시간과 테마에 예약을 생성하면 예외가 발생한다")
    void duplicateTimeReservationAddFail() {
        // given
        Time time = timeRepository.save(new Time(LocalTime.of(12, 30)));
        Theme theme = themeRepository.save(new Theme("테마명", "설명", "썸네일URL"));
        Member member = memberRepository.save(new Member("ddang", "user", "ddang@google.com", "password"));

        // when & then
        reservationService.createReservation(
                new UserReservationRequest(LocalDate.now().plusDays(1L), time.getId(), theme.getId()), member.getId());

        assertThatThrownBy(() -> reservationService.createReservation(
                new UserReservationRequest(LocalDate.now().plusDays(1L), time.getId(), theme.getId()), member.getId())
        ).isInstanceOf(ApplicationException.class);
    }

    @Test
    @DisplayName("이미 지난 날짜로 예약을 생성하면 예외가 발생한다")
    void beforeDateReservationFail() {
        // given
        Time time = timeRepository.save(new Time(LocalTime.of(12, 30)));
        Theme theme = themeRepository.save(new Theme("테마명", "설명", "썸네일URL"));
        Member member = memberRepository.save(new Member("ddang", "user", "ddang@google.com", "password"));
        LocalDate beforeDate = LocalDate.now().minusDays(1L);

        // when & then
        assertThatThrownBy(() -> reservationService.createReservation(
                new UserReservationRequest(beforeDate, time.getId(), theme.getId()), member.getId())
        ).isInstanceOf(ApplicationException.class);
    }

    @Test
    @DisplayName("현재 날짜가 예약 당일이지만, 이미 지난 시간으로 예약을 생성하면 예외가 발생한다")
    void beforeTimeReservationFail() {
        // given
        LocalTime requestTime = LocalTime.now();
        LocalTime beforeTime = requestTime.minusMinutes(1L);
        Time time = timeRepository.save(new Time(beforeTime));
        Theme theme = themeRepository.save(new Theme("테마명", "설명", "썸네일URL"));
        Member member = memberRepository.save(new Member("ddang", "user", "ddang@google.com", "password"));

        // when & then
        assertThatThrownBy(() -> reservationService.createReservation(
                new UserReservationRequest(LocalDate.now(), time.getId(), theme.getId()), member.getId())
        ).isInstanceOf(ApplicationException.class);
    }
}
