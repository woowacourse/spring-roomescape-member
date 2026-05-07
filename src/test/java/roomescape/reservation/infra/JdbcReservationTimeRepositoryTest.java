package roomescape.reservation.infra;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import roomescape.reservation.domain.ReservationTime;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Sql(scripts = {"/truncate.sql", "/data.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class JdbcReservationTimeRepositoryTest {
    @Autowired
    private JdbcReservationTimeRepository repository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void 시간_저장_레포지토리_테스트() {
        ReservationTime savedReservationTime = repository.save(LocalTime.of(15, 40));

        assertThat(savedReservationTime.getStartAt()).isEqualTo(LocalTime.of(15, 40));
    }

    @Test
    void 전체_시간_조회_레포지토리_테스트() {
        List<ReservationTime> reservationTimes = repository.findAll();

        assertThat(reservationTimes).hasSize(4);
        assertThat(reservationTimes)
                .extracting(ReservationTime::getStartAt)
                .containsExactly(LocalTime.of(10, 00), LocalTime.of(11, 00), LocalTime.of(12, 00), LocalTime.of(13, 00));
    }

    @Test
    @DisplayName("더미데이터의 총 시간슬롯데이터 갯수가 4이므로 데이터를 추가한다음 삭제하면 결과가 4가 나와야 한다.")
    void 시간_삭제_레포지토리_테스트() {
        // given
        ReservationTime savedTime = repository.save(LocalTime.of(15, 40));

        // when
        repository.deleteById(savedTime.getId());

        // then
        int rowCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM reservation_time", Integer.class);
        assertThat(rowCount).isEqualTo(4);
    }

    @Test
    @DisplayName("일정표에 등록된 특정 날짜와 테마의 모든 시간을 가져올 수 있다.")
    void findTimesDateAndThemeId_테스트() {
        List<ReservationTime> result = repository.findTimesByDateAndThemeId(LocalDate.parse("2026-05-05"), 1L);

        assertThat(result)
                .extracting(ReservationTime::getStartAt)
                .containsExactly(LocalTime.of(10, 00), LocalTime.of(11, 00), LocalTime.of(12, 00), LocalTime.of(13, 00));
    }
}
