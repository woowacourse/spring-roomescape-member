package roomescape.business.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.business.Reservation;
import roomescape.business.ReservationTheme;
import roomescape.business.fakerepository.FakeReservationRepository;
import roomescape.business.fakerepository.FakeReservationThemeRepository;
import roomescape.persistence.ReservationRepository;
import roomescape.persistence.ReservationThemeRepository;
import roomescape.presentation.dto.ReservationThemeRequestDto;

class ReservationThemeServiceTest {

    private ReservationThemeService reservationThemeService;
    private ReservationThemeRepository reservationThemeRepository;
    private ReservationRepository reservationRepository;

    @BeforeEach
    void setUp() {
        reservationRepository = new FakeReservationRepository();
        reservationThemeRepository = new FakeReservationThemeRepository();
        reservationThemeService = new ReservationThemeService(reservationRepository, reservationThemeRepository);
    }

    @DisplayName("동일한 이름의 테마를 추가할 경우 예외가 발생한다.")
    @Test
    void createSameNameTheme() {
        // given
        ReservationThemeRequestDto reservationThemeRequest = new ReservationThemeRequestDto("수양", "수양테마", "수양썸네일");
        reservationThemeService.createTheme(reservationThemeRequest);

        // when
        // then
        assertThatCode(() -> reservationThemeService.createTheme(reservationThemeRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("동일한 이름의 테마를 추가할 수 없습니다.");
    }

    @DisplayName("테마를 삭제한다.")
    @Test
    void deleteTheme() {
        // given
        Long themeId = reservationThemeRepository.add(new ReservationTheme("수양", "수양테마", "수양썸네일"));

        // when
        reservationThemeService.deleteTheme(themeId);

        // then
        assertThat(reservationThemeRepository.findById(themeId))
                .isNotPresent();
    }


    @DisplayName("예약이 참조하고 있는 테마를 삭제한다.")
    @Test
    void deleteReferencedTheme() {
        // given
        Long themeId = reservationThemeRepository.add(new ReservationTheme("수양", "수양테마", "수양썸네일"));
        reservationRepository.add(new Reservation("수양", LocalDate.now().plusDays(1), null,
                new ReservationTheme(themeId, "수양", "수양테마", "수양썸네일")));

        // when
        // then
        assertThatCode(() -> reservationThemeService.deleteTheme(themeId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당 테마의 예약이 존재하여 삭제할 수 없습니다.");
    }
}
