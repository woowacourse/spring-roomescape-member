package roomescape.business.service.admin;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.business.domain.member.Member;
import roomescape.business.domain.reservation.Reservation;
import roomescape.business.domain.reservation.ReservationTheme;
import roomescape.business.domain.reservation.ReservationTime;
import roomescape.exception.ReservationThemeException;
import roomescape.persistence.ReservationRepository;
import roomescape.persistence.ReservationThemeRepository;
import roomescape.persistence.fakerepository.FakeReservationRepository;
import roomescape.persistence.fakerepository.FakeReservationThemeRepository;
import roomescape.presentation.admin.dto.ReservationThemeRequestDto;

class AdminReservationThemeServiceTest {

    private AdminReservationThemeService reservationThemeService;
    private ReservationThemeRepository reservationThemeRepository;
    private ReservationRepository reservationRepository;

    @BeforeEach
    void setUp() {
        reservationRepository = new FakeReservationRepository();
        reservationThemeRepository = new FakeReservationThemeRepository();
        reservationThemeService = new AdminReservationThemeService(reservationRepository, reservationThemeRepository);
    }

    @DisplayName("테마를 추가한다.")
    @Test
    void createTheme() {
        // given
        ReservationThemeRequestDto reservationThemeRequest = new ReservationThemeRequestDto("수양", "수양테마", "수양썸네일");

        // when
        long themeId = reservationThemeService.createTheme(reservationThemeRequest).id();

        // then
        ReservationTheme reservationTheme = reservationThemeRepository.findById(themeId).orElseThrow();
        assertAll(
                () -> assertThat(reservationTheme.getName()).isEqualTo("수양"),
                () -> assertThat(reservationTheme.getDescription()).isEqualTo("수양테마"),
                () -> assertThat(reservationTheme.getThumbnail()).isEqualTo("수양썸네일")
        );
    }

    @DisplayName("동일한 이름의 테마를 추가할 경우 예외가 발생한다.")
    @Test
    void shouldThrowException_WhenAddingSameNameTheme() {
        // given
        ReservationThemeRequestDto reservationThemeRequest = new ReservationThemeRequestDto("수양", "수양테마", "수양썸네일");
        reservationThemeService.createTheme(reservationThemeRequest);

        // when
        // then
        assertThatCode(() -> reservationThemeService.createTheme(reservationThemeRequest))
                .isInstanceOf(ReservationThemeException.class)
                .hasMessage("동일한 이름의 테마를 추가할 수 없습니다.");
    }

    @DisplayName("테마를 삭제한다.")
    @Test
    void deleteThemeById() {
        // given
        ReservationTheme reservationTheme = reservationThemeRepository.add(new ReservationTheme("수양", "수양테마", "수양썸네일"));

        // when
        reservationThemeService.deleteThemeById(reservationTheme.getId());

        // then
        assertThat(reservationThemeRepository.findById(reservationTheme.getId()))
                .isNotPresent();
    }


    @DisplayName("예약이 참조하고 있는 테마를 삭제하면 예외가 발생한다.")
    @Test
    void shouldThrowException_WhenDeletingThemeWithReservation() {
        // given
        ReservationTheme reservationTheme = reservationThemeRepository.add(new ReservationTheme("수양", "수양테마", "수양썸네일"));
        reservationRepository.add(
                new Reservation(
                        new Member(1L, "수양", "test@email.com"),
                        LocalDate.now().plusDays(1),
                        new ReservationTime(1L, LocalTime.now()),
                        new ReservationTheme(reservationTheme.getId(), "수양", "수양테마", "수양썸네일")
                )
        );

        // when
        // then
        assertThatCode(() -> reservationThemeService.deleteThemeById(reservationTheme.getId()))
                .isInstanceOf(ReservationThemeException.class)
                .hasMessage("해당 테마의 예약이 존재하여 삭제할 수 없습니다.");
    }
}
