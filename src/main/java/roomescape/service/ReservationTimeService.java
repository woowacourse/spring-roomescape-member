package roomescape.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.ReservationTime;
import roomescape.dto.ReservationTimeRequestDTO;
import roomescape.dto.ReservationTimeResponseDTO;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;

@Service
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

    @Transactional
    public ReservationTimeResponseDTO addReservationTime(
            ReservationTimeRequestDTO reservationTimeRequest) {
        ReservationTime reservationTime = new ReservationTime(
                LocalTime.parse(reservationTimeRequest.startAt()));

        ReservationTime savedTime = reservationTimeRepository.save(reservationTime);

        return ReservationTimeResponseDTO.from(savedTime);
    }

    public List<ReservationTimeResponseDTO> findAllReservationTime() {
        return reservationTimeRepository.findAll().stream()
                .map(ReservationTimeResponseDTO::from)
                .toList();
    }

    public List<ReservationTime> findReservedTimes(LocalDate selectedDate, Long themeId) {
        return reservationTimeRepository.findReservedTimes(selectedDate, themeId);
    }

    @Transactional
    public void deleteReservationTime(Long id) {
        if (reservationRepository.existByTimeId(id)) {
            throw new IllegalArgumentException("이미 예약된 시간은 삭제할 수 없습니다.");
        }
        reservationTimeRepository.delete(id);
    }
}
