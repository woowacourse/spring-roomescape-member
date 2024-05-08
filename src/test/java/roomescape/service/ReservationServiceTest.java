package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import roomescape.controller.login.LoginMember;
import roomescape.domain.Member;
import roomescape.domain.MemberRepository;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationRepository;
import roomescape.domain.ReservationTime;
import roomescape.domain.ReservationTimeRepository;
import roomescape.domain.Theme;
import roomescape.domain.ThemeRepository;
import roomescape.exception.ReservationBusinessException;
import roomescape.service.dto.ReservationResponse;
import roomescape.service.dto.ReservationSaveRequest;

@SpringBootTest
@Transactional
class ReservationServiceTest {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ReservationTimeRepository reservationTimeRepository;

    @Autowired
    private ThemeRepository themeRepository;

    @Autowired
    private MemberRepository memberRepository;

    @DisplayName("예약 저장")
    @Test
    void saveReservation() {
        final ReservationTime time = reservationTimeRepository.save(new ReservationTime(LocalTime.parse("10:00")));
        final Theme theme = themeRepository.save(new Theme("이름", "설명", "썸네일"));
        final Member member = memberRepository.save(new Member("고구마", "email@email.com", "1234"));

        final ReservationSaveRequest reservationSaveRequest = new ReservationSaveRequest(LocalDate.parse("2025-11-11"),
                time.getId(), theme.getId());
        final ReservationResponse reservationResponse = reservationService.saveReservation(reservationSaveRequest,
                LoginMember.from(member));

        assertAll(
                () -> assertThat(reservationResponse.name()).isEqualTo("고구마"),
                () -> assertThat(reservationResponse.date()).isEqualTo(LocalDate.parse("2025-11-11")),
                () -> assertThat(reservationResponse.time().id()).isEqualTo(time.getId()),
                () -> assertThat(reservationResponse.time().startAt()).isEqualTo(time.getStartAt()),
                () -> assertThat(reservationResponse.theme().id()).isEqualTo(theme.getId()),
                () -> assertThat(reservationResponse.theme().name()).isEqualTo(theme.getName()),
                () -> assertThat(reservationResponse.theme().description()).isEqualTo(theme.getDescription()),
                () -> assertThat(reservationResponse.theme().thumbnail()).isEqualTo(theme.getThumbnail())
        );
    }

    @DisplayName("존재하지 않는 예약 시간으로 예약 저장")
    @Test
    void timeForSaveReservationNotFound() {
        final Member member = memberRepository.save(new Member("고구마", "email@email.com", "1234"));

        final ReservationSaveRequest reservationSaveRequest = new ReservationSaveRequest(LocalDate.parse("2025-11-11"), 2L, 1L);
        assertThatThrownBy(() -> {
            reservationService.saveReservation(reservationSaveRequest, LoginMember.from(member));
        }).isInstanceOf(ReservationBusinessException.class);
    }

    @DisplayName("예약 삭제")
    @Test
    void deleteReservation() {
        final ReservationTime time = reservationTimeRepository.save(new ReservationTime(LocalTime.parse("10:00")));
        final Theme theme = themeRepository.save(new Theme("이름", "설명", "썸네일"));
        final Member member = memberRepository.save(new Member("고구마", "email@email.com", "1234"));

        final Reservation savedReservation = reservationRepository.save(
                new Reservation(member, LocalDate.parse("2025-05-13"), time, theme));

        reservationService.deleteReservation(savedReservation.getId());
        assertThat(reservationService.getReservations().size()).isEqualTo(0);
    }

    @DisplayName("존재하지 않는 예약 삭제")
    @Test
    void deleteReservationNotFound() {
        assertThatThrownBy(() -> {
            reservationService.deleteReservation(2L);
        }).isInstanceOf(ReservationBusinessException.class);
    }

    @DisplayName("중복된 예약 저장")
    @Test
    void saveDuplicatedReservation() {
        final ReservationTime time = reservationTimeRepository.save(new ReservationTime(LocalTime.parse("10:00")));
        final Theme theme = themeRepository.save(new Theme("이름", "설명", "썸네일"));
        final Member member = memberRepository.save(new Member("고구마", "email@email.com", "1234"));

        final Reservation savedReservation = reservationRepository.save(
                new Reservation(member, LocalDate.parse("2025-05-13"), time, theme));

        assertThatThrownBy(() -> {
            reservationService.saveReservation(
                    new ReservationSaveRequest(LocalDate.parse("2025-05-13"), time.getId(), theme.getId()), LoginMember.from(member));
        }).isInstanceOf(ReservationBusinessException.class);
    }
}
