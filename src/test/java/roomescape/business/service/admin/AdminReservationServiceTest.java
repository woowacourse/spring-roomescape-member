package roomescape.business.service.admin;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.business.domain.member.SignUpMember;
import roomescape.business.domain.reservation.ReservationTheme;
import roomescape.business.domain.reservation.ReservationTime;
import roomescape.exception.ReservationException;
import roomescape.exception.ReservationTimeException;
import roomescape.persistence.MemberRepository;
import roomescape.persistence.ReservationRepository;
import roomescape.persistence.ReservationThemeRepository;
import roomescape.persistence.ReservationTimeRepository;
import roomescape.persistence.fakerepository.FakeMemberRepository;
import roomescape.persistence.fakerepository.FakeReservationRepository;
import roomescape.persistence.fakerepository.FakeReservationThemeRepository;
import roomescape.persistence.fakerepository.FakeReservationTimeRepository;
import roomescape.presentation.admin.dto.AdminReservationRequestDto;
import roomescape.presentation.member.dto.ReservationResponseDto;

class AdminReservationServiceTest {

    private AdminReservationService adminReservationService;
    private ReservationRepository reservationRepository;
    private ReservationTimeRepository reservationTimeRepository;
    private ReservationThemeRepository reservationThemeRepository;

    @BeforeEach
    void setUp() {
        reservationRepository = new FakeReservationRepository();
        reservationTimeRepository = new FakeReservationTimeRepository();
        reservationThemeRepository = new FakeReservationThemeRepository();
        MemberRepository memberRepository = new FakeMemberRepository();
        adminReservationService = new AdminReservationService(
                reservationRepository,
                reservationTimeRepository,
                reservationThemeRepository,
                memberRepository
        );
        memberRepository.save(new SignUpMember("벨로", "bello@email.com", "1234"));
    }

    @DisplayName("예약을 생성한다.")
    @Test
    void createReservation() {
        // given
        Long timeId = reservationTimeRepository.add(new ReservationTime(LocalTime.now())).getId();
        Long themeId = reservationThemeRepository.add(new ReservationTheme("테마 이름", "설명", "썸네일")).getId();
        AdminReservationRequestDto reservationRequestDto = new AdminReservationRequestDto(
                1L,
                LocalDate.now().plusDays(1),
                themeId,
                timeId
        );

        // when
        ReservationResponseDto reservation = adminReservationService.createReservation(reservationRequestDto);

        // then
        assertThat(reservation.id())
                .isEqualTo(1L);
    }

    @DisplayName("과거 일시로 예약을 생성할 경우 예외가 발생한다.")
    @Test
    void shouldThrowException_WhenCreatingReservationWithPastDate() {
        // given
        Long timeId = reservationTimeRepository.add(new ReservationTime(LocalTime.now())).getId();
        Long themeId = reservationThemeRepository.add(new ReservationTheme("테마", "설명", "썸네일")).getId();
        AdminReservationRequestDto reservationRequestDto = new AdminReservationRequestDto(
                1L,
                LocalDate.now().minusDays(1),
                themeId,
                timeId
        );

        // when
        // then
        assertThatCode(() -> adminReservationService.createReservation(reservationRequestDto))
                .isInstanceOf(ReservationException.class)
                .hasMessage("과거 일시로 예약을 생성할 수 없습니다.");
    }

    @DisplayName("존재하지 않는 사용자 id로 예약을 생성할 경우 예외가 발생한다.")
    @Test
    void shouldThrowException_WhenCreatingReservationWithNonExistentMember() {
        // given
        Long timeId = reservationTimeRepository.add(new ReservationTime(LocalTime.now())).getId();
        Long themeId = reservationThemeRepository.add(new ReservationTheme("테마", "설명", "썸네일")).getId();
        long notExistsMemberId = Long.MAX_VALUE;
        AdminReservationRequestDto reservationRequestDto = new AdminReservationRequestDto(
                notExistsMemberId,
                LocalDate.now().plusDays(1),
                themeId,
                timeId
        );

        // when
        // then
        assertThatCode(() -> adminReservationService.createReservation(reservationRequestDto))
                .isInstanceOf(ReservationException.class)
                .hasMessage("존재하지 않는 회원입니다.");
    }

    @DisplayName("존재하지 않는 시간 id로 예약을 생성할 경우 예외가 발생한다.")
    @Test
    void shouldThrowException_WhenCreatingReservationWithNonExistentTime() {
        // given
        Long themeId = reservationThemeRepository.add(new ReservationTheme("테마", "설명", "썸네일")).getId();
        long notExistsTimeId = Long.MAX_VALUE;
        AdminReservationRequestDto reservationRequestDto = new AdminReservationRequestDto(
                1L,
                LocalDate.now().plusDays(1),
                themeId,
                notExistsTimeId
        );

        // when
        // then
        assertThatCode(() -> adminReservationService.createReservation(reservationRequestDto))
                .isInstanceOf(ReservationTimeException.class)
                .hasMessage("존재하지 않는 예약 시간입니다.");
    }

    @DisplayName("이미 예약된 경우(날짜, 시간, 테마) 예약할 수 없다.")
    @Test
    void shouldThrowException_WhenCreatingReservationWithDuplicatedReservation() {
        // given
        Long timeId = reservationTimeRepository.add(new ReservationTime(LocalTime.now())).getId();
        Long themeId = reservationThemeRepository.add(new ReservationTheme("테마", "설명", "썸네일")).getId();
        AdminReservationRequestDto reservationRequestDto = new AdminReservationRequestDto(
                1L,
                LocalDate.now().plusDays(1),
                themeId,
                timeId
        );
        adminReservationService.createReservation(reservationRequestDto);

        // when
        // then
        assertThatCode(() -> adminReservationService.createReservation(reservationRequestDto))
                .isInstanceOf(ReservationException.class)
                .hasMessage("해당 날짜와 시간에 이미 예약이 존재합니다.");
    }

    @DisplayName("예약을 취소한다.")
    @Test
    void deleteReservationById() {
        // given
        Long timeId = reservationTimeRepository.add(new ReservationTime(LocalTime.now())).getId();
        Long themeId = reservationThemeRepository.add(new ReservationTheme("테마", "설명", "썸네일")).getId();
        ReservationResponseDto reservation = adminReservationService.createReservation(new AdminReservationRequestDto(
                        1L,
                        LocalDate.now().plusDays(1),
                        themeId,
                        timeId
                )
        );

        // when
        adminReservationService.deleteReservationById(reservation.id());

        // then
        assertThat(reservationRepository.findById(reservation.id()))
                .isEmpty();
    }
}
