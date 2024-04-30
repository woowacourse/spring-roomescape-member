package roomescape.core.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.core.domain.Reservation;
import roomescape.core.domain.ReservationTime;
import roomescape.core.dto.ReservationTimeRequestDto;
import roomescape.core.dto.ReservationTimeResponseDto;
import roomescape.core.repository.ReservationRepository;
import roomescape.core.repository.ReservationTimeRepository;

@Service
public class ReservationTimeService {
    private final ReservationTimeRepository reservationTimeRepository;
    private final ReservationRepository reservationRepository;

    public ReservationTimeService(final ReservationTimeRepository reservationTimeRepository,
                                  final ReservationRepository reservationRepository) {
        this.reservationTimeRepository = reservationTimeRepository;
        this.reservationRepository = reservationRepository;
    }

    @Transactional
    public ReservationTimeResponseDto create(final ReservationTimeRequestDto request) {
        final ReservationTime reservationTime = new ReservationTime(request.getStartAt());
        final Long id = reservationTimeRepository.save(reservationTime);
        return new ReservationTimeResponseDto(id, reservationTime);
    }

    @Transactional(readOnly = true)
    public List<ReservationTimeResponseDto> findAll() {
        return reservationTimeRepository.findAll()
                .stream()
                .map(ReservationTimeResponseDto::new)
                .toList();
    }

    @Transactional
    public void delete(final long id) {
        final List<Reservation> reservations = reservationRepository.findByTimeId(id);
        if (!reservations.isEmpty()) {
            throw new IllegalArgumentException("Reservation time that have reservations cannot be deleted.");
        }
        reservationTimeRepository.deleteById(id);
    }
}
