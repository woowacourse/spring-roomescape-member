package roomescape.fixture;

import java.time.LocalTime;
import roomescape.reservation.domain.ReservationTime;
import roomescape.reservation.domain.ReservationTimeRepository;

public class ReservationTimeFixture {

    public static final ReservationTime NOT_SAVED_RESERVATION_TIME_1 = new ReservationTime(LocalTime.of(10, 0));
    public static final ReservationTime NOT_SAVED_RESERVATION_TIME_2 = new ReservationTime(LocalTime.of(11, 0));
    public static final ReservationTime NOT_SAVED_RESERVATION_TIME_3 = new ReservationTime(LocalTime.of(12, 0));
    public static final ReservationTime NOT_SAVED_RESERVATION_TIME_4 = new ReservationTime(LocalTime.of(13, 0));

    public static ReservationTime getSavedReservationTime1(final ReservationTimeRepository reservationTimeRepository) {
        final Long id = reservationTimeRepository.save(NOT_SAVED_RESERVATION_TIME_1);
        return reservationTimeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("예약 시간이 존재하지 않습니다."));
    }

    public static ReservationTime getSavedReservationTime2(final ReservationTimeRepository reservationTimeRepository) {
        final Long id = reservationTimeRepository.save(NOT_SAVED_RESERVATION_TIME_2);
        return reservationTimeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("예약 시간이 존재하지 않습니다."));
    }

    public static ReservationTime getSavedReservationTime3(final ReservationTimeRepository reservationTimeRepository) {
        final Long id = reservationTimeRepository.save(NOT_SAVED_RESERVATION_TIME_3);
        return reservationTimeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("예약 시간이 존재하지 않습니다."));
    }

    public static ReservationTime getSavedReservationTime4(final ReservationTimeRepository reservationTimeRepository) {
        final Long id = reservationTimeRepository.save(NOT_SAVED_RESERVATION_TIME_4);
        return reservationTimeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("예약 시간이 존재하지 않습니다."));
    }
}
