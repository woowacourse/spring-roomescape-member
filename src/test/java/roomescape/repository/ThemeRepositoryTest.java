package roomescape.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

@JdbcTest
@Import({ThemeRepository.class, ReservationTimeRepository.class, ReservationRepository.class})
class ThemeRepositoryTest {

    @Autowired
    private ThemeRepository themeRepository;

    @Autowired
    private ReservationTimeRepository reservationTimeRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    private Theme savedTheme;

    @BeforeEach
    void setUp() {
        savedTheme = themeRepository.save(Theme.of("공포", "무서운 테마", "https://example.com/img.jpg"));
    }

    @Test
    void 테마를_저장한다() {
        assertThat(savedTheme.getId()).isPositive();
        assertThat(savedTheme.getName()).isEqualTo("공포");
        assertThat(savedTheme.getDescription()).isEqualTo("무서운 테마");
    }

    @Test
    void id로_테마를_조회한다() {
        Optional<Theme> found = themeRepository.findById(savedTheme.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("공포");
    }

    @Test
    void 전체_테마를_조회한다() {
        themeRepository.save(Theme.of("추리", "머리 쓰는 테마", "https://example.com/img2.jpg"));

        List<Theme> themes = themeRepository.findAll();

        assertThat(themes).hasSize(2);
    }

    @Test
    void 테마를_삭제한다() {
        themeRepository.deleteById(savedTheme.getId());

        assertThat(themeRepository.findById(savedTheme.getId())).isEmpty();
    }

    @Test
    void 인기_테마를_예약_횟수_기준으로_조회한다() {
        Theme theme2 = themeRepository.save(Theme.of("추리", "머리 쓰는 테마", "https://example.com/img2.jpg"));
        ReservationTime time = reservationTimeRepository.save(ReservationTime.of("10:00"));

        LocalDate yesterday = LocalDate.now().minusDays(1);
        LocalDate twoDaysAgo = LocalDate.now().minusDays(2);

        reservationRepository.save(Reservation.of("유저1", yesterday.toString(), time, savedTheme));
        reservationRepository.save(Reservation.of("유저2", twoDaysAgo.toString(), time, savedTheme));
        reservationRepository.save(Reservation.of("유저3", yesterday.toString(), time, theme2));

        List<Theme> famous = themeRepository.findFamous(7, LocalDate.now(), 10);

        assertThat(famous).hasSize(2);
        assertThat(famous.get(0).getId()).isEqualTo(savedTheme.getId());
        assertThat(famous.get(1).getId()).isEqualTo(theme2.getId());
    }

    @Test
    void 조회_기간_밖의_예약은_인기_테마에_포함되지_않는다() {
        ReservationTime time = reservationTimeRepository.save(ReservationTime.of("10:00"));
        LocalDate eightDaysAgo = LocalDate.now().minusDays(8);

        reservationRepository.save(Reservation.of("유저1", eightDaysAgo.toString(), time, savedTheme));

        List<Theme> famous = themeRepository.findFamous(7, LocalDate.now(), 10);

        assertThat(famous).isEmpty();
    }
}
