package roomescape.core.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
        validateDuplicatedStartAt(reservationTime);
        final Long id = reservationTimeRepository.save(reservationTime);
        return new ReservationTimeResponseDto(id, reservationTime);
    }

    private void validateDuplicatedStartAt(final ReservationTime reservationTime) {
        final Integer reservationTimeCount = reservationTimeRepository.countByStartAt(
                reservationTime.getStartAtString());
        if (reservationTimeCount > 0) {
            throw new IllegalArgumentException("해당 시간이 이미 존재합니다.");
        }
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
        final int reservationCount = reservationRepository.countByTimeId(id);
        if (reservationCount > 0) {
            throw new IllegalArgumentException("Reservation time that have reservations cannot be deleted.");
        }
        reservationTimeRepository.deleteById(id);
    }
}
