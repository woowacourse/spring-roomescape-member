package roomescape.reservation.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.reservationtime.domain.ReservationTime;
import roomescape.reservationtime.repository.ReservationTimeRepository;

@Service
@Transactional(readOnly = true)
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;

    public ReservationService(
            final ReservationRepository reservationRepository,
            final ReservationTimeRepository reservationTimeRepository
    ) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
    }

    public List<Reservation> getAll() {
        return reservationRepository.findAll();
    }


    @Transactional
    public Reservation save(final String name, final LocalDate date, final Long timeId) {
        ReservationTime reservationTime = reservationTimeRepository.findById(timeId)
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] 찾는 시간이 없습니다"));

        if (reservationRepository.existsByDateAndTimeId(date, timeId)) {
            throw new IllegalArgumentException("[ERROR] 동일한 시기에 예약을 할 수 없습니다.");
        }

        Reservation reservation =
                Reservation.createNew(name, date, reservationTime);

        return reservationRepository.save(reservation);
    }

    @Transactional
    public void deleteById(final long id) {
        reservationRepository.deleteById(id);
    }

}
