package roomescape.business.service.admin;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.business.domain.reservation.ReservationTime;
import roomescape.exception.ReservationTimeException;
import roomescape.persistence.ReservationRepository;
import roomescape.persistence.ReservationTimeRepository;
import roomescape.presentation.admin.dto.ReservationTimeRequestDto;
import roomescape.presentation.admin.dto.ReservationTimeResponseDto;

@Service
public class AdminReservationTimeService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;

    public AdminReservationTimeService(ReservationRepository reservationRepository,
                                       ReservationTimeRepository reservationTimeRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
    }

    @Transactional
    public ReservationTimeResponseDto createTime(ReservationTimeRequestDto reservationTimeRequestDto) {
        if (reservationTimeRepository.existsByStartAt(reservationTimeRequestDto.startAt())) {
            throw new ReservationTimeException("해당 시간은 이미 존재합니다.");
        }
        ReservationTime reservationTime = reservationTimeRepository.add(
                new ReservationTime(reservationTimeRequestDto.startAt()));
        return ReservationTimeResponseDto.toResponse(reservationTime);
    }

    @Transactional
    public void deleteTimeById(Long timeId) {
        if (reservationRepository.existsByTimeId(timeId)) {
            throw new ReservationTimeException("해당 시간의 예약이 존재하여 시간을 삭제할 수 없습니다.");
        }
        reservationTimeRepository.deleteById(timeId);
    }
}
