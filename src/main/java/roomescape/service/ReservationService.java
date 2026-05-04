package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.exception.EntityNotFoundException;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.service.command.ReservationCreateCommand;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository timeRepository;

    public ReservationService(
            ReservationRepository reservationRepository,
            ReservationTimeRepository timeRepository
    ) {
        this.reservationRepository = reservationRepository;
        this.timeRepository = timeRepository;
    }

    @Transactional
    public Reservation create(
            ReservationCreateCommand createCommand
    ) {
        long timeId = createCommand.timeId();
        ReservationTime time = timeRepository.findById(timeId)
                .orElseThrow(() -> new EntityNotFoundException("예약 시간을 조회할 수 없습니다. id = " + timeId));

        Reservation reservation = Reservation.create(
                createCommand.name(),
                createCommand.date(),
                time
        );

        return reservationRepository.persist(reservation);
    }

    @Transactional
    public List<Reservation> findAll() {
        return reservationRepository.findAll();
    }

    @Transactional
    public void delete(long reservationId) {
        boolean deleted = reservationRepository.delete(reservationId);

        if (!deleted) {
            throw new EntityNotFoundException("삭제할 예약을 조회하지 못했습니다. reservationId = " + reservationId);
        }
    }
}
