package roomescape.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.tuple;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import roomescape.domain.Reservation;

@JdbcTest
@ActiveProfiles("test")
@Import(ReservationDao.class)
public class ReservationDaoTest {

    private static final String INSERT_THREE_TIMES_SQL = """
            INSERT INTO reservation_time (id, start_at)
            VALUES (1, '10:00'),
                   (2, '11:00'),
                   (3, '13:00');
            """;

    private static final String INSERT_SINGLE_THEME_SQL = """
            INSERT INTO theme (id, name, description, img_url)
            VALUES (1, '이든의 공포 하우스', '이든이 귀신으로 나오는 공포 테마',
                    'https://images.example.com/themes/horror-house.jpg');
            """;

    private static final String INSERT_TWO_RESERVATIONS_SQL = """
            INSERT INTO reservation (id, name, date, time_id, theme_id)
            VALUES (1, '브라운', '2026-05-01', 1, 1),
                   (2, '정콩이', '2026-05-02', 2, 1);
            """;

    @Autowired
    private ReservationDao reservationDao;

    @Test
    @Sql(statements = {
            INSERT_THREE_TIMES_SQL,
            INSERT_SINGLE_THEME_SQL,
            INSERT_TWO_RESERVATIONS_SQL
    })
    void 모든_예약을_조회한다() {
        List<Reservation> reservations = reservationDao.findAllReservations();

        assertThat(reservations).hasSize(2);
        assertThat(reservations)
                .extracting(
                        Reservation::getId,
                        Reservation::getName,
                        Reservation::getDate,
                        reservation -> reservation.getTime().getId(),
                        reservation -> reservation.getTime().getStartAt(),
                        Reservation::getThemeId
                )
                .containsExactlyInAnyOrder(
                        tuple(1L, "브라운", LocalDate.of(2026, 5, 1), 1L, LocalTime.of(10, 0), 1L),
                        tuple(2L, "정콩이", LocalDate.of(2026, 5, 2), 2L, LocalTime.of(11, 0), 1L)
                );
    }

    @Test
    @Sql(statements = {
            INSERT_THREE_TIMES_SQL,
            INSERT_SINGLE_THEME_SQL,
            INSERT_TWO_RESERVATIONS_SQL
    })
    void 사용자_이름으로_예약을_조회한다() {
        List<Reservation> reservations = reservationDao.findAllReservationsByUserName("브라운");

        assertThat(reservations).hasSize(1);
        assertThat(reservations)
                .extracting(
                        Reservation::getName,
                        Reservation::getDate,
                        reservation -> reservation.getTime().getId(),
                        reservation -> reservation.getTime().getStartAt()
                )
                .containsExactlyInAnyOrder(
                        tuple("브라운", LocalDate.of(2026, 5, 1), 1L, LocalTime.of(10, 0))
                );
    }

    @Test
    @Sql(statements = {
            INSERT_THREE_TIMES_SQL,
            INSERT_SINGLE_THEME_SQL,
            INSERT_TWO_RESERVATIONS_SQL
    })
    void ID에_해당하는_예약을_조회한다() {
        Reservation reservation = reservationDao.findReservationById(1L);

        assertThat(reservation)
                .extracting(
                        Reservation::getId,
                        Reservation::getName,
                        Reservation::getDate,
                        reservationTime -> reservationTime.getTime().getId(),
                        reservationTime -> reservationTime.getTime().getStartAt(),
                        Reservation::getThemeId
                )
                .containsExactly(
                        1L,
                        "브라운",
                        LocalDate.of(2026, 5, 1),
                        1L,
                        LocalTime.of(10, 0),
                        1L
                );
    }

    @Test
    @Sql(statements = {
            INSERT_THREE_TIMES_SQL,
            INSERT_SINGLE_THEME_SQL,
            INSERT_TWO_RESERVATIONS_SQL
    })
    void 예약을_수정한다() {
        int updatedCount = reservationDao.updateById(1L, LocalDate.of(2026, 5, 03), 2L);

        Reservation updatedReservation = reservationDao.findReservationById(1L);

        assertThat(updatedCount).isEqualTo(1);
        assertThat(updatedReservation)
                .extracting(
                        Reservation::getId,
                        Reservation::getName,
                        Reservation::getDate,
                        reservation -> reservation.getTime().getId(),
                        reservation -> reservation.getTime().getStartAt(),
                        Reservation::getThemeId
                )
                .containsExactly(
                        1L,
                        "브라운",
                        LocalDate.of(2026, 5, 3),
                        2L,
                        LocalTime.of(11, 0),
                        1L
                );
    }

    @Test
    @Sql(statements = {
            INSERT_THREE_TIMES_SQL,
            INSERT_SINGLE_THEME_SQL
    })
    void 예약을_추가한다() {
        Long id = reservationDao.insertWithKeyHolder(
                "브라운",
                LocalDate.of(2026, 5, 1),
                1L,
                1L
        );

        Reservation reservation = reservationDao.findReservationById(id);

        assertThat(id).isNotNull();
        assertThat(id).isPositive();
        assertThat(reservation)
                .extracting(
                        Reservation::getId,
                        Reservation::getName,
                        Reservation::getDate,
                        reservationTime -> reservationTime.getTime().getId(),
                        reservationTime -> reservationTime.getTime().getStartAt(),
                        Reservation::getThemeId
                )
                .containsExactly(
                        id,
                        "브라운",
                        LocalDate.of(2026, 5, 1),
                        1L,
                        LocalTime.of(10, 0),
                        1L
                );
    }

    @Test
    @Sql(statements = {
            INSERT_THREE_TIMES_SQL,
            INSERT_SINGLE_THEME_SQL,
            INSERT_TWO_RESERVATIONS_SQL
    })
    void 예약을_삭제한다() {
        int deletedCount = reservationDao.delete(1L);

        assertThat(deletedCount).isEqualTo(1);

        assertThat(reservationDao.findAllReservations()).hasSize(1);
    }

    @Test
    @Sql(statements = {
            INSERT_THREE_TIMES_SQL,
            INSERT_SINGLE_THEME_SQL,
            INSERT_TWO_RESERVATIONS_SQL
    })
    void 같은_날짜_시간_테마의_예약을_추가하면_예외가_발생한다() {
        assertThatThrownBy(() -> reservationDao.insertWithKeyHolder(
                "정콩이",
                LocalDate.of(2026, 5, 1),
                1L,
                1L
        )).isInstanceOf(DataIntegrityViolationException.class);
    }
}
