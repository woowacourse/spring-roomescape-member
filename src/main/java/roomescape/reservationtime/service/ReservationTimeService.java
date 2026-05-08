package roomescape.reservationtime.service;

import java.time.LocalTime;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.reservationtime.domain.ReservationTime;
import roomescape.reservationtime.repository.ReservationTimeRepository;

@Service
public class ReservationTimeService {

    private final ReservationTimeRepository reservationTimeRepository;
    private final ReservationRepository reservationRepository;

    public ReservationTimeService(
            final ReservationTimeRepository reservationTimeRepository,
            final ReservationRepository reservationRepository
    ) {
        this.reservationTimeRepository = reservationTimeRepository;
        this.reservationRepository = reservationRepository;
    }


    public ReservationTime save(final LocalTime startAt) {
        ReservationTime reservationTime = ReservationTime.createNew(startAt);

        if (reservationTimeRepository.existsByStartAt(startAt)) {
            throw new IllegalArgumentException("[ERROR] 같은 시간을 추가할 수 없습니다.");
        }

        return reservationTimeRepository.save(reservationTime);
    }

    public List<ReservationTime> findAvailableTimes(final LocalDate date, final long themeId) {
        return reservationTimeRepository.findAvailableTimes(date, themeId);
    }

    public void deleteById(final long timeId) {
        if (reservationRepository.existsByTimeId(timeId)) {
            throw new IllegalArgumentException("[ERROR] 이미 예약된 시간은 삭제할 수 없습니다.");
        }
        reservationTimeRepository.deleteById(timeId);
    }

    public List<ReservationTime> getAll() {
        return reservationTimeRepository.findAll();
    }

    public ReservationTime getById(final long timeId) {
        return reservationTimeRepository.findById(timeId)
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] 찾는 시간이 없습니다"));
    }
}
