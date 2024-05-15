package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.domain.exception.InvalidValueException;
import roomescape.dto.reservationtime.ReservationTimeCreateRequest;
import roomescape.dto.reservationtime.ReservationTimeResponse;
import roomescape.service.exception.InvalidRequestException;

class ReservationTimeServiceTest extends BaseServiceTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private ReservationTimeService reservationTimeService;

    @Nested
    @DisplayName("예약 시간 조회 테스트")
    class FindReservationTime {

        @Test
        @DisplayName("모든 예약 시간 정보를 조회한다.")
        void findAll() {
            jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES ('12:12')");
            jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES ('12:20')");

            List<ReservationTimeResponse> results = reservationTimeService.findAll();

            assertThat(results).hasSize(2);
        }
    }

    @Nested
    @DisplayName("예약 시간 생성 테스트")
    class CreteNewReservationTime {

        @Test
        @DisplayName("예약 시간을 추가한다.")
        void add() {
            ReservationTimeCreateRequest request = ReservationTimeCreateRequest.from("12:12");

            ReservationTimeResponse result = reservationTimeService.add(request);

            assertAll(
                    () -> assertThat(reservationTimeService.findAll()).hasSize(1),
                    () -> assertThat(result.startAt()).isEqualTo("12:12")
            );
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {" ", "   "})
        @DisplayName("예약 시간이 공백이면 예외가 발생한다.")
        void addReservationTimeByNullOrEmptyStartAt(String startAt) {
            ReservationTimeCreateRequest request = ReservationTimeCreateRequest.from(startAt);

            assertThatThrownBy(() -> reservationTimeService.add(request))
                    .isInstanceOf(InvalidValueException.class);
        }

        @Test
        @DisplayName("중복된 예약시간을 추가하면 예외가 발생한다.")
        void addReservationTimeByDuplicateStartAt() {
            jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES ('12:12')");
            ReservationTimeCreateRequest request = ReservationTimeCreateRequest.from("12:12");

            assertThatThrownBy(() -> reservationTimeService.add(request))
                    .isInstanceOf(InvalidRequestException.class);
        }
    }

    @Nested
    @DisplayName("예약 시간 삭제 테스트")
    class DeleteReservationTime {

        @Test
        @DisplayName("예약 시간을 삭제한다.")
        void delete() {
            jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES ('12:12')");

            reservationTimeService.delete(1L);

            assertThat(reservationTimeService.findAll()).isEmpty();
        }

        @Test
        @DisplayName("예약 시간 삭제시 아이디가 존재하지 않으면 예외가 발생한다.")
        void deleteByNotExistId() {
            assertThatThrownBy(() -> reservationTimeService.delete(-1L))
                    .isInstanceOf(InvalidRequestException.class);
        }

        @Test
        @DisplayName("예약 시간 삭제시 아이디가 null이면 예외가 발생한다.")
        void deleteByNullOrEmptyId() {
            assertThatThrownBy(() -> reservationTimeService.delete(null))
                    .isInstanceOf(InvalidRequestException.class);
        }

        @Test
        @DisplayName("특정 시간에 대한 예약이 존재할 때, 해당 시간을 삭제하면 예외가 발생한다.")
        void deleteWhenReservationExist() {
            jdbcTemplate.update(
                    "INSERT INTO member (name, email, password, role) VALUES ('사용자1', 'user1@wooteco.com', 'user1', 'USER')");
            jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES ('12:12')");
            jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES ('방탈출1', '1번 방탈출', '썸네일1')");
            jdbcTemplate.update(
                    "INSERT INTO reservation (date, member_id, time_id, theme_id) VALUES ('2024-05-01', '1', '1', '1')");

            assertThatThrownBy(() -> reservationTimeService.delete(1L))
                    .isInstanceOf(InvalidRequestException.class);
        }
    }
}
