package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import roomescape.acceptancetest.RoomecapeAcceptanceTest;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.reservationtime.domain.ReservationTime;
import roomescape.reservationtime.repository.ReservationTimeRepository;
import roomescape.theme.domain.Theme;
import roomescape.theme.exception.ThemeConstraintException;
import roomescape.theme.exception.ThemeDuplicateException;
import roomescape.theme.repository.ThemeRepository;
import roomescape.theme.service.ThemeService;

@RoomescapeServiceTest
class ThemeServiceTest {

    @Autowired
    private ThemeService themeService;
    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private ThemeRepository themeRepository;
    @Autowired
    private ReservationTimeRepository reservationTimeRepository;

    @Test
    @DisplayName("같은 이름 테마 중복 생성 예외")
    void save_whenDuplicateName_throws() {
        // given
        themeRepository.save(Theme.createNew("미술관의 밤", "설명", "thumb"));

        // when & then
        assertThatThrownBy(() -> themeService.save("미술관의 밤", "다른 설명", "thumb2"))
                .isInstanceOf(ThemeDuplicateException.class);
    }

    @Test
    @DisplayName("예약 존재하는 테마 삭제 예외")
    void deleteById_whenExistsReservation_throws() {
        // given
        Theme theme = themeRepository.save(Theme.createNew("미술관의 밤", "설명", "thumb"));
        themeRepository.save(Theme.createNew("미술관의 밤2", "설명", "thumb"));
        ReservationTime reservationTime = reservationTimeRepository.save(ReservationTime.createNew(LocalTime.of(10, 0), theme));
        reservationRepository.save(
                Reservation.createNew(
                        "쿠다",
                        LocalDate.now(),
                        reservationTime
                )
        );

        // when & then
        assertThatThrownBy(() -> themeService.deleteById(theme.getId()))
                .isInstanceOf(ThemeConstraintException.class);
    }
}
