package roomescape.reservation.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationTime;
import roomescape.reservation.dto.request.ReservationTimeCreateRequest;
import roomescape.reservation.dto.response.ReservationResponse;
import roomescape.reservation.dto.response.ReservationTimeCreateResponse;
import roomescape.reservation.dto.response.ThemeSimpleResponse;
import roomescape.reservation.dto.response.TimeResponse;
import roomescape.theme.dao.ThemeDAO;
import roomescape.theme.domain.Theme;

@JdbcTest
class ReservationDAOTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private ReservationDAO reservationDAO;
    private ReservationTimeDAO reservationTimeDAO;
    private ThemeDAO themeDAO;

    @BeforeEach
    void setUp() {
        reservationDAO = new ReservationDAO(jdbcTemplate);
        reservationTimeDAO = new ReservationTimeDAO(jdbcTemplate);
        themeDAO = new ThemeDAO(jdbcTemplate);
    }

    private ReservationTimeCreateResponse createTime() {
        Long id = reservationTimeDAO.insert(new ReservationTimeCreateRequest(LocalTime.of(10, 0)));

        return ReservationTimeCreateResponse.of(id, LocalTime.of(10, 10, 10));
    }

    private Theme createTheme() {
        return themeDAO.insert(new Theme("테마이름", "테마설명", "https://image.url"));
    }

    @Nested
    class 예약을_저장한다 {

        @Test
        void 새로운_예약을_저장한다() {
            // given
            ReservationTimeCreateResponse time = createTime();
            Theme theme = createTheme();

            // when
            Reservation saved = reservationDAO.insert("브라운", LocalDate.of(2023, 8, 5), time.id(), theme.getId());

            // then
            assertThat(saved.getId()).isNotNull();
            assertThat(saved.getName()).isEqualTo("브라운");
        }

        @Test
        void 저장_후_전체_조회에_포함된다() {
            // given
            ReservationTimeCreateResponse time = createTime();
            Theme theme = createTheme();

            // when
            reservationDAO.insert("브라운", LocalDate.of(2023, 8, 5), time.id(), theme.getId());

            // then
            List<ReservationResponse> all = reservationDAO.findAll().stream()
                    .map(reservation -> ReservationResponse.of(
                            reservation.getId(),
                            reservation.getName(),
                            reservation.getDate(),
                            TimeResponse.from(reservation.getTime()),
                            ThemeSimpleResponse.from(reservation.getTheme())
                    )).toList();
            assertThat(all).hasSize(1);
        }
    }

    @Test
    void 저장된_모든_예약을_조회한다() {
        // given
        ReservationTimeCreateResponse time = createTime();
        Theme theme = createTheme();
        reservationDAO.insert("브라운", LocalDate.of(2023, 8, 5), time.id(), theme.getId());
        reservationDAO.insert("리사", LocalDate.of(2023, 8, 6), time.id(), theme.getId());

        // when
        List<ReservationResponse> all = reservationDAO.findAll().stream()
                .map(reservation -> ReservationResponse.of(
                        reservation.getId(),
                        reservation.getName(),
                        reservation.getDate(),
                        TimeResponse.from(reservation.getTime()),
                        ThemeSimpleResponse.from(reservation.getTheme())
                )).toList();

        // then
        assertThat(all).hasSize(2);
    }

    @Test
    void ID로_예약을_삭제한다() {
        // given
        ReservationTimeCreateResponse time = createTime();
        Theme theme = createTheme();
        Reservation saved = reservationDAO.insert("브라운", LocalDate.of(2023, 8, 5), time.id(), theme.getId());

        // when
        reservationDAO.delete(saved.getId());

        // then
        List<ReservationResponse> all = reservationDAO.findAll().stream()
                .map(reservation -> ReservationResponse.of(
                        reservation.getId(),
                        reservation.getName(),
                        reservation.getDate(),
                        TimeResponse.from(reservation.getTime()),
                        ThemeSimpleResponse.from(reservation.getTheme())
                )).toList();
        assertThat(all).isEmpty();
    }

    @Nested
    class 시간_ID로_예약_존재_여부를_확인한다 {

        @Test
        void 해당_시간의_예약이_존재하면_true를_반환한다() {
            // given
            ReservationTimeCreateResponse time = createTime();
            Theme theme = createTheme();
            reservationDAO.insert("브라운", LocalDate.of(2023, 8, 5), time.id(), theme.getId());

            // when
            boolean exists = reservationDAO.existsByTimeId(time.id());

            // then
            assertThat(exists).isTrue();
        }

        @Test
        void 해당_시간의_예약이_없으면_false를_반환한다() {
            // when
            boolean exists = reservationDAO.existsByTimeId(999L);

            // then
            assertThat(exists).isFalse();
        }
    }
}
