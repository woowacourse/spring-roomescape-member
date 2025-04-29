package roomescape.dao;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import roomescape.domain.ReservationTime;

import static org.assertj.core.api.Assertions.assertThat;

class InMemoryReservationTimeDAOTest {

    @Test
    @DisplayName("dao에 존재하는 모든 reservationTime 데이터를 조회한다")
    void findAll() {
        //given
        List<ReservationTime> reservationTimes = List.of(new ReservationTime(LocalTime.of(10, 0)));
        ReservationTimeDAO reservationTimeDAO = new InMemoryReservationTimeDAO(reservationTimes);

        //when
        List<ReservationTime> actual = reservationTimeDAO.findAll();

        //then
        assertThat(actual).hasSize(1);
    }

    @Test
    void findById() {
        //given
        ReservationTimeDAO reservationTimeDAO = new InMemoryReservationTimeDAO(new ArrayList<>());
        long id = reservationTimeDAO.insert(new ReservationTime(LocalTime.of(10, 0)));

        //when
        Optional<ReservationTime> actual = reservationTimeDAO.findById(id);

        //then
        assertThat(actual).isPresent();
    }

    @Test
    @DisplayName("dao에 reservationTime 데이터를 저장한다")
    void insert() {
        //given
        ReservationTimeDAO reservationTimeDAO = new InMemoryReservationTimeDAO(new ArrayList<>());

        //when
        long id = reservationTimeDAO.insert(new ReservationTime(LocalTime.of(10, 0)));

        //then
        assertThat(id).isEqualTo(1L);
    }

    @ParameterizedTest
    @CsvSource(value = {"10:00,10:00,true", "10:00,10:01,false"})
    @DisplayName("dao에서 같은 startAt인 reservationTime 데이터의 존재 여부를 확인한다")
    void existsByStartAt(LocalTime time, LocalTime startAt, boolean expected) {
        //given
        ReservationTimeDAO reservationTimeDAO = new InMemoryReservationTimeDAO(new ArrayList<>());
        reservationTimeDAO.insert(new ReservationTime(time));

        //when
        boolean actual = reservationTimeDAO.existsByStartAt(startAt);

        //then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("dao에 저장된 대상 id 인 reservationTime 데이터를 삭제한다")
    void deleteById() {
        //given
        ReservationTimeDAO reservationTimeDAO = new InMemoryReservationTimeDAO(new ArrayList<>());
        reservationTimeDAO.insert(new ReservationTime(LocalTime.of(10, 0)));

        //when
        boolean actual = reservationTimeDAO.deleteById(1L);

        //then
        assertThat(actual).isTrue();
    }

    @Test
    @DisplayName("dao에 저장되어 있지 않은 대상 id 인 reservationTime 데이터를 삭제 시 false를 return 한다")
    void deleteByNotExistedId() {
        //given
        ReservationTimeDAO reservationTimeDAO = new InMemoryReservationTimeDAO(new ArrayList<>());
        reservationTimeDAO.insert(new ReservationTime(LocalTime.of(10, 0)));

        //when
        boolean actual = reservationTimeDAO.deleteById(100L);

        //then
        assertThat(actual).isFalse();
    }
}
