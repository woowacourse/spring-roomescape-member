package roomescape.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.tester.AutoConfigureGraphQlTester;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.service.ReservationService;
import roomescape.service.ReservationTimeService;
import roomescape.service.ThemeService;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ReservationDaoTest {
    
    @Autowired
    private ReservationService reservationService;
    
    @Autowired
    private ReservationTimeService reservationTimeService;

    @Autowired
    private ThemeService themeService;
    @Autowired
    private ReservationDao reservationDao;

    @Test
    void 사용자가_선택한_날짜와_테마에_해당하는_예약시간Id를_가져온다() {
        // given
        LocalDate targetDate = LocalDate.of(2026, 5, 6);

        ReservationTime targetTime1 = reservationTimeService.save(new ReservationTime(LocalTime.of(10, 0)));
        ReservationTime targetTime2 = reservationTimeService.save(new ReservationTime(LocalTime.of(11, 0)));
        ReservationTime otherDateTime = reservationTimeService.save(new ReservationTime(LocalTime.of(12, 0)));
        ReservationTime otherThemeTime = reservationTimeService.save(new ReservationTime(LocalTime.of(13, 0)));

        Theme targetTheme = themeService.save(new Theme("공포", "무서움", "https://roomescape.com/horror"));
        Theme otherTheme = themeService.save(new Theme("판타지", "신비로움", "https://roomescape.com/fantasy"));

        reservationService.save("맥스", targetDate, targetTime1.getId(), targetTheme.getId());
        reservationService.save("피노", targetDate, targetTime2.getId(), targetTheme.getId());

        reservationService.save("브라운", LocalDate.of(2026, 5, 5), otherDateTime.getId(), targetTheme.getId());

        reservationService.save("포비", targetDate, otherThemeTime.getId(), otherTheme.getId());

        // when
        List<Long> timeIds = reservationDao.findReservedTimeIdsByDateAndThemeId(
                targetDate,
                targetTheme.getId()
        );

        // then
        assertThat(timeIds)
                .containsExactlyInAnyOrder(targetTime1.getId(), targetTime2.getId())
                .doesNotContain(otherDateTime.getId(), otherThemeTime.getId());
    }
}
