package roomescape.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.dao.ReservationDao;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.dto.ReservationTimeStatusResponse;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ThemeServiceTest {
    @Autowired
    private ThemeService themeService;
    @Autowired
    private ReservationService reservationService;
    @Autowired
    private ReservationTimeService reservationTimeService;
    @Autowired
    private ReservationDao reservationDao;

    @Test
    void 테마를_저장하고_조회한다() {
        Theme savedTheme = themeService.save(new Theme("공포", "무서움", "https://roomescape.com"));
        List<Theme> themes = themeService.findAll();
        assertThat(themes.getFirst().getId()).isEqualTo(savedTheme.getId());
    }

    @Test
    void 테마를_저장하고_삭제한다() {
        Theme savedTheme = themeService.save(new Theme("공포", "무서움", "https://roomescape.com"));
        themeService.deleteById(savedTheme.getId());
        assertThat(themeService.findAll()).hasSize(0);
    }

    @Test
    void 예약_가능한_시간_필터링(){
        LocalDate targetDate = LocalDate.of(2030, 5, 6);

        ReservationTime targetTime1 = reservationTimeService.save(new ReservationTime(LocalTime.of(10, 0)));
        ReservationTime targetTime2 = reservationTimeService.save(new ReservationTime(LocalTime.of(11, 0)));
        ReservationTime otherDateTime = reservationTimeService.save(new ReservationTime(LocalTime.of(12, 0)));
        ReservationTime otherThemeTime = reservationTimeService.save(new ReservationTime(LocalTime.of(13, 0)));

        Theme targetTheme = themeService.save(new Theme("공포", "무서움", "https://roomescape.com/horror"));
        Theme otherTheme = themeService.save(new Theme("판타지", "신비로움", "https://roomescape.com/fantasy"));

        reservationService.save("맥스", targetDate, targetTime1.getId(), targetTheme.getId());
        reservationService.save("피노", targetDate, targetTime2.getId(), targetTheme.getId());

        reservationService.save("브라운", LocalDate.of(2030, 5, 5), otherDateTime.getId(), targetTheme.getId());
        reservationService.save("포비", targetDate, otherThemeTime.getId(), otherTheme.getId());

        assertThat(reservationTimeService.findReservationTimeByDateAndThemeId(targetDate, targetTheme.getId()))
                .extracting(ReservationTimeStatusResponse::available)
                .containsExactlyInAnyOrder(true, true, false, false);
    }
}
