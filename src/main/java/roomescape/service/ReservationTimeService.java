package roomescape.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.ReservationTime;
import roomescape.dto.ReservationTimeRequestDTO;
import roomescape.dto.ReservationTimeResponseDTO;
import roomescape.exception.ReservationTimeInUseException;
import roomescape.exception.ReservationTimeNotFoundException;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;

@Service
@Transactional
public class ReservationTimeService {

    private final ReservationTimeRepository reservationTimeRepository;
    private final ReservationRepository reservationRepository;

    public ReservationTimeService(
            ReservationTimeRepository reservationTimeRepository,
            ReservationRepository reservationRepository
    ) {
        this.reservationTimeRepository = reservationTimeRepository;
        this.reservationRepository = reservationRepository;
    }

    public ReservationTimeResponseDTO addReservationTime(ReservationTimeRequestDTO reservationTimeRequest) {
        ReservationTime reservationTime = ReservationTime.withoutId(reservationTimeRequest.startAt());

        ReservationTime savedTime = reservationTimeRepository.save(reservationTime);
        return ReservationTimeResponseDTO.from(savedTime);
    }

    public List<ReservationTimeResponseDTO> findAllReservationTime() {
        return reservationTimeRepository.findAll().stream()
                .map(ReservationTimeResponseDTO::from)
                .toList();
    }

    public List<ReservationTimeResponseDTO> findReservedTimes(LocalDate selectedDate, Long themeId) {
        return reservationTimeRepository.findReservedTimes(selectedDate, themeId)
                .stream()
                .map(ReservationTimeResponseDTO::from)
                .toList();
    }

    public void deleteReservationTime(Long id) {
        if (reservationRepository.existsReservationByTimeId(id)) {
            throw new ReservationTimeInUseException("예약 시간 삭제 실패 (사용 중): " + id);
        }
        if (reservationTimeRepository.findById(id).isEmpty()) {
            throw new ReservationTimeNotFoundException("예약 시간 삭제 실패 (존재하지 않음): " + id);
        }
        reservationTimeRepository.delete(id);
    }
}
