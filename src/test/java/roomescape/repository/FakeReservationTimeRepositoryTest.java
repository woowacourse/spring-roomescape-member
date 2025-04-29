package roomescape.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import roomescape.domain.ReservationTime;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class FakeReservationTimeRepositoryTest {

    ReservationTimeRepository reservationTimeRepository;

    @DisplayName("Reservation Time을 저장할 수 있다")
    @Test
    void saveReservationTimeTest() {
        reservationTimeRepository = new FakeReservationTimeRepository(new ArrayList<>());
        ReservationTime reservationTime = new ReservationTime(null, LocalTime.now());

        Optional<ReservationTime> optionalReservationTime = reservationTimeRepository.save(reservationTime);
        ReservationTime savedReservation = optionalReservationTime.get();
        Long saveId = savedReservation.id();

        assertThat(saveId).isEqualTo(1L);
    }

    @Nested
    @DisplayName("예약 시간 조회")
    class ReservationTimeFindTest {

        @DisplayName("저장된 Reservation Time을 불러올 수 있다")
        @Test
        void findAllReservationTimeTest() {
            ReservationTime reservationTime1 = new ReservationTime(1L, LocalTime.now());
            ReservationTime reservationTime2 = new ReservationTime(2L, LocalTime.now());
            ReservationTime reservationTime3 = new ReservationTime(3L, LocalTime.now());
            reservationTimeRepository = new FakeReservationTimeRepository(List.of(reservationTime1, reservationTime2, reservationTime3));

            List<ReservationTime> allReservationTimes = reservationTimeRepository.findAll();

            Long reservationTimeId1 = allReservationTimes.get(0).id();
            Long reservationTimeId2 = allReservationTimes.get(1).id();
            Long reservationTimeId3 = allReservationTimes.get(2).id();

            assertAll(
                    () -> assertThat(reservationTimeId1).isEqualTo(1L),
                    () -> assertThat(reservationTimeId2).isEqualTo(2L),
                    () -> assertThat(reservationTimeId3).isEqualTo(3L),
                    () -> assertThat(allReservationTimes).hasSize(3)
            );
        }

        @DisplayName("저장되지 않은 예약 시간을 불러오면 빈 Optional 반환된다")
        @Test
        void findInvalidReservationTimeTest() {
            reservationTimeRepository = new FakeReservationTimeRepository(new ArrayList<>());

            assertThat(reservationTimeRepository.findById(2L)).isEmpty();
        }
    }

    @Nested
    @DisplayName("예약 시간 삭제")
    class ReservationTimeDeleteTest {

        @DisplayName("존재하는 Id의 Reservation Time을 삭제할 수 있다")
        @Test
        void deleteReservationTimeTest() {
            ArrayList<ReservationTime> reservationTimes = new ArrayList<>();
            reservationTimes.add(new ReservationTime(1L, LocalTime.now()));
            reservationTimeRepository = new FakeReservationTimeRepository(reservationTimes);

            Long deleteId = 1L;

            assertDoesNotThrow(() -> reservationTimeRepository.deleteById(deleteId));
        }

        @DisplayName("삭제된 Id의 갯수만큼 반환된다")
        @Test
        void deleteReservationTimeCountTest() {
            ArrayList<ReservationTime> reservationTimes = new ArrayList<>();
            reservationTimes.add(new ReservationTime(1L, LocalTime.now()));
            reservationTimes.add(new ReservationTime(1L, LocalTime.now()));
            reservationTimes.add(new ReservationTime(1L, LocalTime.now()));
            reservationTimeRepository = new FakeReservationTimeRepository(reservationTimes);

            Long deleteId = 1L;
            int deletedCount = reservationTimeRepository.deleteById(deleteId);

            Assertions.assertThat(deletedCount).isEqualTo(3);
        }

        @DisplayName("삭제된 Id의 갯수만큼 반환된다")
        @Test
        void deleteReservationTimeNotExistIdCountTest() {
            ArrayList<ReservationTime> reservationTimes = new ArrayList<>();
            reservationTimes.add(new ReservationTime(1L, LocalTime.now()));
            reservationTimes.add(new ReservationTime(1L, LocalTime.now()));
            reservationTimes.add(new ReservationTime(1L, LocalTime.now()));
            reservationTimeRepository = new FakeReservationTimeRepository(reservationTimes);

            Long deleteId = 2L;
            int deletedCount = reservationTimeRepository.deleteById(deleteId);

            Assertions.assertThat(deletedCount).isEqualTo(0);
        }
    }
}
