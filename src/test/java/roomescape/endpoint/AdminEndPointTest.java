package roomescape.endpoint;

import static org.hamcrest.Matchers.is;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationRepository;
import roomescape.domain.ReservationTime;
import roomescape.domain.ReservationTimeRepository;
import roomescape.domain.Theme;
import roomescape.domain.ThemeRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class AdminEndPointTest {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ReservationTimeRepository reservationTimeRepository;

    @Autowired
    private ThemeRepository themeRepository;

    @DisplayName("관리자 메인 페이지 응답")
    @Test
    void adminPageLoad() {
        HttpRestTestTemplate.assertGetOk("/admin");
    }

    @DisplayName("예약 페이지 응답")
    @Test
    void reservationPageLoad() {
        List<Reservation> reservations = reservationRepository.findAll();
        int reservationSize = reservations.size();

        HttpRestTestTemplate.assertGetOk("/admin/reservation");
        HttpRestTestTemplate.assertGetOk("/reservations", "size()", is(reservationSize));
    }

    @DisplayName("시간 페이지 응답")
    @Test
    void timePageLoad() {
        List<ReservationTime> reservationTimes = reservationTimeRepository.findAll();
        int reservationTimeSize = reservationTimes.size();

        HttpRestTestTemplate.assertGetOk("/admin/time");
        HttpRestTestTemplate.assertGetOk("/times", "size()", is(reservationTimeSize));
    }

    @DisplayName("테마 페이지 응답")
    @Test
    void themePageLoad() {
        List<Theme> themes = themeRepository.findAll();
        int themesSize = themes.size();

        HttpRestTestTemplate.assertGetOk("/admin/theme");
        HttpRestTestTemplate.assertGetOk("/themes", "size()", is(themesSize));
    }
}
