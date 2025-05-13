package roomescape.business.service.member;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.business.domain.member.Member;
import roomescape.business.domain.member.MemberRole;
import roomescape.business.domain.member.SignUpMember;
import roomescape.business.domain.reservation.ReservationTheme;
import roomescape.business.domain.reservation.ReservationTime;
import roomescape.config.LoginMember;
import roomescape.exception.ReservationException;
import roomescape.persistence.MemberRepository;
import roomescape.persistence.ReservationRepository;
import roomescape.persistence.ReservationThemeRepository;
import roomescape.persistence.ReservationTimeRepository;
import roomescape.persistence.fakerepository.FakeMemberRepository;
import roomescape.persistence.fakerepository.FakeReservationRepository;
import roomescape.persistence.fakerepository.FakeReservationThemeRepository;
import roomescape.persistence.fakerepository.FakeReservationTimeRepository;
import roomescape.presentation.member.dto.ReservationRequestDto;

class ReservationServiceTest {

    private ReservationService reservationService;
    private ReservationTimeRepository reservationTimeRepository;
    private ReservationThemeRepository reservationThemeRepository;
    private LoginMember loginMember;

    @BeforeEach
    void setUp() {
        ReservationRepository reservationRepository = new FakeReservationRepository();
        reservationTimeRepository = new FakeReservationTimeRepository();
        reservationThemeRepository = new FakeReservationThemeRepository();
        MemberRepository memberRepository = new FakeMemberRepository();
        reservationService = new ReservationService(
                reservationRepository,
                reservationTimeRepository,
                reservationThemeRepository,
                memberRepository
        );
        memberRepository.save(new SignUpMember("벨로", "bello@email.com", "1234"));
        loginMember = new LoginMember(1L, "벨로", MemberRole.MEMBER);
    }

    @DisplayName("예약한다.")
    @Test
    void createReservation() {
        // given
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        Long timeId = reservationTimeRepository.add(new ReservationTime(LocalTime.now())).getId();
        Long themeId = reservationThemeRepository.add(new ReservationTheme("테마", "설명", "썸네일")).getId();
        ReservationRequestDto reservationRequestDto = new ReservationRequestDto(tomorrow, timeId, themeId);

        // when
        reservationService.createReservation(reservationRequestDto, loginMember);

        // then
        assertThat(reservationService.getAllReservations())
                .isNotEmpty();
    }

    @DisplayName("과거 일시로 예약을 생성할 경우 예외가 발생한다.")
    @Test
    void shouldThrowException_WhenCreatingReservationWithPastDate() {
        // given
        LocalDateTime pastDateTime = LocalDateTime.now().minusDays(1);
        Long timeId = reservationTimeRepository.add(new ReservationTime(pastDateTime.toLocalTime())).getId();
        Long themeId = reservationThemeRepository.add(new ReservationTheme("테마", "설명", "썸네일")).getId();
        ReservationRequestDto reservationRequestDto = new ReservationRequestDto(
                pastDateTime.toLocalDate(), timeId, themeId);

        // when
        // then
        assertThatCode(() -> reservationService.createReservation(reservationRequestDto, loginMember))
                .isInstanceOf(ReservationException.class)
                .hasMessage("과거 일시로 예약을 생성할 수 없습니다.");
    }

    @DisplayName("이미 예약된 경우(날짜, 시간, 테마) 예약할 수 없다.")
    @Test
    void shouldThrowException_WhenCreatingReservationWithDuplicatedReservation() {
        // given
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        Long timeId = reservationTimeRepository.add(new ReservationTime(LocalTime.now())).getId();
        Long themeId = reservationThemeRepository.add(new ReservationTheme("테마", "설명", "썸네일")).getId();
        ReservationRequestDto reservationRequestDto = new ReservationRequestDto(tomorrow, timeId, themeId);
        reservationService.createReservation(reservationRequestDto, loginMember);

        // when
        // then
        assertThatCode(() -> reservationService.createReservation(reservationRequestDto, loginMember))
                .isInstanceOf(ReservationException.class)
                .hasMessage("해당 날짜와 시간에 이미 예약이 존재합니다.");
    }

    @DisplayName("예약 목록을 조회한다.")
    @Test
    void getAllReservations() {
        // given
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        Long timeId = reservationTimeRepository.add(new ReservationTime(LocalTime.now())).getId();
        Long themeId = reservationThemeRepository.add(new ReservationTheme("테마", "설명", "썸네일")).getId();
        ReservationRequestDto reservationRequestDto = new ReservationRequestDto(tomorrow, timeId, themeId);
        reservationService.createReservation(reservationRequestDto, loginMember);

        // when
        var reservations = reservationService.getAllReservations();

        // then
        assertThat(reservations)
                .isNotEmpty();
    }

    @DisplayName("예약을 조회한다.")
    @Test
    void getReservationById() {
        // given
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        Long timeId = reservationTimeRepository.add(new ReservationTime(LocalTime.now())).getId();
        Long themeId = reservationThemeRepository.add(new ReservationTheme("테마", "설명", "썸네일")).getId();
        ReservationRequestDto reservationRequestDto = new ReservationRequestDto(tomorrow, timeId, themeId);
        reservationService.createReservation(reservationRequestDto, loginMember);

        // when
        var reservation = reservationService.getAllReservations().getFirst();

        // then
        assertThat(reservation)
                .isNotNull();
    }
}
