package roomescape.reservation.service;

import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.exception.DataNotFoundException;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.time.service.ReservationTimeService;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    //TODO : ReservationTimeRepository에 의존하도록 수정해야함.
    private final ReservationTimeService reservationTimeService;

    public Long save(final String name, final LocalDate date, final Long time_id) {
        final Reservation reservation = new Reservation(name, date, reservationTimeService.getById(time_id));

        return reservationRepository.save(reservation);
    }

    public Reservation getById(final Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("해당 예약 데이터가 존재하지 않습니다. id = " + id));
    }

    public List<Reservation> findAll() {
        return reservationRepository.findAll();
    }

    public void deleteById(final Long id) {
        final Reservation found = reservationRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("해당 예약 데이터가 존재하지 않습니다. id = " + id));

        reservationRepository.delete(found);
    }
}
