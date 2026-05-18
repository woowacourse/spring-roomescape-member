package roomescape.infrastructure;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import roomescape.entity.Reservation;
import roomescape.entity.ReservationTime;
import roomescape.entity.Theme;
import roomescape.entity.ThemeSortType;

@JdbcTest
@Import({
        ThemeJdbcTemplateRepository.class,
        ReservationTimeJdbcTemplateRepository.class,
        ReservationJdbcTemplateRepository.class
})
class ThemeJdbcTemplateRepositoryTest {

    private static final String TEST_THEMA_NAME = "테스트 테마";
    private static final String TEST_THEMA_DESCRIPTION = "테스트 테마 설명";
    private static final String TEST_THEMA_THUMBNAIL = "https://good.com/thumb-nail";

    private final ThemeJdbcTemplateRepository themeRepository;
    private final ReservationTimeJdbcTemplateRepository timeRepository;
    private final ReservationJdbcTemplateRepository reservationRepository;

    @Autowired
    public ThemeJdbcTemplateRepositoryTest(ThemeJdbcTemplateRepository themeRepository,
                                           ReservationTimeJdbcTemplateRepository timeRepository,
                                           ReservationJdbcTemplateRepository reservationRepository) {
        this.themeRepository = themeRepository;
        this.timeRepository = timeRepository;
        this.reservationRepository = reservationRepository;
    }

    @Test
    @DisplayName("테마를 잘 저장한다")
    void save_success() {
        // given
        Theme theme = Theme.createWithNullId(TEST_THEMA_NAME, TEST_THEMA_DESCRIPTION, TEST_THEMA_THUMBNAIL);

        // when
        Theme savedTheme = themeRepository.save(theme);

        // then
        Assertions.assertNotNull(savedTheme.id());

        Theme expect = theme.appendId(savedTheme.id());
        Assertions.assertEquals(expect, savedTheme);
    }

    @Test
    @DisplayName("아이디를 기반으로 테마를 찾는다")
    void findById_success() {
        // given
        Theme saved = themeRepository.save(Theme.createWithNullId(TEST_THEMA_NAME, TEST_THEMA_DESCRIPTION, TEST_THEMA_THUMBNAIL));

        // when
        Optional<Theme> foundTheme = themeRepository.findById(saved.id());

        // then
        Assertions.assertTrue(foundTheme.isPresent());
        Assertions.assertEquals(saved.id(), foundTheme.get().id());
        Assertions.assertEquals(TEST_THEMA_NAME, foundTheme.get().name());
    }

    @Test
    @DisplayName("아이디를 기반으로 테마를 찾는다 - 없어도 오류가 발생하지 않는다. - Optional<Empty> 반환")
    void findById_success_even_if_no_theme() {
        // when
        Optional<Theme> foundTheme = themeRepository.findById(999L);

        // then
        Assertions.assertTrue(foundTheme.isEmpty());
    }

    @Test
    @DisplayName("모든 테마를 가져온다")
    void findAll_success() {
        // given
        themeRepository.save(Theme.createWithNullId("테마1", "설명1", "https://theme1.com"));
        themeRepository.save(Theme.createWithNullId("테마2", "설명2", "https://theme2.com"));

        // when
        List<Theme> result = themeRepository.findAll();

        // then
        Assertions.assertEquals(2, result.size());
    }

    @Test
    @DisplayName("모든 테마를 가져온다 - 없는 경우에는 빈 리스트 반환")
    void findAll_success_even_if_no_theme() {
        // when
        List<Theme> result = themeRepository.findAll();

        // then
        Assertions.assertEquals(0, result.size());
    }

    @Test
    @DisplayName("아이디를 기반으로 테마를 삭제한다")
    void deleteById_success() {
        // given
        Theme saved = themeRepository.save(Theme.createWithNullId("삭제 대상", "설명", "https://delete.com"));

        // when
        themeRepository.deleteById(saved.id());

        // then
        Assertions.assertTrue(themeRepository.findById(saved.id()).isEmpty());
    }

    @Test
    @DisplayName("없는 ID 기반으로 테마를 삭제해도 오류가 발생하지 않는다")
    void deleteById_success_even_if_no_theme() {
        // when & then
        Assertions.assertDoesNotThrow(
                () -> themeRepository.deleteById(999L)
        );
    }

    @Test
    @DisplayName("기간 내 예약 수가 많은 테마를 상위 N개 조회한다")
    void findTopNByPeriod_success() {
        // given
        ReservationTime time1 = timeRepository.save(ReservationTime.createWithNullId(LocalTime.of(10, 0)));
        ReservationTime time2 = timeRepository.save(ReservationTime.createWithNullId(LocalTime.of(11, 0)));
        ReservationTime time3 = timeRepository.save(ReservationTime.createWithNullId(LocalTime.of(12, 0)));

        Theme theme1 = themeRepository.save(Theme.createWithNullId("인기 테마", "설명1", "thumb1"));
        Theme theme2 = themeRepository.save(Theme.createWithNullId("덜 인기 테마", "설명2", "thumb2"));

        LocalDate future1 = LocalDate.now().plusDays(1);
        LocalDate future2 = LocalDate.now().plusDays(2);
        LocalDate future3 = LocalDate.now().plusDays(3);

        reservationRepository.save(Reservation.createWithNullId("예약자1", future1, time1, theme1));
        reservationRepository.save(Reservation.createWithNullId("예약자2", future2, time2, theme1));
        reservationRepository.save(Reservation.createWithNullId("예약자3", future3, time3, theme2));

        // when
        List<Theme> result = themeRepository.findTopNByPeriod(
                future1,
                future3,
                ThemeSortType.POPULAR,
                2L
        );

        // then
        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals(theme1.id(), result.get(0).id());
        Assertions.assertEquals(theme2.id(), result.get(1).id());
    }
}
