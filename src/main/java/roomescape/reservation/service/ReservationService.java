package roomescape.reservation.service;

import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.exception.DataNotFoundException;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.reservation.repository.entity.ReservationEntity;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;

    public Long save(final String name, final LocalDate date, final Long timeId) {
        final ReservationEntity reservationEntity = new ReservationEntity(name, date, timeId);

        return reservationRepository.save(reservationEntity);
    }

    public Reservation getById(final Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("해당 예약 데이터가 존재하지 않습니다. id = " + id));
    }

    public List<Reservation> findAll() {
        return reservationRepository.findAll();
    }

    public void deleteById(final Long id) {
        reservationRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("해당 예약 데이터가 존재하지 않습니다. id = " + id));

        reservationRepository.deleteById(id);
    }
}
