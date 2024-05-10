package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import roomescape.dao.JdbcMemberDao;
import roomescape.dao.JdbcReservationDao;
import roomescape.dao.JdbcReservationTimeDao;
import roomescape.dao.JdbcThemeDao;
import roomescape.domain.member.Member;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.reservationtime.ReservationTime;
import roomescape.domain.theme.Theme;
import roomescape.dto.reservation.AvailableReservationResponse;
import roomescape.dto.reservation.ReservationCreateRequest;
import roomescape.dto.reservation.ReservationFilterRequest;
import roomescape.dto.reservation.ReservationResponse;
import roomescape.fixture.MemberFixtures;
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
    private JdbcMemberDao memberDao;
    @Autowired
    private ReservationService reservationService;

    @Test
    @DisplayName("모든 예약 정보를 조회한다.")
    void findAll() {
        //given
        Theme theme = themeDao.create(ThemeFixtures.createDefaultTheme());
        Member member = memberDao.create(MemberFixtures.createUserMember("daon"));
        ReservationTime time1 = reservationTimeDao.create(ReservationTimeFixtures.createReservationTime("12:02"));
        ReservationTime time2 = reservationTimeDao.create(ReservationTimeFixtures.createReservationTime("12:20"));
        Reservation reservation1 = ReservationFixtures.createReservation(member, time1, theme);
        Reservation reservation2 = ReservationFixtures.createReservation(member, time2, theme);
        reservationDao.create(reservation1);
        reservationDao.create(reservation2);

        //when
        List<ReservationResponse> results = reservationService.findAll();

        //then
        assertThat(results).hasSize(2);
    }

    @Test
    @DisplayName("요청에 시작시간이 종료시간보다 이후이면 예외가 발생한다.")
    void findFilteredWhenFromDateAfterToDate() {
        //given
        Theme theme = themeDao.create(ThemeFixtures.createDefaultTheme());
        Member member = memberDao.create(MemberFixtures.createUserMember("daon"));
        ReservationTime time1 = reservationTimeDao.create(ReservationTimeFixtures.createReservationTime("12:02"));
        ReservationTime time2 = reservationTimeDao.create(ReservationTimeFixtures.createReservationTime("12:20"));
        Reservation reservation1 = ReservationFixtures.createReservation(member, time1, theme);
        Reservation reservation2 = ReservationFixtures.createReservation(member, time2, theme);
        reservationDao.create(reservation1);
        reservationDao.create(reservation2);

        ReservationFilterRequest request = new ReservationFilterRequest(1L, 1L, "2024-04-04", "2024-04-03");

        //when //then
        assertThatThrownBy(() -> reservationService.findFiltered(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("시작 날짜는 종료 날짜보다 이후일 수 없습니다.");
    }

    @Test
    @DisplayName("이용 가능한 예약 시간을 조회한다.")
    void findTimeByDateAndThemeId() {
        //given
        LocalDateTime now = LocalDateTime.of(2024, 5, 2, 12, 2);
        LocalDate today = LocalDate.of(now.getYear(), now.getMonth(), now.getDayOfMonth());
        ReservationTime reservationTime1 = ReservationTimeFixtures.createReservationTime("12:02");
        ReservationTime reservationTime2 = ReservationTimeFixtures.createReservationTime("12:20");

        Member member = memberDao.create(MemberFixtures.createUserMember("daon"));
        Theme theme = themeDao.create(ThemeFixtures.createDefaultTheme());
        ReservationTime time1 = reservationTimeDao.create(reservationTime1);
        reservationTimeDao.create(reservationTime2);
        reservationDao.create(ReservationFixtures.createReservation(member, today.toString(), time1, theme));

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
            Member member = memberDao.create(MemberFixtures.createUserMember(givenName));
            ReservationTime reservationTime =
                    reservationTimeDao.create(ReservationTimeFixtures.createReservationTime("12:00"));
            Theme theme = themeDao.create(ThemeFixtures.createDefaultTheme());
            ReservationCreateRequest request =
                    ReservationFixtures.getReservationCreateRequest(givenDate, reservationTime.getId(), theme.getId());

            //when
            ReservationResponse result = reservationService.add(member, request, now);

            //then
            assertAll(
                    () -> assertThat(result.getMember().getName()).isEqualTo(givenName),
                    () -> assertThat(result.getDate()).isEqualTo(givenDate),
                    () -> assertThat(reservationService.findAll()).hasSize(1)
            );
        }

        @Test
        @DisplayName("존재하지 않는 시간 아이디로 예약 추가시 에외가 발생한다.")
        void addNotExistTimeId() {
            //given
            LocalDateTime now = LocalDateTime.of(2024, 5, 2, 12, 2);
            Member member = memberDao.create(MemberFixtures.createUserMember("다온"));
            themeDao.create(ThemeFixtures.createDefaultTheme());
            long given = -1L;
            ReservationCreateRequest givenRequest = ReservationFixtures.getReservationCreateRequest(given, 1L);

            //when //then
            assertThatThrownBy(() -> reservationService.add(member, givenRequest, now))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("예약 시간 아이디에 해당하는 예약 시간이 존재하지 않습니다.");
        }

        @Test
        @DisplayName("존재하지 않는 테마 아이디로 예약 추가시 에외가 발생한다.")
        void addNotExistThemeId() {
            //given
            LocalDateTime now = LocalDateTime.of(2024, 5, 2, 12, 2);
            Member member = memberDao.create(MemberFixtures.createUserMember("다온"));
            reservationTimeDao.create(ReservationTimeFixtures.createReservationTime("12:24"));
            long given = -1L;
            ReservationCreateRequest givenRequest = ReservationFixtures.getReservationCreateRequest(1L, given);

            //when //then
            assertThatThrownBy(() -> reservationService.add(member, givenRequest, now))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("테마 아이디에 해당하는 테마가 존재하지 않습니다.");
        }

        @Test
        @DisplayName("동일한 테마, 날짜, 시간에 대한 예약을 추가하면 예외가 발생한다.")
        void createReservationByDuplicated() {
            //given
            LocalDateTime now = LocalDateTime.of(2024, 5, 2, 12, 2);
            LocalDate today = LocalDate.of(now.getYear(), now.getMonth(), now.getDayOfMonth());
            LocalDate tomorrow = today.plusDays(1);
            Member member = memberDao.create(MemberFixtures.createUserMember("다온"));
            reservationTimeDao.create(ReservationTimeFixtures.createReservationTime("12:00"));
            themeDao.create(ThemeFixtures.createDefaultTheme());
            ReservationCreateRequest request = ReservationCreateRequest.of(tomorrow.toString(), 1L, 1L);
            reservationService.add(member, request, now);

            //when //then
            assertThatThrownBy(() -> reservationService.add(member, request, now))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("이미 예약이 있어 추가할 수 없습니다.");
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
            Member member = memberDao.create(MemberFixtures.createUserMember("다온"));
            reservationTimeDao.create(ReservationTimeFixtures.createReservationTime("12:00"));
            themeDao.create(ThemeFixtures.createDefaultTheme());
            ReservationCreateRequest request = ReservationCreateRequest.of(tomorrow.toString(), 1L, 1L);
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
            Member member = memberDao.create(MemberFixtures.createUserMember("다온"));
            reservationTimeDao.create(ReservationTimeFixtures.createReservationTime("12:00"));
            themeDao.create(ThemeFixtures.createDefaultTheme());
            ReservationCreateRequest request = ReservationCreateRequest.of(tomorrow.toString(), 1L, 1L);
            reservationService.add(member, request, now);
            Long givenId = null;

            //when //then
            assertThatThrownBy(() -> reservationService.delete(givenId))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("예약 아이디는 비어있을 수 없습니다.");
        }

        @Test
        @DisplayName("예약 삭제시 아이디가 존재하지 않는다면 예외가 발생한다.")
        void deleteNotExistId() {
            //given
            LocalDateTime now = LocalDateTime.of(2024, 5, 2, 12, 2);
            LocalDate today = LocalDate.of(now.getYear(), now.getMonth(), now.getDayOfMonth());
            LocalDate tomorrow = today.plusDays(1);
            Member member = memberDao.create(MemberFixtures.createUserMember("다온"));
            reservationTimeDao.create(ReservationTimeFixtures.createReservationTime("12:00"));
            themeDao.create(ThemeFixtures.createDefaultTheme());
            ReservationCreateRequest request = ReservationCreateRequest.of(tomorrow.toString(), 1L, 1L);
            reservationService.add(member, request, now);
            long givenId = -1L;

            //when //then
            assertThatThrownBy(() -> reservationService.delete(givenId))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("예약 아이디에 해당하는 예약이 존재하지 않습니다.");
        }
    }
}
