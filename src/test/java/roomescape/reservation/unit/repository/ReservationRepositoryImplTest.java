package roomescape.reservation.unit.repository;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.reservation.repository.JdbcReservationRepository;
import roomescape.reservation.repository.ReservationRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ReservationRepositoryImplTest {

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;
    private ReservationRepository reservationRepository;

    @BeforeEach
    void beforeEach() {
        reservationRepository = new JdbcReservationRepository(jdbcTemplate);
    }
//
//    @Test
//    @DisplayName("모든 예약을 조회한다.")
//    void findAll() {
//        // given
//        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES (?)", "10:00");
//        jdbcTemplate.update("INSERT INTO reservation (name, date, time_id) VALUES (?, ?, ?)",
//                "브라운", "2023-08-05", 1L);
//
//        // when
//        List<Reservation> reservations = reservationRepository.findAll();
//
//        // then
//        Reservation expected = new Reservation(
//                1L,
//                "브라운",
//                LocalDate.of(2023, 8, 5),
//                new ReservationTime(
//                        1L,
//                        LocalTime.of(10, 0)
//                ));
//        assertThat(reservations.size()).isEqualTo(1);
//        assertThat(reservations.getFirst()).isEqualTo(expected);
//    }
//
//    @Test
//    @DisplayName("id로 예약을 조회한다.")
//    void findById() {
//        // given
//        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES (?)", "10:00");
//        jdbcTemplate.update("INSERT INTO reservation (name, date, time_id) VALUES (?, ?, ?)",
//                "브라운", "2023-08-05", 1L);
//
//        // when
//        Optional<Reservation> reservation = reservationRepository.findById(1L);
//
//        // then
//        Reservation expected = new Reservation(
//                1L,
//                "브라운",
//                LocalDate.of(2023, 8, 5),
//                new ReservationTime(
//                        1L,
//                        LocalTime.of(10, 0)
//                ));
//        assertThat(reservation.get()).isEqualTo(expected);
//    }
//
//    @Test
//    @DisplayName("예약을 저장한다.")
//    void save() {
//        // given
//        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES (?)", "10:00");
//        Reservation reservation = new Reservation(
//                null,
//                "미소",
//                LocalDate.of(2025, 4, 21),
//                new ReservationTime(
//                        1L,
//                        LocalTime.of(10, 0)
//                )
//        );
//
//        // when
//        Reservation saved = reservationRepository.save(reservation);
//
//        // then
//        Reservation found = jdbcTemplate.queryForObject(
//                "select r.id, r.name, r.date, rt.id as time_id, rt.start_at "
//                        + "from reservation r "
//                        + "inner join reservation_time rt on r.time_id = rt.id "
//                        + "where r.id = ?",
//                (resultSet, rowNum) ->
//                        new Reservation(
//                                resultSet.getLong("id"),
//                                resultSet.getString("name"),
//                                LocalDate.parse(resultSet.getString("date")),
//                                new ReservationTime(
//                                        resultSet.getLong("time_id"),
//                                        LocalTime.parse(resultSet.getString("start_at"))
//                                )
//                        ),
//                1L
//        );
//        assertThat(found.getId()).isEqualTo(1L);
//        assertThat(found.getName()).isEqualTo(reservation.getName());
//        assertThat(found.getDate()).isEqualTo(reservation.getDate());
//        assertThat(found.getTime()).isEqualTo(reservation.getTime());
//        assertThat(found).isEqualTo(saved);
//    }
//
//    @Test
//    @DisplayName("id로 예약을 삭제한다.")
//    void deleteById() {
//        // given
//        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES (?)", "10:00");
//        jdbcTemplate.update("INSERT INTO reservation (name, date, time_id) VALUES (?, ?, ?)",
//                "브라운", "2023-08-05", 1L);
//
//        // when
//        reservationRepository.deleteById(1L);
//
//        // then
//        List<Reservation> reservations = jdbcTemplate.query(
//                "select r.id, r.name, r.date, rt.id as time_id, rt.start_at "
//                        + "from reservation r "
//                        + "inner join reservation_time rt on r.time_id = rt.id",
//                (resultSet, rowNum) -> new Reservation(
//                        resultSet.getLong("id"),
//                        resultSet.getString("name"),
//                        LocalDate.parse(resultSet.getString("date")),
//                        new ReservationTime(
//                                resultSet.getLong("time_id"),
//                                LocalTime.parse(resultSet.getString("start_at"))
//                        )
//                ));
//        assertThat(reservations.size()).isEqualTo(0);
//    }
}
