package roomescape.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import roomescape.domain.Theme;
import roomescape.dto.PopularTheme;
import roomescape.exception.ThemeInUseException;

@JdbcTest
@ActiveProfiles("test")
@Import({
        ThemeDao.class,
        ReservationTimeDao.class,
        ReservationDao.class
})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ThemeDaoTest {

    @Autowired
    private ThemeDao themeDao;

    @Autowired
    private ReservationTimeDao reservationTimeDao;

    @Autowired
    private ReservationDao reservationDao;

    @Test
    void 테마_생성_테스트() {
        String name = "이든의 공포 하우스";
        String description = "이든이 귀신으로 나오는 공포 테마";
        String imgUrl = "image.jpg";
        Long id = themeDao.insertTheme(name, description, imgUrl);

        Theme actual = themeDao.findById(id);

        assertThat(actual.getId()).isNotNull();
        assertThat(actual.getName()).isEqualTo(name);
        assertThat(actual.getDescription()).isEqualTo(description);
        assertThat(actual.getImgUrl()).isEqualTo(imgUrl);
    }

    @Test
    void 테마_삭제_테스트() {
        Long id = themeDao.insertTheme("이든의 공포 하우스", "이든이 귀신으로 나오는 공포 테마", "image.jpg");

        int deleteCount = themeDao.delete(id);

        assertThat(deleteCount).isEqualTo(1);
    }

    @Test
    void 예약이_존재하는_테마_삭제_시_예외_발생() {
        Long themeId = themeDao.insertTheme("이든의 공포 하우스", "이든이 귀신으로 나오는 공포 테마", "image.jpg");
        Long timeId = reservationTimeDao.insertReservationTime(LocalTime.of(10, 0));
        reservationDao.insertReservation("이든", LocalDate.of(2026, 5, 6), timeId, themeId);

        assertThatThrownBy(() -> themeDao.delete(themeId))
                .isInstanceOf(ThemeInUseException.class);
    }

    @Test
    @Sql("/popular-themes.sql")
    void 최근_1주동안_예약이_많았던_테마_상위_10개를_조회한다() {
        List<PopularTheme> popularThemes = themeDao.findPopularThemes(
                LocalDate.of(2026, 5, 1), LocalDate.of(2026, 5, 7));

        assertThat(popularThemes).hasSize(10);
        assertThat(popularThemes)
                .extracting(PopularTheme::name)
                .containsExactly(
                        "이든의 공포 하우스",
                        "정콩이의 방탈출",
                        "우주 정거장 탈출",
                        "고대 유적의 비밀",
                        "마법사의 서재",
                        "좀비 연구소",
                        "해적선의 보물",
                        "미스터리 호텔",
                        "시간의 문",
                        "사라진 탐정"
                );
        assertThat(popularThemes)
                .extracting(PopularTheme::rank)
                .containsExactly(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L);
    }
}
