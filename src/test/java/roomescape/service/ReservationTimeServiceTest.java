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
import roomescape.dto.reservationtime.ReservationTimeCreateRequest;
import roomescape.dto.reservationtime.ReservationTimeResponse;
import roomescape.fixture.MemberFixtures;
import roomescape.fixture.ReservationFixtures;
import roomescape.fixture.ReservationTimeFixtures;
import roomescape.fixture.ThemeFixtures;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Sql(value = "classpath:test_db_clean.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
class ReservationTimeServiceTest {

    @Autowired
    private JdbcReservationDao reservationDao;
    @Autowired
    private JdbcReservationTimeDao reservationTimeDao;
    @Autowired
    private JdbcThemeDao themeDao;
    @Autowired
    private JdbcMemberDao memberDao;
    @Autowired
    private ReservationTimeService reservationTimeService;

    @Test
    @DisplayName("모든 예약 시간 정보를 조회한다.")
    void findAll() {
        //given
        ReservationTime reservationTime1 = ReservationTimeFixtures.createReservationTime("12:12");
        ReservationTime reservationTime2 = ReservationTimeFixtures.createReservationTime("12:20");
        reservationTimeDao.create(reservationTime1);
        reservationTimeDao.create(reservationTime2);

        //when
        List<ReservationTimeResponse> results = reservationTimeService.findAll();
        ReservationTimeResponse firstResponse = results.get(0);

        //then
        assertAll(
                () -> assertThat(results).hasSize(2),
                () -> assertThat(firstResponse.getStartAt()).isEqualTo("12:12")
        );
    }

    @Nested
    @DisplayName("예약 시간 추가")
    class create {
        @Test
        @DisplayName("예약 시간을 추가한다.")
        void add() {
            //given
            String givenStartAt = "10:52";
            ReservationTimeCreateRequest request = ReservationTimeFixtures.createReservationTimeCreateRequest(
                    givenStartAt);

            //when
            ReservationTimeResponse result = reservationTimeService.add(request);

            //then
            assertAll(
                    () -> assertThat(result.getStartAt()).isEqualTo(givenStartAt),
                    () -> assertThat(reservationTimeService.findAll()).hasSize(1)
            );
        }


        @ParameterizedTest
        @NullAndEmptySource
        @DisplayName("예약 시간에 null이나 공백 문자열이 입력되면 예외가 발생한다.")
        void createReservationTimeByNullOrEmptyStartAt(String given) {
            //given
            ReservationTimeCreateRequest request = ReservationTimeFixtures.createReservationTimeCreateRequest(given);

            //when //then
            assertThatThrownBy(() -> reservationTimeService.add(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("예약 시간이 중복되면 예외가 발생한다.")
        void createReservationTimeWhenDuplicatedStartAt() {
            //given
            reservationTimeDao.create(ReservationTimeFixtures.createReservationTime("12:02"));
            ReservationTimeCreateRequest request = ReservationTimeFixtures.createReservationTimeCreateRequest("12:02");

            //when //then
            assertThatThrownBy(() -> reservationTimeService.add(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    @DisplayName("예약 시간 삭제")
    class delete {

        @Test
        @DisplayName("예약 시간을 삭제한다.")
        void deleteReservationTime() {
            //given
            reservationTimeDao.create(ReservationTimeFixtures.createReservationTime("12:02"));
            long givenId = 1L;

            //when
            reservationTimeService.delete(givenId);
            List<ReservationTimeResponse> results = reservationTimeService.findAll();

            //then
            assertThat(results).isEmpty();
        }

        @Test
        @DisplayName("예약 시간 삭제시 아이디가 비어있으면 예외가 발생한다.")
        void deleteNullId() {
            //given
            reservationTimeDao.create(ReservationTimeFixtures.createReservationTime("12:02"));
            Long givenId = null;

            //when //then
            assertThatThrownBy(() -> reservationTimeService.delete(givenId))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("예약 시간 삭제시 아이디가 존재하지 않는다면 예외가 발생한다.")
        void deleteNotExistId() {
            //given
            reservationTimeDao.create(ReservationTimeFixtures.createReservationTime("12:02"));
            long givenId = -1L;

            //when //then
            assertThatThrownBy(() -> reservationTimeService.delete(givenId))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("특정 시간에 대한 예약이 존재하는데, 그 시간을 삭제하려 할 때 예외가 발생한다.")
        void deleteReservationTimeWhenReservationExist() {
            //given
            ReservationTime time = reservationTimeDao.create(ReservationTimeFixtures.createReservationTime("12:02"));
            Theme theme = themeDao.create(ThemeFixtures.createDefaultTheme());
            Member member = memberDao.create(MemberFixtures.createUserMember("다온"));
            Reservation reservation = ReservationFixtures.createReservation(member, "2024-05-02", time, theme);
            reservationDao.create(reservation);

            //when //then
            assertThatThrownBy(() -> reservationTimeService.delete(1L))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
