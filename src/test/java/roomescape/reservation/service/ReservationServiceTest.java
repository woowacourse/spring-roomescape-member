package roomescape.reservation.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import roomescape.config.DatabaseCleaner;
import roomescape.reservation.domain.Description;
import roomescape.reservation.domain.ReservationTime;
import roomescape.reservation.domain.Theme;
import roomescape.reservation.domain.ThemeName;
import roomescape.reservation.dto.ReservationSaveRequest;
import roomescape.reservation.repository.ReservationTimeRepository;
import roomescape.reservation.repository.ThemeRepository;

@SpringBootTest(webEnvironment = WebEnvironment.NONE)
class ReservationServiceTest {

    @Autowired
    private DatabaseCleaner databaseCleaner;

    @Autowired
    private ThemeRepository themeRepository;

    @Autowired
    private ReservationTimeRepository reservationTimeRepository;

    @Autowired
    private ReservationService reservationService;

    @AfterEach
    void init() {
        databaseCleaner.cleanUp();
    }

    @Test
    @DisplayName("존재하지 않는 예약 시간에 예약을 하면 예외가 발생한다.")
    void notExistReservationTimeIdExceptionTest() {
        Theme theme = new Theme(new ThemeName("공포"), new Description("호러 방탈출"), "http://asdf.jpg");
        Long themeId = themeRepository.save(theme);

        ReservationSaveRequest reservationSaveRequest = new ReservationSaveRequest("호기", LocalDate.now(), themeId, 1L);

        assertThatThrownBy(() -> reservationService.save(reservationSaveRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("중복된 예약이 있다면 예외가 발생한다.")
    void duplicateReservationExceptionTest() {
        Theme theme = new Theme(new ThemeName("공포"), new Description("호러 방탈출"), "http://asdf.jpg");
        Long themeId = themeRepository.save(theme);

        LocalTime localTime = LocalTime.parse("10:00");
        ReservationTime reservationTime = new ReservationTime(localTime);
        Long timeId = reservationTimeRepository.save(reservationTime);

        LocalDate localDate = LocalDate.now();
        ReservationSaveRequest request = new ReservationSaveRequest("카키", localDate, themeId, timeId);
        reservationService.save(request);

        ReservationSaveRequest duplicateRequest = new ReservationSaveRequest("호기", localDate, themeId, timeId);
        assertThatThrownBy(() -> reservationService.save(duplicateRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("예약 아이디로 조회 시 존재하지 않는 아이디면 예외가 발생한다.")
    void findByIdExceptionTest() {
        assertThatThrownBy(() -> reservationService.findById(1L))
                .isInstanceOf(IllegalArgumentException.class);
    }
}

