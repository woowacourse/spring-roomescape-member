package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import roomescape.dao.JdbcReservationDao;
import roomescape.dao.JdbcReservationTimeDao;
import roomescape.dao.JdbcThemeDao;
import roomescape.domain.member.Member;
import roomescape.domain.member.MemberEmail;
import roomescape.domain.member.MemberName;
import roomescape.domain.member.MemberPassword;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.reservationtime.ReservationTime;
import roomescape.domain.theme.Theme;
import roomescape.dto.reservation.AvailableReservationResponse;
import roomescape.dto.reservation.ReservationCreateRequest;
import roomescape.dto.reservation.ReservationResponse;
import roomescape.fixture.ReservationFixtures;
import roomescape.fixture.ReservationTimeFixtures;
import roomescape.fixture.ThemeFixtures;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Sql(value = "classpath:test_db_clean.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
class ReservationServiceTest {

    @Autowired
    private JdbcReservationDao reservationDao;
    @Autowired
    private JdbcReservationTimeDao reservationTimeDao;
    @Autowired
    private JdbcThemeDao themeDao;
    @Autowired
    private ReservationService reservationService;

    @Test
    @DisplayName("모든 예약 정보를 조회한다.")
    void findAll() {
        //given
        Theme theme = themeDao.create(ThemeFixtures.createDefaultTheme());
        ReservationTime time1 = reservationTimeDao.create(ReservationTimeFixtures.createReservationTime("12:02"));
        ReservationTime time2 = reservationTimeDao.create(ReservationTimeFixtures.createReservationTime("12:20"));
        Reservation reservation1 = ReservationFixtures.createReservation(time1, theme);
        Reservation reservation2 = ReservationFixtures.createReservation(time2, theme);
        reservationDao.create(reservation1);
        reservationDao.create(reservation2);

        //when
        List<ReservationResponse> results = reservationService.findAll();

        //then
        assertThat(results).hasSize(2);

    }

    @Test
    @DisplayName("이용 가능한 예약 시간을 조회한다.")
    void findTimeByDateAndThemeId() {
        //given
        LocalDateTime now = LocalDateTime.of(2024, 5, 2, 12, 2);
        LocalDate today = LocalDate.of(now.getYear(), now.getMonth(), now.getDayOfMonth());
        ReservationTime reservationTime1 = ReservationTimeFixtures.createReservationTime("12:02");
        ReservationTime reservationTime2 = ReservationTimeFixtures.createReservationTime("12:20");

        Theme theme = themeDao.create(ThemeFixtures.createDefaultTheme());
        ReservationTime time1 = reservationTimeDao.create(reservationTime1);
        reservationTimeDao.create(reservationTime2);
        reservationDao.create(ReservationFixtures.createReservation(today.toString(), time1, theme));

        //when
        List<AvailableReservationResponse> responses
                = reservationService.findTimeByDateAndThemeID(today.toString(), theme.getId());
        AvailableReservationResponse alreadyBooked = responses.get(0);
        AvailableReservationResponse notBooked = responses.get(1);

        //then
        assertAll(
                () -> assertThat(alreadyBooked.isAlreadyBooked()).isTrue(),
                () -> assertThat(notBooked.isAlreadyBooked()).isFalse()
        );
    }

    @Nested
    @DisplayName("예약 추가")
    class create {
        @Test
        @DisplayName("예약을 추가한다.")
        void add() {
            //given
            LocalDateTime now = LocalDateTime.of(2024, 5, 2, 12, 2);
            LocalDate today = LocalDate.of(now.getYear(), now.getMonth(), now.getDayOfMonth());
            LocalDate tomorrow = today.plusDays(1);
            String givenName = "wooteco";
            String givenDate = tomorrow.toString();
            ReservationTime reservationTime =
                    reservationTimeDao.create(ReservationTimeFixtures.createReservationTime("12:00"));
            Theme theme = themeDao.create(ThemeFixtures.createDefaultTheme());
            ReservationCreateRequest givenRequest =
                    ReservationFixtures.createReservationCreateRequest(
                            givenDate, reservationTime.getId(), theme.getId()
                    );

            //when
            Member member = createMemberWithName(givenName);
            ReservationResponse result = reservationService.add(member, givenRequest, now);

            //then
            assertAll(
                    () -> assertThat(result.getName()).isEqualTo(givenName),
                    () -> assertThat(result.getDate()).isEqualTo(givenDate),
                    () -> assertThat(reservationService.findAll()).hasSize(1)
            );
        }

        @Test
        @DisplayName("존재하지 않는 시간 아이디로 예약 추가시 에외가 발생한다.")
        void addNotExistTimeId() {
            //given
            LocalDateTime now = LocalDateTime.of(2024, 5, 2, 12, 2);
            themeDao.create(ThemeFixtures.createDefaultTheme());
            long given = -1L;
            ReservationCreateRequest givenRequest = ReservationFixtures.createReservationCreateRequest(given, 1L);
            Member member = createMemberWithName("daon");

            //when //then
            assertThatThrownBy(() -> reservationService.add(member, givenRequest, now))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("존재하지 않는 테마 아이디로 예약 추가시 에외가 발생한다.")
        void addNotExistThemeId() {
            //given
            LocalDateTime now = LocalDateTime.of(2024, 5, 2, 12, 2);
            reservationTimeDao.create(ReservationTimeFixtures.createReservationTime("12:24"));
            long given = -1L;
            ReservationCreateRequest givenRequest = ReservationFixtures.createReservationCreateRequest(1L, given);
            Member member = createMemberWithName("daon");

            //when //then
            assertThatThrownBy(() -> reservationService.add(member, givenRequest, now))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @ParameterizedTest
        @NullAndEmptySource
        @DisplayName("예약자명에 null이나 공백 문자열이 입력되면 예외가 발생한다.")
        void createReservationByNullOrEmptyName(String given) {
            //given
            LocalDateTime now = LocalDateTime.of(2024, 5, 2, 12, 2);
            LocalDate today = LocalDate.of(now.getYear(), now.getMonth(), now.getDayOfMonth());
            LocalDate tomorrow = today.plusDays(1);
            reservationTimeDao.create(ReservationTimeFixtures.createReservationTime("12:00"));
            themeDao.create(ThemeFixtures.createDefaultTheme());
            ReservationCreateRequest request =
                    ReservationFixtures.createReservationCreateRequest(tomorrow.toString(), 1L, 1L);
            Member member = createMemberWithName(given);

            //when //then
            assertThatThrownBy(() -> reservationService.add(member, request, now))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @ParameterizedTest
        @NullAndEmptySource
        @DisplayName("예약 날짜에 null이나 공백 문자열이 입력되면 예외가 발생한다.")
        void createReservationByNullOrEmptyDate(String given) {
            //given
            LocalDateTime now = LocalDateTime.of(2024, 5, 2, 12, 2);
            reservationTimeDao.create(ReservationTimeFixtures.createReservationTime("12:00"));
            themeDao.create(ThemeFixtures.createDefaultTheme());
            ReservationCreateRequest request =
                    ReservationFixtures.createReservationCreateRequest(given, 1L, 1L);
            Member member = createMemberWithName("daon");

            //when //then
            assertThatThrownBy(() -> reservationService.add(member, request, now))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @ParameterizedTest
        @ValueSource(strings = {"24-02-04", "2024;04;24"})
        @DisplayName("예약 날짜가 yyyy-MM-dd 형식이 아닌 경우 예외가 발생한다.")
        void createReservationByInvalidDate(String given) {
            //given
            LocalDateTime now = LocalDateTime.of(2024, 5, 2, 12, 2);
            reservationTimeDao.create(ReservationTimeFixtures.createReservationTime("12:00"));
            themeDao.create(ThemeFixtures.createDefaultTheme());
            ReservationCreateRequest request =
                    ReservationFixtures.createReservationCreateRequest(given, 1L, 1L);
            Member member = createMemberWithName("daon");

            //when //then
            assertThatThrownBy(() -> reservationService.add(member, request, now))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("지나간 날짜에 대한 예약을 추가하면 예외가 발생한다.")
        void createReservationByPastDate() {
            //given
            LocalDateTime now = LocalDateTime.of(2024, 5, 2, 12, 2);
            LocalDate today = LocalDate.of(now.getYear(), now.getMonth(), now.getDayOfMonth());
            LocalDate pastDay = today.minusDays(1);
            reservationTimeDao.create(ReservationTimeFixtures.createReservationTime("12:00"));
            themeDao.create(ThemeFixtures.createDefaultTheme());
            ReservationCreateRequest request =
                    ReservationFixtures.createReservationCreateRequest(pastDay.toString(), 1L, 1L);
            Member member = createMemberWithName("daon");

            //when //then
            assertThatThrownBy(() -> reservationService.add(member, request, now))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("지나간 시간에 대한 예약을 추가하면 예외가 발생한다.")
        void createReservationByPastTime() {
            //given
            LocalDateTime now = LocalDateTime.of(2024, 5, 2, 12, 2);
            LocalDate today = LocalDate.of(now.getYear(), now.getMonth(), now.getDayOfMonth());
            LocalTime currentTime = LocalTime.of(now.getHour(), now.getMinute());
            LocalTime pastTime = currentTime.minusHours(1);
            reservationTimeDao.create(ReservationTimeFixtures.createReservationTime(pastTime.toString()));
            themeDao.create(ThemeFixtures.createDefaultTheme());
            ReservationCreateRequest request =
                    ReservationFixtures.createReservationCreateRequest(today.toString(), 1L, 1L);
            Member member = createMemberWithName("daon");

            //when //then
            assertThatThrownBy(() -> reservationService.add(member, request, now))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("동일한 테마, 날짜, 시간에 대한 예약을 추가하면 예외가 발생한다.")
        void createReservationByDuplicated() {
            //given
            LocalDateTime now = LocalDateTime.of(2024, 5, 2, 12, 2);
            LocalDate today = LocalDate.of(now.getYear(), now.getMonth(), now.getDayOfMonth());
            LocalDate tomorrow = today.plusDays(1);
            reservationTimeDao.create(ReservationTimeFixtures.createReservationTime("12:00"));
            themeDao.create(ThemeFixtures.createDefaultTheme());
            ReservationCreateRequest request = ReservationCreateRequest.of(tomorrow.toString(), 1L, 1L);
            Member member = createMemberWithName("daon");
            reservationService.add(member, request, now);

            //when //then
            assertThatThrownBy(() -> reservationService.add(member, request, now))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    @DisplayName("예약 삭제")
    class delete {
        @Test
        @DisplayName("예약을 삭제한다.")
        void deleteReservation() {
            //given
            LocalDateTime now = LocalDateTime.of(2024, 5, 2, 12, 2);
            LocalDate today = LocalDate.of(now.getYear(), now.getMonth(), now.getDayOfMonth());
            LocalDate tomorrow = today.plusDays(1);
            reservationTimeDao.create(ReservationTimeFixtures.createReservationTime("12:00"));
            themeDao.create(ThemeFixtures.createDefaultTheme());
            ReservationCreateRequest request = ReservationCreateRequest.of(tomorrow.toString(), 1L, 1L);
            Member member = createMemberWithName("daon");
            reservationService.add(member, request, now);
            long givenId = 1L;

            //when
            reservationService.delete(givenId);
            List<ReservationResponse> results = reservationService.findAll();

            //then
            assertThat(results).isEmpty();
        }

        @Test
        @DisplayName("예약 삭제시 아이디가 비어있으면 예외가 발생한다.")
        void deleteNullId() {
            //given
            LocalDateTime now = LocalDateTime.of(2024, 5, 2, 12, 2);
            LocalDate today = LocalDate.of(now.getYear(), now.getMonth(), now.getDayOfMonth());
            LocalDate tomorrow = today.plusDays(1);
            reservationTimeDao.create(ReservationTimeFixtures.createReservationTime("12:00"));
            themeDao.create(ThemeFixtures.createDefaultTheme());
            ReservationCreateRequest request = ReservationCreateRequest.of(tomorrow.toString(), 1L, 1L);
            Member member = createMemberWithName("daon");
            reservationService.add(member, request, now);
            Long givenId = null;

            //when //then
            assertThatThrownBy(() -> reservationService.delete(givenId))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("예약 삭제시 아이디가 존재하지 않는다면 예외가 발생한다.")
        void deleteNotExistId() {
            //given
            LocalDateTime now = LocalDateTime.of(2024, 5, 2, 12, 2);
            LocalDate today = LocalDate.of(now.getYear(), now.getMonth(), now.getDayOfMonth());
            LocalDate tomorrow = today.plusDays(1);
            reservationTimeDao.create(ReservationTimeFixtures.createReservationTime("12:00"));
            themeDao.create(ThemeFixtures.createDefaultTheme());
            ReservationCreateRequest request = ReservationCreateRequest.of(tomorrow.toString(), 1L, 1L);
            Member member = createMemberWithName("daon");
            reservationService.add(member, request, now);
            long givenId = -1L;

            //when //then
            assertThatThrownBy(() -> reservationService.delete(givenId))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    private Member createMemberWithName(String name) {
        return new Member(
                1L,
                new MemberName(name),
                new MemberEmail("test@test.com"),
                new MemberPassword("1234")
        );
    }
}
