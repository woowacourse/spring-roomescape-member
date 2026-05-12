package roomescape.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.reservationtime.domain.ReservationTime;
import roomescape.service.stub.FakeReservationRepository;
import roomescape.service.stub.FakeThemeRepository;
import roomescape.theme.exception.ThemeConstraintException;
import roomescape.theme.exception.ThemeDuplicateException;
import roomescape.theme.repository.ThemeRepository;
import roomescape.theme.service.ThemeService;

class ThemeServiceTest {

    private ThemeService themeService;
    private ReservationRepository reservationRepository;

    @BeforeEach
    void setUp() {
        ThemeRepository themeRepository = new FakeThemeRepository();
        reservationRepository = new FakeReservationRepository();
        themeService = new ThemeService(themeRepository, reservationRepository);
    }

    @Test
    @DisplayName("같은 이름 테마 중복 생성 예외")
    void save_whenDuplicateName_throws() {
        // given
        themeService.save("미술관의 밤", "설명", "thumb");

        // when & then
        assertThatThrownBy(() -> themeService.save("미술관의 밤", "다른 설명", "thumb2"))
                .isInstanceOf(ThemeDuplicateException.class);
    }

    @Test
    @DisplayName("예약 존재하는 테마 삭제 예외")
    void deleteById_whenExistsReservation_throws() {
        // given
        themeService.save("미술관의 밤", "설명", "thumb");
        themeService.save("미술관의 밤2", "설명", "thumb");
        reservationRepository.save(
                Reservation.createNew(
                        "홍길동",
                        LocalDate.now(),
                        ReservationTime.createNew(
                                java.time.LocalTime.of(10, 0),
                                themeService.getAll().get(0)
                        )
                )
        );

        // when & then
        assertThatThrownBy(() -> themeService.deleteById(themeService.getAll().get(0).getId()))
                .isInstanceOf(ThemeConstraintException.class);

    }
}
