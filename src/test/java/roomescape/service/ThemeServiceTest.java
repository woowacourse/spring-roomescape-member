package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import roomescape.dao.FakeDatabase;
import roomescape.dao.FakeReservationDao;
import roomescape.dao.FakeReservationTimeDao;
import roomescape.dao.FakeThemeDao;
import roomescape.dao.ReservationDao;
import roomescape.dao.ReservationTimeDao;
import roomescape.dao.ThemeDao;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.service.dto.ServiceThemeRequest;
import roomescape.service.dto.ServiceThemeResponse;

public class ThemeServiceTest {

    private ThemeService themeService;

    private ThemeDao themeDao;

    private ReservationTimeDao reservationTimeDao;

    private ReservationDao reservationDao;

    @BeforeEach
    void beforeEach() {
        FakeDatabase fakeDatabase = new FakeDatabase();

        reservationDao = new FakeReservationDao(fakeDatabase);
        reservationTimeDao = new FakeReservationTimeDao(fakeDatabase);
        themeDao = new FakeThemeDao(fakeDatabase);

        themeService = new ThemeService(themeDao, reservationDao);
    }

    @Test
    void createTest() {
        String name = "피즈의 모험";
        String description = "모험 이야기";
        String thumbnailUrl = "url.jpg";
        ServiceThemeRequest requestDto = new ServiceThemeRequest(name, description, thumbnailUrl);

        ServiceThemeResponse responseDto = themeService.create(requestDto);

        assertThat(responseDto).isEqualTo(
                new ServiceThemeResponse(
                        1L,
                        name,
                        description,
                        thumbnailUrl
                )
        );
    }

    @Test
    void readAllTest() {
        themeService.create(new ServiceThemeRequest(
                "피즈의 모험",
                "모험 이야기",
                "url.jpg"
        ));
        themeService.create(new ServiceThemeRequest(
                "나무의 일대기",
                "모험 이야기",
                "url.jpg"
        ));

        List<ServiceThemeResponse> responseDtos = themeService.readAll();

        assertThat(responseDtos.size()).isEqualTo(2);
        assertThat(responseDtos.get(0)).isEqualTo(new ServiceThemeResponse(
                1L,
                "피즈의 모험",
                "모험 이야기",
                "url.jpg"
        ));
        assertThat(responseDtos.get(1)).isEqualTo(new ServiceThemeResponse(
                2L,
                "나무의 일대기",
                "모험 이야기",
                "url.jpg"
        ));
    }

    @Test
    void deleteTest() {
        themeService.create(new ServiceThemeRequest(
                "피즈의 모험",
                "모험 이야기",
                "url.jpg"
        ));

        themeService.delete(1L);

        List<ServiceThemeResponse> responseDtos = themeService.readAll();
        assertThat(responseDtos.size()).isEqualTo(0);
    }

    @Test
    void readRankingTest() {
        ReservationTime reservationTime = reservationTimeDao.create(new ReservationTime(LocalTime.of(10, 0)));
        Theme theme1 = themeDao.create(new Theme("피즈의 모험", "모험 이야기", "url.jpg"));
        Theme theme2 = themeDao.create(new Theme("피즈의 모험2", "모험 이야기", "url.jpg"));

        reservationDao.create(new Reservation("fizz", LocalDate.of(2026, 5, 2), reservationTime, theme1));
        reservationDao.create(new Reservation("fizz", LocalDate.of(2026, 5, 4), reservationTime, theme1));
        reservationDao.create(new Reservation("fizz2", LocalDate.of(2026, 5, 4), reservationTime, theme2));

        List<ServiceThemeResponse> responseDtos = themeService.readRanking(LocalDate.of(2026, 5, 1),
                LocalDate.of(2026, 5, 7));

        assertThat(responseDtos.get(0).name()).isEqualTo("피즈의 모험");
        assertThat(responseDtos.get(1).name()).isEqualTo("피즈의 모험2");
    }
}
