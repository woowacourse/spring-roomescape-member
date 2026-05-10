package roomescape.reservation.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import roomescape.exception.DuplicateResourceException;
import roomescape.reservation.controller.dto.ReservationRequest;
import roomescape.reservation.domain.Reservation;
import roomescape.support.FixedClockConfig;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
@Import(FixedClockConfig.class)
class ReservationServiceTest {

    private static final LocalDate FUTURE_DATE = LocalDate.of(2026, 12, 31);
    private static final LocalDate TODAY = LocalDate.of(2026, 5, 6);

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private Long timeId10;
    private Long timeId08;
    private Long themeId;

    @BeforeEach
    void setUp() {
        timeId10 = insertReservationTime(LocalTime.of(10, 0));
        timeId08 = insertReservationTime(LocalTime.of(8, 0));
        themeId = insertTheme("우테코", "우테코 전용 테마", "https://example.com/thumb.jpg");
    }

    @Nested
    @DisplayName("save 메서드는")
    class Save {

        @Test
        @DisplayName("정상 요청이면 예약을 저장하고 생성된 예약을 반환한다.")
        void saveSuccess() {
            // given
            ReservationRequest request = new ReservationRequest("브라운", FUTURE_DATE, timeId10, themeId);

            // when
            Reservation saved = reservationService.save(request);

            // then
            assertThat(saved.getId()).isNotNull();
            assertThat(saved.getName()).isEqualTo("브라운");
            assertThat(saved.getDate()).isEqualTo(FUTURE_DATE);
            assertThat(saved.getTime().getId()).isEqualTo(timeId10);
            assertThat(saved.getTheme().getId()).isEqualTo(themeId);
        }

        @Test
        @DisplayName("예약 일자가 과거이면 IllegalArgumentException 이 발생하고 저장되지 않는다.")
        void saveFailWhenPastDate() {
            // given
            LocalDate pastDate = LocalDate.of(2026, 5, 5);
            ReservationRequest request = new ReservationRequest("브라운", pastDate, timeId10, themeId);

            // when & then
            assertThatThrownBy(() -> reservationService.save(request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("과거 시각");

            assertThat(reservationService.findAll()).isEmpty();
        }

        @Test
        @DisplayName("오늘 날짜라도 예약 시간이 현재 시각보다 이전이면 예외가 발생한다.")
        void saveFailWhenTodayButPastTime() {
            // given - 고정 시각 09:00, 시간 슬롯은 08:00
            ReservationRequest request = new ReservationRequest("브라운", TODAY, timeId08, themeId);

            // when & then
            assertThatThrownBy(() -> reservationService.save(request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("과거 시각");

            assertThat(reservationService.findAll()).isEmpty();
        }

        @Test
        @DisplayName("동일한 날짜/시간/테마 조합의 예약이 이미 존재하면 DuplicateResourceException 이 발생한다.")
        void saveFailWhenDuplicate() {
            // given
            ReservationRequest request = new ReservationRequest("브라운", FUTURE_DATE, timeId10, themeId);
            reservationService.save(request);

            ReservationRequest duplicate = new ReservationRequest("제임스", FUTURE_DATE, timeId10, themeId);

            // when & then
            assertThatThrownBy(() -> reservationService.save(duplicate))
                    .isInstanceOf(DuplicateResourceException.class)
                    .hasMessageContaining("이미 해당 날짜와 시간에 예약이 존재합니다.");

            assertThat(reservationService.findAll()).hasSize(1);
        }
    }

    @Nested
    @DisplayName("deleteById 메서드는")
    class DeleteById {

        @Test
        @DisplayName("ID에 해당하는 예약을 삭제한다.")
        void deleteByIdRemovesReservation() {
            // given
            Reservation saved = reservationService.save(
                    new ReservationRequest("브라운", FUTURE_DATE, timeId10, themeId)
            );

            // when
            reservationService.deleteById(saved.getId());

            // then
            assertThat(reservationService.findAll()).isEmpty();
        }
    }

    @Nested
    @DisplayName("deleteAll 메서드는")
    class DeleteAll {

        @Test
        @DisplayName("모든 예약을 삭제한다.")
        void deleteAllRemovesEverything() {
            // given
            reservationService.save(new ReservationRequest("브라운", FUTURE_DATE, timeId10, themeId));
            reservationService.save(new ReservationRequest("제임스", FUTURE_DATE.plusDays(1), timeId10, themeId));

            // when
            reservationService.deleteAll();

            // then
            assertThat(reservationService.findAll()).isEmpty();
        }
    }

    @Nested
    @DisplayName("findAll 메서드는")
    class FindAll {

        @Test
        @DisplayName("저장된 예약을 ID 오름차순으로 반환한다.")
        void findAllReturnsAll() {
            // given
            reservationService.save(new ReservationRequest("브라운", FUTURE_DATE, timeId10, themeId));
            reservationService.save(new ReservationRequest("제임스", FUTURE_DATE.plusDays(1), timeId10, themeId));

            // when
            List<Reservation> result = reservationService.findAll();

            // then
            assertThat(result).extracting(Reservation::getName)
                    .containsExactly("브라운", "제임스");
        }

        @Test
        @DisplayName("예약이 없으면 빈 목록을 반환한다.")
        void findAllReturnsEmpty() {
            assertThat(reservationService.findAll()).isEmpty();
        }
    }

    @Nested
    @DisplayName("findByFilter 메서드는")
    class FindByFilter {

        @Test
        @DisplayName("date 와 themeId 조건에 모두 부합하는 예약만 반환한다.")
        void findByFilterAppliesBothConditions() {
            // given
            Long otherThemeId = insertTheme("다른 테마", "설명", "https://example.com/thumb2.jpg");

            reservationService.save(new ReservationRequest("브라운", FUTURE_DATE, timeId10, themeId));
            reservationService.save(new ReservationRequest("제임스", FUTURE_DATE, timeId10, otherThemeId));
            reservationService.save(new ReservationRequest("나나", FUTURE_DATE.plusDays(1), timeId10, themeId));

            // when
            List<Reservation> result = reservationService.findByFilter(FUTURE_DATE, themeId);

            // then
            assertThat(result).hasSize(1);
            assertThat(result.get(0).getName()).isEqualTo("브라운");
        }

        @Test
        @DisplayName("두 조건이 모두 null 이면 전체 예약을 반환한다.")
        void findByFilterReturnsAllWhenNoFilter() {
            // given
            reservationService.save(new ReservationRequest("브라운", FUTURE_DATE, timeId10, themeId));
            reservationService.save(new ReservationRequest("제임스", FUTURE_DATE.plusDays(1), timeId10, themeId));

            // when
            List<Reservation> result = reservationService.findByFilter(null, null);

            // then
            assertThat(result).hasSize(2);
        }
    }

    private Long insertReservationTime(LocalTime startAt) {
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES (?)", startAt.toString());
        return jdbcTemplate.queryForObject(
                "SELECT id FROM reservation_time WHERE start_at = ?",
                Long.class,
                startAt.toString()
        );
    }

    private Long insertTheme(String name, String description, String thumbnailUrl) {
        jdbcTemplate.update(
                "INSERT INTO theme (name, description, thumbnail_url) VALUES (?, ?, ?)",
                name, description, thumbnailUrl
        );
        return jdbcTemplate.queryForObject(
                "SELECT id FROM theme WHERE name = ?",
                Long.class,
                name
        );
    }
}
