package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static roomescape.domain.member.Role.USER;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import roomescape.domain.exception.InvalidValueException;
import roomescape.domain.member.Member;
import roomescape.domain.member.MemberEmail;
import roomescape.domain.member.MemberName;
import roomescape.dto.reservation.AdminReservationCreateRequest;
import roomescape.dto.reservation.AvailableReservationResponse;
import roomescape.dto.reservation.MemberReservationCreateRequest;
import roomescape.dto.reservation.ReservationResponse;
import roomescape.service.exception.InvalidRequestException;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Sql(value = "classpath:clean_data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class ReservationServiceTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private ReservationService reservationService;

    @BeforeEach
    void setUp() {
        jdbcTemplate.update(
                "INSERT INTO member (name, email, password, role) VALUES ('사용자1', 'user1@wooteco.com', 'user1', 'USER')");
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES ('12:12')");
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES ('방탈출1', '1번 방탈출', '썸네일1')");
    }

    @Nested
    @DisplayName("예약 조회 테스트")
    class FindReservation {

        @Test
        @DisplayName("모든 예약 정보를 조회한다.")
        void findAll() {
            jdbcTemplate.update(
                    "INSERT INTO reservation (date, member_id, time_id, theme_id) VALUES ('2024-05-01', '1', '1', '1')");
            jdbcTemplate.update(
                    "INSERT INTO reservation (date, member_id, time_id, theme_id) VALUES ('2024-05-01', '1', '1', '1')");

            List<ReservationResponse> reservations = reservationService.findAll();

            assertThat(reservations).hasSize(2);
        }

        @Test
        @Sql(value = {"classpath:clean_data.sql", "classpath:test_data.sql"})
        @DisplayName("테마 아이디, 멤버 아이디, 날짜 범위로 필터링된 예약 정보를 조회한다.")
        void findFiltered() {
            List<ReservationResponse> filtered = reservationService.findFiltered(
                    1L,
                    1L,
                    LocalDate.of(2024, 5, 3),
                    LocalDate.of(2024, 5, 9));

            assertThat(filtered).hasSize(1);
        }

        @Test
        @DisplayName("테마별 예약 시간을 조회하며 예약 가능 여부를 알려준다.")
        void findAvailableReservationTime() {
            jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES ('12:22')");
            jdbcTemplate.update(
                    "INSERT INTO reservation (date, member_id, time_id, theme_id) VALUES ('2024-05-01', '1', '1', '1')");

            List<AvailableReservationResponse> responses =
                    reservationService.findTimeByDateAndThemeID("2024-05-01", 1L);
            AvailableReservationResponse alreadyBooked = responses.get(0);
            AvailableReservationResponse notBooked = responses.get(1);

            assertAll(
                    () -> assertThat(responses).hasSize(2),
                    () -> assertThat(alreadyBooked.alreadyBooked()).isTrue(),
                    () -> assertThat(notBooked.alreadyBooked()).isFalse()
            );
        }
    }

    @Nested
    @DisplayName("예약 생성 테스트")
    class CreateReservation {

        @Test
        @DisplayName("관리자가 예약을 생성한다.")
        void addByAdmin() {
            String date = LocalDate.now().plusDays(1).toString();
            AdminReservationCreateRequest request = AdminReservationCreateRequest.of(
                    date,
                    1L,
                    1L,
                    1L
            );

            ReservationResponse result = reservationService.add(request);

            assertAll(
                    () -> assertThat(reservationService.findAll()).hasSize(1),
                    () -> assertThat(result.date()).isEqualTo(date),
                    () -> assertThat(result.member().id()).isSameAs(1L),
                    () -> assertThat(result.time().id()).isSameAs(1L),
                    () -> assertThat(result.theme().id()).isSameAs(1L)
            );
        }

        @Test
        @DisplayName("유저가 예약을 생성한다.")
        void addByMember() {
            String date = LocalDate.now().plusDays(1).toString();
            MemberReservationCreateRequest request = MemberReservationCreateRequest.of(
                    date,
                    1L,
                    1L
            );
            Member member = new Member(
                    1L,
                    new MemberName("사용자1"),
                    new MemberEmail("user1@wooteco.com"),
                    "user1", USER
            );

            ReservationResponse result = reservationService.add(request, member);

            assertAll(
                    () -> assertThat(reservationService.findAll()).hasSize(1),
                    () -> assertThat(result.date()).isEqualTo(date),
                    () -> assertThat(result.member().id()).isSameAs(1L),
                    () -> assertThat(result.time().id()).isSameAs(1L),
                    () -> assertThat(result.theme().id()).isSameAs(1L)
            );
        }

        @Test
        @DisplayName("존재하지 않는 시간 아이디로 예약 생성시 예외가 발생한다.")
        void addByNotExistTimeId() {
            String date = LocalDate.now().plusDays(1).toString();
            AdminReservationCreateRequest request = AdminReservationCreateRequest.of(
                    date,
                    1L,
                    -1L,
                    1L
            );

            Assertions.assertThatThrownBy(() -> reservationService.add(request))
                    .isInstanceOf(InvalidRequestException.class);
        }

        @Test
        @DisplayName("존재하지 않는 테마 아이디로 예약 생성시 예외가 발생한다.")
        void addByNotExistThemeId() {
            String date = LocalDate.now().plusDays(1).toString();
            AdminReservationCreateRequest request = AdminReservationCreateRequest.of(
                    date,
                    1L,
                    1L,
                    -1L
            );

            Assertions.assertThatThrownBy(() -> reservationService.add(request))
                    .isInstanceOf(InvalidRequestException.class);
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {" ", "   "})
        @DisplayName("예약 날짜가 공백이면 예외가 발생한다.")
        void addByNullOrEmptyDate(String date) {
            AdminReservationCreateRequest request = AdminReservationCreateRequest.of(
                    date,
                    1L,
                    1L,
                    1L
            );

            assertThatThrownBy(() -> reservationService.add(request))
                    .isInstanceOf(InvalidValueException.class);
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {"24-05-10", "2024:05:10"})
        @DisplayName("예약 날짜가 yyyy-MM-dd 형식이 아닌 경우 예외가 발생한다.")
        void addByInvalidDateFormat(String date) {
            AdminReservationCreateRequest request = AdminReservationCreateRequest.of(
                    date,
                    1L,
                    1L,
                    1L
            );

            assertThatThrownBy(() -> reservationService.add(request))
                    .isInstanceOf(InvalidValueException.class);
        }

        @Test
        @DisplayName("지나간 날짜에 대한 예약을 추가하면 예외가 발생한다.")
        void addReservationByPastDate() {
            String date = LocalDate.now().minusDays(1).toString();
            AdminReservationCreateRequest request = AdminReservationCreateRequest.of(
                    date,
                    1L,
                    1L,
                    1L
            );

            assertThatThrownBy(() -> reservationService.add(request))
                    .isInstanceOf(InvalidRequestException.class);
        }

        @Test
        @DisplayName("지나간 시간에 대한 예약을 추가하면 예외가 발생한다.")
        void addByPastTime() {
            String date = LocalDate.now().toString();
            String pastTime = LocalTime.now().minusHours(1).toString();
            jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES ('" + pastTime + "')");
            AdminReservationCreateRequest request = AdminReservationCreateRequest.of(
                    date,
                    1L,
                    2L,
                    1L
            );

            assertThatThrownBy(() -> reservationService.add(request))
                    .isInstanceOf(InvalidRequestException.class);
        }

        @Test
        @DisplayName("동일한 테마, 날짜, 시간에 대한 예약을 추가하면 예외가 발생한다.")
        void addByDuplicate() {
            String date = LocalDate.now().toString();
            jdbcTemplate.update(
                    "INSERT INTO reservation (date, member_id, time_id, theme_id) "
                            + "VALUES ('" + date + "', '1', '1', '1')");
            AdminReservationCreateRequest request = AdminReservationCreateRequest.of(
                    date,
                    1L,
                    1L,
                    1L
            );

            assertThatThrownBy(() -> reservationService.add(request))
                    .isInstanceOf(InvalidRequestException.class);
        }
    }

    @Nested
    @DisplayName("예약 삭제 테스트")
    class DeleteReservation {

        @Test
        @DisplayName("예약을 삭제한다.")
        void delete() {
            jdbcTemplate.update(
                    "INSERT INTO reservation (date, member_id, time_id, theme_id) VALUES ('2024-05-10', '1', '1', '1')");

            reservationService.delete(1L);

            assertThat(reservationService.findAll()).isEmpty();
        }

        @Test
        @DisplayName("예약 삭제시 아이디가 null이면 예외가 발생한다.")
        void deleteByNullOrEmptyId() {
            assertThatThrownBy(() -> reservationService.delete(null))
                    .isInstanceOf(InvalidRequestException.class);
        }

        @Test
        @DisplayName("예약 삭제시 아이디가 존재하지 않으면 예외가 발생한다.")
        void deleteByNotExistId() {
            assertThatThrownBy(() -> reservationService.delete(-1L))
                    .isInstanceOf(InvalidRequestException.class);
        }
    }
}
