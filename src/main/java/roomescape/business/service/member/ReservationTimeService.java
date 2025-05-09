package roomescape.business.service.member;

import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import roomescape.business.domain.reservation.ReservationTime;
import roomescape.exception.ReservationTimeException;
import roomescape.persistence.ReservationTimeRepository;
import roomescape.presentation.admin.dto.ReservationTimeResponseDto;
import roomescape.presentation.member.dto.AvailableTimesResponseDto;

@Service
public class ReservationTimeService {

    private final ReservationTimeRepository reservationTimeRepository;

    @Autowired
    public ReservationTimeService(ReservationTimeRepository reservationTimeRepository) {
        this.reservationTimeRepository = reservationTimeRepository;
    }

    public List<ReservationTimeResponseDto> getAllTimes() {
        return reservationTimeRepository.findAll()
                .stream()
                .map(ReservationTimeResponseDto::toResponse)
                .toList();
    }

    public ReservationTimeResponseDto getTimeById(Long id) {
        ReservationTime reservationTime = reservationTimeRepository.findById(id)
                .orElseThrow(() -> new ReservationTimeException("존재하지 않는 예약 시간입니다."));
        return ReservationTimeResponseDto.toResponse(reservationTime);
    }

    public List<AvailableTimesResponseDto> findAvailableTimes(LocalDate date, Long themeId) {
        return reservationTimeRepository.findAvailableTimes(date, themeId);
    }
}
