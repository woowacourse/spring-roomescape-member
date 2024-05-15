package roomescape.reservation.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.admin.dto.ReservationFilterRequest;
import roomescape.member.domain.Member;
import roomescape.member.domain.Role;
import roomescape.member.domain.repository.MemberRepository;
import roomescape.member.dto.LoginMember;
import roomescape.reservation.dao.FakeMemberDao;
import roomescape.reservation.dao.FakeReservationDao;
import roomescape.reservation.dao.FakeReservationTimeDao;
import roomescape.reservation.dao.FakeThemeDao;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationTime;
import roomescape.reservation.domain.Theme;
import roomescape.reservation.domain.repository.ReservationRepository;
import roomescape.reservation.domain.repository.ReservationTimeRepository;
import roomescape.reservation.domain.repository.ThemeRepository;
import roomescape.reservation.dto.DateRequest;
import roomescape.reservation.dto.ReservationRequest;
import roomescape.reservation.dto.ReservationResponse;

@DisplayName("예약 로직 테스트")
class ReservationServiceTest {
    ReservationRepository reservationRepository;
    ReservationTimeRepository reservationTimeRepository;
    ThemeRepository themeRepository;
    MemberRepository memberRepository;
    ReservationService reservationService;

    Member member;
    Theme theme;
    ReservationTime reservationTime;

    @BeforeEach
    void setUp() {
        memberRepository = new FakeMemberDao();
        reservationRepository = new FakeReservationDao(memberRepository);
        reservationTimeRepository = new FakeReservationTimeDao(reservationRepository);
        themeRepository = new FakeThemeDao(reservationRepository);
        reservationService = new ReservationService(
                reservationRepository,
                reservationTimeRepository,
                themeRepository,
                memberRepository
        );

        reservationTime = reservationTimeRepository.save(new ReservationTime(LocalTime.MIDNIGHT));
        theme = themeRepository.save(new Theme("name", "description", "thumbnail"));
        member = memberRepository.save(new Member("name", "email@email.com", "Password", Role.MEMBER));
    }

    @DisplayName("예약 조회에 성공한다.")
    @Test
    void find() {
        //given
        LocalDate date = LocalDate.now().plusYears(1);

        reservationService.create(new ReservationRequest(new DateRequest(date.toString()), reservationTime.getId(), theme.getId()),
                new LoginMember(member.getId(), member.getName(), member.getEmail(), member.getRole()));

        //when
        List<ReservationResponse> reservations = reservationService.findAllReservations();

        //then
        assertAll(
                () -> assertThat(reservations).hasSize(1),
                () -> assertThat(reservations.get(0).date()).isEqualTo(date),
                () -> assertThat(reservations.get(0).time().id()).isEqualTo(reservationTime.getId()),
                () -> assertThat(reservations.get(0).time().startAt()).isEqualTo(reservationTime.getStartAt())
        );
    }

    @DisplayName("모든 조건을 통해 예약 조회에 성공한다.")
    @Test
    void findByFilter() {
        //given
        LocalDate date = LocalDate.now().plusYears(1);

        reservationService.create(new ReservationRequest(new DateRequest(date.toString()), reservationTime.getId(), theme.getId()),
                new LoginMember(member.getId(), member.getName(), member.getEmail(), member.getRole()));
        ReservationFilterRequest reservationFilterRequest = new ReservationFilterRequest(theme.getId(), member.getId(),
                LocalDate.parse("2025-01-01"), LocalDate.parse("2025-12-31"));

        //when
        List<ReservationResponse> reservations = reservationService.findReservationsBy(reservationFilterRequest);

        //then
        assertAll(
                () -> assertThat(reservations).hasSize(1),
                () -> assertThat(reservations.get(0).date()).isEqualTo(date),
                () -> assertThat(reservations.get(0).time().id()).isEqualTo(reservationTime.getId()),
                () -> assertThat(reservations.get(0).time().startAt()).isEqualTo(reservationTime.getStartAt())
        );
    }

    @DisplayName("하나의 조건을 통해 예약 조회에 성공한다.")
    @Test
    void findByOneFilter() {
        //given
        LocalDate date = LocalDate.now().plusYears(1);

        reservationService.create(new ReservationRequest(new DateRequest(date.toString()), reservationTime.getId(), theme.getId()),
                new LoginMember(member.getId(), member.getName(), member.getEmail(), member.getRole()));
        ReservationFilterRequest reservationFilterRequest = new ReservationFilterRequest(theme.getId(), member.getId(),
                LocalDate.parse("2025-01-01"), LocalDate.parse("2025-12-31"));

        //when
        List<ReservationResponse> reservations = reservationService.findReservationsBy(reservationFilterRequest);

        //when & then
        assertAll(
                () -> assertThat(reservations).hasSize(1),
                () -> assertThat(reservations.get(0).date()).isEqualTo(date),
                () -> assertThat(reservations.get(0).time().id()).isEqualTo(reservationTime.getId()),
                () -> assertThat(reservations.get(0).time().startAt()).isEqualTo(reservationTime.getStartAt())
        );
    }

