package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.dao.InMemoryReservationTimeDAO;
import roomescape.domain.ReservationTime;
import roomescape.service.dto.ReservationTimeCreation;

class ReservationTimeServiceTest {

    @Test
    @DisplayName("시간 데이터를 조회할 수 있어야 한다")
    void findAll() {
        //given
        ReservationTimeService reservationTimeService = new ReservationTimeService(
                new InMemoryReservationTimeDAO(new ArrayList<>()));
        reservationTimeService.addReservationTime(new ReservationTimeCreation(LocalTime.of(10, 0)));

        //when
        List<ReservationTime> actual = reservationTimeService.findAll();

        //then
        assertThat(actual).hasSize(1);
    }

    @Test
    @DisplayName("시간 데이터를 추가할 수 있어야 한다")
    void addReservationTime() {
        //given
        ReservationTimeService reservationTimeService = new ReservationTimeService(
                new InMemoryReservationTimeDAO(new ArrayList<>()));

        //when
        ReservationTimeCreation creation = new ReservationTimeCreation(LocalTime.of(10, 0));
        reservationTimeService.addReservationTime(creation);

        //then
        assertThat(reservationTimeService.findAll()).hasSize(1);
    }

    @Test
    @DisplayName("이미 존재하는 시간 데이터일 경우 예외를 던진다")
    void cannotAddReservationTime() {
        //given
        ReservationTimeService reservationTimeService = new ReservationTimeService(
                new InMemoryReservationTimeDAO(new ArrayList<>()));
        ReservationTimeCreation creation = new ReservationTimeCreation(LocalTime.of(10, 0));
        reservationTimeService.addReservationTime(creation);
        ReservationTimeCreation duplicatedTime = new ReservationTimeCreation(LocalTime.of(10, 0));

        //when & then
        assertThatThrownBy(() -> reservationTimeService.addReservationTime(duplicatedTime))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 존재하는 예약 가능 시간입니다: %s".formatted(duplicatedTime.startAt()));
    }

    @Test
    @DisplayName("id를 기반으로 시간 데이터를 삭제할 수 있어야 한다")
    void deleteById() {
        //given
        ReservationTimeService reservationTimeService = new ReservationTimeService(
                new InMemoryReservationTimeDAO(new ArrayList<>()));
        reservationTimeService.addReservationTime(new ReservationTimeCreation(LocalTime.of(10, 0)));
        ReservationTime savedTime = reservationTimeService.findAll().getFirst();

        //when
        boolean actual = reservationTimeService.deleteById(savedTime.getId());

        //then
        assertThat(actual).isTrue();
    }

    @Test
    @DisplayName("존재하지 않는 id를 기반으로 시간 데이터를 삭제하면 false를 리턴해야 한다")
    void deleteNotExistTimeById() {
        //given
        ReservationTimeService reservationTimeService = new ReservationTimeService(
                new InMemoryReservationTimeDAO(new ArrayList<>()));

        //when
        boolean actual = reservationTimeService.deleteById(100L);

        //then
        assertThat(actual).isFalse();
    }
}
