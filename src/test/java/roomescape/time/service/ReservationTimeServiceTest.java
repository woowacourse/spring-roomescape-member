package roomescape.time.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import roomescape.exception.DuplicateResourceException;
import roomescape.exception.ResourceInUseException;
import roomescape.exception.ResourceNotFoundException;
import roomescape.support.FixedClockConfig;
import roomescape.time.controller.dto.ReservationTimeRequest;
import roomescape.time.domain.ReservationTime;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Transactional
@Import(FixedClockConfig.class)
class ReservationTimeServiceTest {

    @Autowired
    private ReservationTimeService reservationTimeService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Nested
    @DisplayName("save 메서드는")
    class Save {

        @Test
        @DisplayName("새로운 예약 시간이면 정상적으로 저장한다.")
        void saveSuccess() {
            // given
            ReservationTimeRequest request = new ReservationTimeRequest(LocalTime.of(10, 0));

            // when
            ReservationTime saved = reservationTimeService.save(request);

            // then
            assertThat(saved.getId()).isNotNull();
            assertThat(saved.getStartAt()).isEqualTo(LocalTime.of(10, 0));
        }

        @Test
        @DisplayName("이미 존재하는 시간이면 DuplicateResourceException 이 발생한다.")
        void saveFailWhenDuplicate() {
            // given
            reservationTimeService.save(new ReservationTimeRequest(LocalTime.of(10, 0)));

            // when & then
            assertThatThrownBy(() ->
                    reservationTimeService.save(new ReservationTimeRequest(LocalTime.of(10, 0)))
            )
                    .isInstanceOf(DuplicateResourceException.class)
                    .hasMessageContaining("해당 시간이 이미 존재합니다.");
        }
    }

    @Nested
    @DisplayName("getById 메서드는")
    class GetById {

        @Test
        @DisplayName("존재하는 ID 면 해당 시간을 반환한다.")
        void getByIdSuccess() {
            // given
            ReservationTime saved = reservationTimeService.save(new ReservationTimeRequest(LocalTime.of(10, 0)));

            // when
            ReservationTime result = reservationTimeService.getById(saved.getId());

            // then
            assertThat(result.getStartAt()).isEqualTo(LocalTime.of(10, 0));
        }

        @Test
        @DisplayName("존재하지 않는 ID 면 ResourceNotFoundException 이 발생한다.")
        void getByIdFailWhenNotFound() {
            assertThatThrownBy(() -> reservationTimeService.getById(999L))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("해당 ID의 예약 시간이 존재하지 않습니다.");
        }
    }

    @Nested
    @DisplayName("deleteById 메서드는")
    class DeleteById {

        @Test
        @DisplayName("참조되지 않는 시간은 정상적으로 삭제한다.")
        void deleteByIdSuccess() {
            // given
            ReservationTime saved = reservationTimeService.save(new ReservationTimeRequest(LocalTime.of(10, 0)));

            // when
            reservationTimeService.deleteById(saved.getId());

            // then
            assertThat(reservationTimeService.findAll()).isEmpty();
        }

        @Test
        @DisplayName("예약에 사용 중인 시간은 ResourceInUseException 이 발생하고 삭제되지 않는다.")
        void deleteByIdFailWhenInUse() {
            // given
            ReservationTime savedTime = reservationTimeService.save(new ReservationTimeRequest(LocalTime.of(10, 0)));
            Long themeId = insertTheme("테마", "설명", "https://example.com/a.png");
            insertReservation("브라운", LocalDate.of(2026, 12, 31), savedTime.getId(), themeId);

            // when & then
            assertThatThrownBy(() -> reservationTimeService.deleteById(savedTime.getId()))
                    .isInstanceOf(ResourceInUseException.class)
                    .hasMessageContaining("이 시간을 참조하는 예약이 있어 삭제할 수 없습니다.");

            assertThat(reservationTimeService.findAll()).hasSize(1);
        }
    }

    @Nested
    @DisplayName("findAll 메서드는")
    class FindAll {

        @Test
        @DisplayName("저장된 모든 시간을 시작 시각 오름차순으로 반환한다.")
        void findAllReturnsSorted() {
            // given
            reservationTimeService.save(new ReservationTimeRequest(LocalTime.of(13, 0)));
            reservationTimeService.save(new ReservationTimeRequest(LocalTime.of(10, 0)));

            // when
            List<ReservationTime> result = reservationTimeService.findAll();

            // then
            assertThat(result).extracting(ReservationTime::getStartAt)
                    .containsExactly(LocalTime.of(10, 0), LocalTime.of(13, 0));
        }

        @Test
        @DisplayName("등록된 시간이 없으면 빈 목록을 반환한다.")
        void findAllReturnsEmpty() {
            assertThat(reservationTimeService.findAll()).isEmpty();
        }
    }

    @Nested
    @DisplayName("findAvailableTimes 메서드는")
    class FindAvailableTimes {

        @Test
        @DisplayName("주어진 날짜/테마에 이미 예약된 시간은 제외하고 반환한다.")
        void findAvailableTimesExcludesBooked() {
            // given
            ReservationTime t10 = reservationTimeService.save(new ReservationTimeRequest(LocalTime.of(10, 0)));
            ReservationTime t11 = reservationTimeService.save(new ReservationTimeRequest(LocalTime.of(11, 0)));
            ReservationTime t12 = reservationTimeService.save(new ReservationTimeRequest(LocalTime.of(12, 0)));

            Long themeId = insertTheme("테마", "설명", "https://example.com/a.png");
            LocalDate date = LocalDate.of(2026, 12, 31);
            insertReservation("브라운", date, t11.getId(), themeId);

            // when
            List<ReservationTime> available = reservationTimeService.findAvailableTimes(themeId, date);

            // then
            assertThat(available).extracting(ReservationTime::getStartAt)
                    .containsExactly(LocalTime.of(10, 0), LocalTime.of(12, 0));
        }
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

    private void insertReservation(String name, LocalDate date, Long timeId, Long themeId) {
        jdbcTemplate.update(
                "INSERT INTO reservation (name, reservation_date, time_id, theme_id) VALUES (?, ?, ?, ?)",
                name, date.toString(), timeId, themeId
        );
    }
}
