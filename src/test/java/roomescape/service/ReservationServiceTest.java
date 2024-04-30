package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.console.dao.InMemoryReservationDao;
import roomescape.console.dao.InMemoryReservationTimeDao;
import roomescape.console.db.InMemoryReservationDb;
import roomescape.console.db.InMemoryReservationTimeDb;
import roomescape.dao.ReservationDao;
import roomescape.dao.ReservationTimeDao;
import roomescape.domain.ReservationTime;
import roomescape.dto.ReservationRequest;
import roomescape.dto.ReservationResponse;

class ReservationServiceTest {
    private ReservationService reservationService;
    private ReservationTimeDao reservationTimeDao;

    @BeforeEach
    void setUp() {
        InMemoryReservationDb inMemoryReservationDb = new InMemoryReservationDb();
        InMemoryReservationTimeDb inMemoryReservationTimeDb = new InMemoryReservationTimeDb();
        reservationTimeDao = new InMemoryReservationTimeDao(
                inMemoryReservationDb, inMemoryReservationTimeDb);
        ReservationDao reservationDao = new InMemoryReservationDao(inMemoryReservationDb);
        reservationService = new ReservationService(
                reservationDao, reservationTimeDao);
    }

    @DisplayName("모든 예약 검색")
    @Test
    void findAll() {
        assertThat(reservationService.findAll()).isEmpty();
    }

    @DisplayName("예약 저장")
    @Test
    void save() {
        //given
        reservationTimeDao.save(new ReservationTime("10:00"));
        ReservationRequest reservationRequest = new ReservationRequest("aa", "2024-10-10", 1L);
        //when
        ReservationResponse response = reservationService.save(reservationRequest);
        //then
        assertAll(
                () -> assertThat(reservationService.findAll()).hasSize(1),
                () -> assertThat(response.id()).isEqualTo(1),
                () -> assertThat(response.name()).isEqualTo("aa"),
                () -> assertThat(response.date()).isEqualTo("2024-10-10"),
                () -> assertThat(response.time().id()).isEqualTo(1),
                () -> assertThat(response.time().startAt()).isEqualTo("10:00")
        );
    }

    @DisplayName("삭제 테스트")
    @Test
    void deleteById() {
        //given
        reservationTimeDao.save(new ReservationTime("10:00"));
        reservationService.save(new ReservationRequest("aa", "2024-10-10", 1L));
        //when
        reservationService.deleteById(1);
        //then
        assertThat(reservationService.findAll()).isEmpty();
    }
}