    @DisplayName("조건이 모두 없어도 조회가 가능하다.")
    @Test
    void findByNoFilter() {
        //given
        LocalDate date = LocalDate.now().plusYears(1);

        reservationService.create(new ReservationRequest(new DateRequest(date.toString()), reservationTime.getId(), theme.getId()),
                new LoginMember(member.getId(), member.getName(), member.getEmail(), member.getRole()));
        ReservationFilterRequest reservationFilterRequest = new ReservationFilterRequest(null, null, null, null);

        //when
        List<ReservationResponse> reservations = reservationService.findReservationsBy(reservationFilterRequest);

        //then
        assertAll(
                () -> assertThat(reservations).hasSize(1),
                () -> assertThat(reservations.get(0).date()).isEqualTo(date),
                () -> assertThat(reservations.get(0).time().id()).isEqualTo(reservationTime.getId()),
                () -> assertThat(reservations.get(0).time().startAt()).isEqualTo(reservationTime.getStartAt())
        );
    }

    @DisplayName("시작 날짜보다 끝 날짜가 전이면 예외가 발생한다.")
    @Test
    void findByFilterException() {
        //given
        LocalDate date = LocalDate.now().plusYears(1);

        reservationService.create(new ReservationRequest(new DateRequest(date.toString()), reservationTime.getId(), theme.getId()),
                new LoginMember(member.getId(), member.getName(), member.getEmail(), member.getRole()));
        ReservationFilterRequest reservationFilterRequest = new ReservationFilterRequest(theme.getId(), member.getId(),
                LocalDate.parse("2025-01-01"), LocalDate.parse("2024-12-31"));

        //when & then
        assertThatThrownBy(() -> reservationService.findReservationsBy(reservationFilterRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("예약 생성에 성공한다.")
    @Test
    void create() {
        //given
        String date = "2099-04-18";
        ReservationRequest reservationRequest = new ReservationRequest(new DateRequest(date), reservationTime.getId(), theme.getId());

        //when
        ReservationResponse reservationResponse = reservationService.create(reservationRequest,
                new LoginMember(1, "test", "test@email.com", Role.MEMBER));

        //then
        assertAll(
                () -> assertThat(reservationResponse.date()).isEqualTo(date),
                () -> assertThat(reservationResponse.time().id()).isEqualTo(reservationTime.getId())
        );
    }

    @DisplayName("존재하지 않는 시간으로 예약할 시 예외가 발생한다.")
    @Test
    void NotExistTimeReservation() {
        //given
        String date = "2099-04-18";
        reservationRepository.save(new Reservation(1L, LocalDate.parse(date), reservationTime, theme));

        ReservationRequest reservationRequest = new ReservationRequest(new DateRequest(date), reservationTime.getId() + 1,
                theme.getId());

        //when & then
        assertThatThrownBy(() -> reservationService.create(reservationRequest,
                new LoginMember(member.getId(), member.getName(), member.getEmail(), member.getRole())))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("존재하지 않는 테마로 예약할 시 예외가 발생한다.")
    @Test
    void NotExistThemeReservation() {
        //given
        String date = "2099-04-18";

        ReservationRequest reservationRequest = new ReservationRequest(new DateRequest(date), reservationTime.getId(),
                theme.getId() + 1);

        //when & then
        assertThatThrownBy(() -> reservationService.create(reservationRequest,
                new LoginMember(member.getId(), member.getName(), member.getEmail(), member.getRole())))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("날짜, 시간, 테마가 중복되게 예약할 시 예외가 발생한다.")
    @Test
    void duplicateReservation() {
        //given
        String date = "2099-04-18";
        long timeId = reservationTime.getId();
        long themeId = theme.getId();

        ReservationRequest reservationRequest = new ReservationRequest(new DateRequest(date), timeId, themeId);
        reservationService.create(reservationRequest,
                new LoginMember(member.getId(), member.getName(), member.getEmail(), member.getRole()));

        //when & then
        assertThatThrownBy(() -> reservationService.create(reservationRequest,
                new LoginMember(member.getId(), member.getName(), member.getEmail(), member.getRole())))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("예약 삭제에 성공한다.")
    @Test
    void delete() {
        //given
        String date = "2099-04-18";
        long timeId = reservationTime.getId();
        long themeId = theme.getId();

        ReservationRequest reservationRequest = new ReservationRequest(new DateRequest(date), timeId, themeId);
        ReservationResponse reservation = reservationService.create(reservationRequest,
                new LoginMember(member.getId(), member.getName(), member.getEmail(), member.getRole()));

        //when
        reservationService.delete(reservation.id());

        //then
        assertThat(reservationRepository.findAll()).hasSize(0);
    }

    @DisplayName("존재하지 않는 예약 삭제 시 예외가 발생한다.")
    @Test
    void deleteNotExistReservation() {
        // give & when & then
        assertThatThrownBy(() -> reservationService.delete(5L))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
