package roomescape.reservation.infrastructure.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.global.exception.DeleteReservationException;
import roomescape.reservation.application.dto.CreateReservationRequest;
import roomescape.reservation.domain.ReservationDate;
import roomescape.reservation.domain.ReservationName;
import roomescape.reservation.presentation.dto.ReservationTimeRequest;
import roomescape.reservation.presentation.dto.ThemeRequest;

@JdbcTest
public class ReservationDaoTest {

    private ReservationDao reservationDao;
    private ReservationTimeDao reservationTimeDao;
    private ThemeDao themeDao;
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public ReservationDaoTest(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.reservationDao = new ReservationDao(jdbcTemplate);
        this.reservationTimeDao = new ReservationTimeDao(jdbcTemplate);
        this.themeDao = new ThemeDao(jdbcTemplate);
    }

    @BeforeEach
    public void resetAutoIncrement() {
        jdbcTemplate.execute("ALTER TABLE reservation ALTER COLUMN id RESTART WITH 1");
        jdbcTemplate.execute("ALTER TABLE reservation_time ALTER COLUMN id RESTART WITH 1");
    }

    @Test
    @DisplayName("예약 추가 확인 테스트")
    void insertTest() {
        // given
        ReservationTimeRequest reservationTimeRequest = new ReservationTimeRequest(LocalTime.of(15, 40));
        ThemeRequest themeRequest = new ThemeRequest(
                "레벨2 탈출",
                "우테코 레벨2를 탈출하는 내용입니다.",
                "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"
        );
        CreateReservationRequest createReservationRequest = new CreateReservationRequest(
                new ReservationName("브라운"),
                themeDao.insert(themeRequest),
                new ReservationDate(LocalDate.of(2023, 8, 5)),
                reservationTimeDao.insert(reservationTimeRequest.getStartAt())
        );

        // when
        reservationDao.insert(createReservationRequest);

        // then
        assertThat(count()).isEqualTo(1);
    }

    @Test
    @DisplayName("예약 삭제 확인 테스트")
    void deleteTest() {
        // given
        ReservationTimeRequest reservationTimeRequest = new ReservationTimeRequest(LocalTime.of(15, 40));
        ThemeRequest themeRequest = new ThemeRequest(
                "레벨2 탈출",
                "우테코 레벨2를 탈출하는 내용입니다.",
                "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"
        );
        CreateReservationRequest createReservationRequest = new CreateReservationRequest(
                new ReservationName("브라운"),
                themeDao.insert(themeRequest),
                new ReservationDate(LocalDate.of(2023, 8, 5)),
                reservationTimeDao.insert(reservationTimeRequest.getStartAt())
        );
        reservationDao.insert(createReservationRequest);

        // when
        reservationDao.delete(1L);

        // then
        assertThat(count()).isEqualTo(0);
    }

    @Test
    @DisplayName("저장되어 있지 않은 id로 요청을 보내면 예외가 발생한다.")
    void deleteExceptionTest() {
        assertThatThrownBy(() -> reservationDao.delete(1L))
                .isInstanceOf(DeleteReservationException.class)
                .hasMessage("[ERROR] 삭제하지 못했습니다.");
    }

    private int count() {
        String sql = "select count(*) from reservation";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }

}
