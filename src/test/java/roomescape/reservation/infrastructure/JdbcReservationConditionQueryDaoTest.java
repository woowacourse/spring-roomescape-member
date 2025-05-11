package roomescape.reservation.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import roomescape.reservation.infrastructure.dto.ReservationDetailData;
import roomescape.reservation.presentation.controller.ReservationSearchCondition;

@JdbcTest
@Import(JdbcReservationQueryDao.class)
@ActiveProfiles("test")
@Sql(scripts = "/prepare-reservation-condition-data.sql")
class JdbcReservationConditionQueryDaoTest {

    @Autowired
    private JdbcReservationQueryDao reservationQueryDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        jdbcTemplate.update("INSERT INTO reservation (member_id, theme_id, time_id, date) VALUES (1, 2, 1, '2025-01-01')");
        jdbcTemplate.update("INSERT INTO reservation (member_id, theme_id, time_id, date) VALUES (1, 2, 1, '2025-01-02')");
        jdbcTemplate.update("INSERT INTO reservation (member_id, theme_id, time_id, date) VALUES (1, 1, 1, '2025-01-03')");
        jdbcTemplate.update("INSERT INTO reservation (member_id, theme_id, time_id, date) VALUES (2, 1, 1, '2025-01-04')");
    }

    @DisplayName("조건이 없다면 전체 조회")
    @Test
    void noSearchFilter_then_getAll() {
        ReservationSearchCondition condition = new ReservationSearchCondition(null, null, null, null);
        List<ReservationDetailData> result = reservationQueryDao.findByCondition(condition);
        assertThat(result).hasSize(4);
    }

    @DisplayName("회원Id로 조회 - memberId가 1인 데이터는 3개다.")
    @Test
    void search_withMemberId() {
        // given
        ReservationSearchCondition condition = new ReservationSearchCondition(1L, null, null, null);

        // when
        List<ReservationDetailData> result = reservationQueryDao.findByCondition(condition);

        // then
        assertThat(result).hasSize(3);
    }

    @DisplayName("날짜 시작 조건 - 1/4일부터의 예약은 1개다")
    @Test
    void search_withDateFrom() {
        // given
        LocalDate dateFrom = LocalDate.of(2025, 1, 4);
        ReservationSearchCondition condition = new ReservationSearchCondition(null, null, dateFrom, null);

        // when
        List<ReservationDetailData> result = reservationQueryDao.findByCondition(condition);

        // then
        assertThat(result).hasSize(1);
    }

    @DisplayName("날짜 종료 조건 - 1/2일까지의 예약은 2개다")
    @Test
    void search_withDateTo() {
        // given
        LocalDate dateTo = LocalDate.of(2025, 1, 2);
        ReservationSearchCondition condition = new ReservationSearchCondition(null, null, null, dateTo);

        // when
        List<ReservationDetailData> result = reservationQueryDao.findByCondition(condition);

        // then
        assertThat(result).hasSize(2);
    }

    @DisplayName("조건 2개로 조회 - memberId가 1이면서 themeId가 2인 데이터는 2개다.")
    @Test
    void search_withMultipleCondition() {
        // given
        ReservationSearchCondition condition = new ReservationSearchCondition(1L, 2L, null, null);

        // when
        List<ReservationDetailData> result = reservationQueryDao.findByCondition(condition);

        // then
        assertThat(result).hasSize(2);
    }

    @DisplayName("맞는 조건이 없다면 데이터가 0개 조회된다.")
    @Test
    void search_noConditionMatching() {
        // given
        ReservationSearchCondition condition = new ReservationSearchCondition(3L, null, null, null);

        // when
        List<ReservationDetailData> result = reservationQueryDao.findByCondition(condition);

        // then
        assertThat(result).hasSize(0);
    }
}
