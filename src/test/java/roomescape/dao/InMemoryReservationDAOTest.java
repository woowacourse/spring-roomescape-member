package roomescape.dao;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;

import static org.assertj.core.api.Assertions.assertThat;

class InMemoryReservationDAOTest {

    @Test
    @DisplayName("dao에서 모든 reservation 데이터를 조회한다")
    void findAll() {
        //given
        List<Reservation> reservations = List.of(new Reservation(1, "test",
                LocalDate.of(2025, 1, 1),
                new ReservationTime(1, LocalTime.of(10, 0))));
        ReservationDAO reservationDAO = new InMemoryReservationDAO(reservations);

        //when
        List<Reservation> actual = reservationDAO.findAll();

        //then
        assertThat(actual).hasSize(1);
    }

    @ParameterizedTest
    @CsvSource(value = {"2025-01-01,1,true", "2025-01-02,1,false", "2025-01-01,2,false"})
    @DisplayName("dao에서 특정 date와 time_id를 가진 reservation 데이터가 존재하는지 확인한다")
    void existsByDateAndTimeId(LocalDate date, long timeId, boolean expected) {
        //given
        List<Reservation> reservations = List.of(new Reservation(1, "test",
                LocalDate.of(2025, 1, 1),
                new ReservationTime(1, LocalTime.of(10, 0))));
        ReservationDAO reservationDAO = new InMemoryReservationDAO(reservations);

        //when
        boolean actual = reservationDAO.existsByDateAndTimeId(date, timeId);

        //then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("dao에 reservation 데이터를 저장한다")
    void insert() {
        //given
        ReservationDAO reservationDAO = new InMemoryReservationDAO(new ArrayList<>());
        Reservation reservation = new Reservation("test",
                LocalDate.of(2025, 1, 1),
                new ReservationTime(1, LocalTime.of(1, 1)));

        //when
        long actual = reservationDAO.insert(reservation);

        //then
        assertThat(actual).isEqualTo(1L);
    }

    @Test
    @DisplayName("dao에 존재하는 대상 id 인 reservation 데이터를 삭제한다")
    void deleteById() {
        //given
        ReservationDAO reservationDAO = new InMemoryReservationDAO(new ArrayList<>());
        Reservation reservation = new Reservation("test",
                LocalDate.of(2025, 1, 1),
                new ReservationTime(1, LocalTime.of(1, 1)));
        long id = reservationDAO.insert(reservation);

        //when
        boolean actual = reservationDAO.deleteById(id);

        //then
        assertThat(actual).isTrue();
    }

    @Test
    @DisplayName("dao에 존재하지 않는 대상 id 인 reservation 데이터를 삭제 시도 시 false를 return 한다")
    void deleteByNotExistedId() {
        //given
        ReservationDAO reservationDAO = new InMemoryReservationDAO(new ArrayList<>());
        Reservation reservation = new Reservation("test",
                LocalDate.of(2025, 1, 1),
                new ReservationTime(1, LocalTime.of(1, 1)));
        reservationDAO.insert(reservation);

        //when
        boolean actual = reservationDAO.deleteById(100L);

        //then
        assertThat(actual).isFalse();
    }
}
