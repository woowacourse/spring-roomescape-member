package roomescape.business.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.business.domain.reservation.ReservationTime;
import roomescape.exception.ReservationTimeException;
import roomescape.presentation.mapper.ReservationTimeMapper;
import roomescape.persistence.ReservationRepository;
import roomescape.persistence.ReservationTimeRepository;
import roomescape.presentation.dto.AvailableTimesResponseDto;
import roomescape.presentation.dto.ReservationTimeRequestDto;
import roomescape.presentation.dto.ReservationTimeResponseDto;

@Service
public class ReservationTimeService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;

    @Autowired
    public ReservationTimeService(ReservationRepository reservationRepository,
                                  ReservationTimeRepository reservationTimeRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
    }

    public List<ReservationTimeResponseDto> getAllTimes() {
        return reservationTimeRepository.findAll()
                .stream()
                .map(ReservationTimeMapper::toResponse)
                .toList();
    }

    public ReservationTimeResponseDto getTimeById(Long id) {
        ReservationTime reservationTime = reservationTimeRepository.findById(id)
                .orElseThrow(() -> new ReservationTimeException("존재하지 않는 예약 시간입니다."));
        return ReservationTimeMapper.toResponse(reservationTime);
    }

    public List<AvailableTimesResponseDto> findAvailableTimes(LocalDate date, Long themeId) {
        return reservationTimeRepository.findAvailableTimes(date, themeId);
    }

    @Transactional
    public Long createTime(ReservationTimeRequestDto reservationTimeRequestDto) {
        if (reservationTimeRepository.existsByStartAt(reservationTimeRequestDto.startAt())) {
            throw new ReservationTimeException("해당 시간은 이미 존재합니다.");
        }
        return reservationTimeRepository.add(new ReservationTime(reservationTimeRequestDto.startAt()));
    }

    @Transactional
    public void deleteTimeById(Long timeId) {
        if (reservationRepository.existsByTimeId(timeId)) {
            throw new ReservationTimeException("해당 시간의 예약이 존재하여 시간을 삭제할 수 없습니다.");
        }
        reservationTimeRepository.deleteById(timeId);
    }
}
