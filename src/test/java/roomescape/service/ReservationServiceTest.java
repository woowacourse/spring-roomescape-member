package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.domain.Reservation;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;

@JdbcTest
class ReservationServiceTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private ReservationService reservationService;

    private LocalDate date = LocalDate.parse("2023-08-05");

    @BeforeEach
    void setup() {
        jdbcTemplate.update("DELETE FROM reservation;");
        jdbcTemplate.update("ALTER TABLE reservation ALTER COLUMN id RESTART WITH 1;");

        ReservationRepository reservationRepository = new ReservationRepository(jdbcTemplate);
        ReservationTimeRepository reservationTimeRepository = new ReservationTimeRepository(jdbcTemplate);
        ThemeRepository themeRepository = new ThemeRepository(jdbcTemplate);
        this.reservationService = new ReservationService(reservationRepository, reservationTimeRepository,
                themeRepository);
    }

    @Test
    void 예약_생성_테스트() {
        // when
        Reservation result = reservationService.create("브라운", date, 1L, 1L);

        // then
        assertAll(
                () -> assertThat(result.getId()).isNotNull(),
                () -> assertThat(result.getName()).isEqualTo("브라운"),
                () -> assertThat(result.getDate()).isEqualTo(date)
        );
    }

    @Test
    void 전체_예약_조회_테스트() {
        // given
        reservationService.create("브라운", date, 1L, 1L);
        reservationService.create("구구", date, 2L, 1L);

        // when
        List<Reservation> result = reservationService.findAll();

        // then
        assertThat(result).hasSize(2);
    }

    @Test
    void 예약_삭제_테스트() {
        // given
        Reservation created = reservationService.create("브라운", date, 1L, 1L);

        // when
        reservationService.delete(created.getId());

        // then
        assertThat(reservationService.findAll()).isEmpty();
    }

    @Test
    void 존재하지않는_timeId로_예약_생성_시_예외_발생() {
        // when & then
        assertThatThrownBy(() -> reservationService.create("홍길동", date, 999L, 1L))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("[ERROR] 존재하지 않는 예약 시간입니다.");
    }

    @Test
    void 존재하지않는_themeId로_예약_생성_시_예외_발생() {
        // when & then
        assertThatThrownBy(() -> reservationService.create("홍길동", date, 1L, 999L))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("[ERROR] 존재하지 않는 테마입니다.");
    }

    @Test
    void 존재하지않는_id의_예약_삭제_시_예외_발생() {
        // when & then
        assertThatThrownBy(() -> reservationService.delete(999L))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("[ERROR] 존재하지 않는 ID입니다.");
    }

    @Test
    void 중복_예약_시_예외_발생() {
        // given
        reservationService.create("브라운", date, 1L, 1L);

        // when & then
        assertThatThrownBy(() -> reservationService.create("브라운", date, 1L, 1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 이미 예약된 시간입니다.");
    }
}
