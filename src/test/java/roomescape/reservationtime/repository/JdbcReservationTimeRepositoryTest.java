package roomescape.reservationtime.repository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.reservationtime.domain.ReservationTime;
import roomescape.theme.domain.Theme;
import roomescape.theme.repository.JdbcThemeRepository;
import roomescape.theme.repository.ThemeRepository;

@JdbcTest
class JdbcReservationTimeRepositoryTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    ReservationTimeRepository reservationTimeRepository;
    ThemeRepository themeRepository;

    @BeforeEach
    void setUp() {
        reservationTimeRepository = new JdbcReservationTimeRepository(jdbcTemplate);

        reservationTimeRepository.save(ReservationTime.builder().startAt(LocalTime.parse("09:00")).build());
        reservationTimeRepository.save(ReservationTime.builder().startAt(LocalTime.parse("10:00")).build());
        reservationTimeRepository.save(ReservationTime.builder().startAt(LocalTime.parse("11:00")).build());

        themeRepository = new JdbcThemeRepository(jdbcTemplate);
    }


    public static Stream<Arguments> createTimes() {
        return Stream.of(
                Arguments.of(List.of(LocalTime.parse("09:00"), LocalTime.parse("10:00"), LocalTime.parse("11:00")))
        );
    }

    @DisplayName("날짜/테마 선택 시 예약 가능한 시간 조회하는 테스트입니다.")
    @ParameterizedTest
    @MethodSource("createTimes")
    void check_available_time(List<LocalTime> expectedTimes) {
        Theme theme = Theme.builder()
                .name("theme name")
                .description("theme description")
                .thumbnailImgUrl("theme img url")
                .build();
        Theme savedTheme = themeRepository.save(theme);
        LocalDate date = LocalDate.of(2026, 5, 4);

        List<ReservationTime> reservationTimes = reservationTimeRepository
                .findByThemeAndDate(savedTheme.getId(), date);

        for (int i = 0; i < 3; i++) {
            assertThat(reservationTimes.get(i).getStartAt()).isEqualTo(expectedTimes.get(i));
        }
    }
}
