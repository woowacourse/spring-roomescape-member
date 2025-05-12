package roomescape.reservation.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.auth.service.dto.LoginMember;
import roomescape.auth.entity.Member;
import roomescape.auth.entity.Role;
import roomescape.auth.repository.FakeMemberRepository;
import roomescape.auth.repository.MemberRepository;
import roomescape.global.exception.badRequest.BadRequestException;
import roomescape.global.exception.conflict.ReservationConflictException;
import roomescape.global.exception.notFound.ReservationTimeNotFoundException;
import roomescape.reservation.entity.Reservation;
import roomescape.reservation.repository.FakeReservationRepository;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.reservation.service.dto.request.CreateReservationRequest;
import roomescape.theme.entity.Theme;
import roomescape.theme.repository.FakeThemeRepository;
import roomescape.theme.repository.ThemeRepository;
import roomescape.time.entity.ReservationTime;
import roomescape.time.repository.FakeTimeRepository;
import roomescape.time.repository.ReservationTimeRepository;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ReservationServiceTest {
    private final ReservationRepository reservationRepository = new FakeReservationRepository();
    private final ReservationTimeRepository timeRepository = new FakeTimeRepository();
    private final ThemeRepository themeRepository = new FakeThemeRepository(reservationRepository);
    private final MemberRepository memberRepository = new FakeMemberRepository();
    private final ReservationService service = new ReservationService(
            reservationRepository,
            timeRepository,
            themeRepository,
            memberRepository
    );

    private final long themeId = 1L;
    private final LoginMember loginMember = new LoginMember(1L, "test", "test@example.com", Role.USER);

    @BeforeEach
    void setupTheme() {
        themeRepository.save(new Theme(themeId, "theme", "hello", "hi"));
        memberRepository.save(new Member(
                loginMember.id(),
                loginMember.name(),
                loginMember.role().name(),
                loginMember.email(),
                "password"
        ));
    }

    @DisplayName("존재하지 않는 timeId로 생성할 수 없다.")
    @Test
    void notExistTimeId() {
        // given
        LocalDate now = LocalDate.now();
        CreateReservationRequest request = new CreateReservationRequest(
                now.plusDays(1),
                loginMember,
                1L,
                themeId
        );

        // when & then
        assertThatThrownBy(() -> {
            service.createReservation(request);
        }).isInstanceOf(ReservationTimeNotFoundException.class);
     }

    @DisplayName("과거 날짜/시간 예약은 생성할 수 없다.")
    @Test
    void pastReservation() {
        // given
        final long timeId = 1L;
        ReservationTime timeEntity = new ReservationTime(timeId, LocalTime.of(12, 0));
        timeRepository.save(timeEntity);

        LocalDate now = LocalDate.now();
        CreateReservationRequest request = new CreateReservationRequest(
                now.minusDays(1),
                loginMember,
                timeId,
                themeId
        );

        // when & then
        assertThatThrownBy(() -> {
            service.createReservation(request);
        }).isInstanceOf(BadRequestException.class);
    }

    @DisplayName("같은 날짜, 같은 시각에 이미 예약이 존재하는 경우, 재생성할 수 없다.")
    @Test
    void duplicateReservation() {
        // given
        LocalDate now = LocalDate.now();
        LocalDate date = now.plusDays(1);

        final long timeId = 1L;
        ReservationTime timeEntity = new ReservationTime(timeId, LocalTime.of(12, 0));
        Reservation reservation = new Reservation(1L, loginMember.id(), date, timeEntity, themeId);
        timeRepository.save(timeEntity);
        reservationRepository.save(reservation);

        CreateReservationRequest request = new CreateReservationRequest(
                now.plusDays(1),
                loginMember,
                timeId,
                themeId
        );

        // when & then
        assertThatThrownBy(() -> {
            service.createReservation(request);
        }).isInstanceOf(ReservationConflictException.class);
    }

    @DisplayName("과거 날짜의 예약은 생성할 수 없다.")
    @Test
    void doesNotAllowedCreatingPastReservation() {
        // given
        final long timeId = 1L;
        ReservationTime time = new ReservationTime(timeId, LocalTime.of(12, 0));
        timeRepository.save(time);

        LocalDate now = LocalDate.now();
        LocalDate date = now.minusDays(1);

        CreateReservationRequest request = new CreateReservationRequest(date, loginMember, timeId, themeId);

        // when & then
        assertThatThrownBy(() -> {
            service.createReservation(request);
        }).isInstanceOf(BadRequestException.class);
    }
}
