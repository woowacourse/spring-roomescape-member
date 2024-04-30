package roomescape.core.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.core.domain.Reservation;
import roomescape.core.domain.ReservationTime;
import roomescape.core.dto.ReservationRequestDto;
import roomescape.core.dto.ReservationResponseDto;
import roomescape.core.repository.ReservationRepository;
import roomescape.core.repository.ReservationTimeRepository;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;

    public ReservationService(final ReservationRepository reservationRepository,
                              final ReservationTimeRepository reservationTimeRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
    }

    @Transactional
    public ReservationResponseDto create(final ReservationRequestDto request) {
        final ReservationTime reservationTime = reservationTimeRepository.findById(request.getTimeId());
        final Reservation reservation = new Reservation(request.getName(), request.getDate(), reservationTime);
        final Long id = reservationRepository.save(reservation);

        return new ReservationResponseDto(id, reservation);
    }

    @Transactional(readOnly = true)
    public List<ReservationResponseDto> findAll() {
        return reservationRepository.findAll()
                .stream()
                .map(ReservationResponseDto::new)
                .toList();
    }

    @Transactional
    public void delete(final long id) {
        reservationRepository.deleteById(id);
    }
}
