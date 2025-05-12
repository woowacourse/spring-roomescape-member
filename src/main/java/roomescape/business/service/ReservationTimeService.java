package roomescape.business.service;

import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.time.LocalDate;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;
import roomescape.business.ReservationTime;
import roomescape.exception.ReservationTimeException;
import roomescape.persistence.ReservationRepository;
import roomescape.persistence.ReservationTimeRepository;
import roomescape.presentation.dto.request.ReservationTimeRequestDto;
import roomescape.presentation.dto.response.AvailableTimesResponseDto;
import roomescape.presentation.dto.response.ReservationTimeResponseDto;

@Named
@Transactional
public class ReservationTimeService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;

    @Inject
    public ReservationTimeService(ReservationRepository reservationRepository,
                                  ReservationTimeRepository reservationTimeRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
    }

    public List<ReservationTimeResponseDto> readTimeAll() {
        return reservationTimeRepository.findAll()
                .stream()
                .map(reservationTime -> new ReservationTimeResponseDto(
                        reservationTime.getId(),
                        reservationTime.getStartAt()
                ))
                .toList();
    }

    public ReservationTimeResponseDto readTimeOne(Long id) {
        ReservationTime reservationTime = reservationTimeRepository.findById(id)
                .orElseThrow(() -> new ReservationTimeException("존재하지 않는 예약 시간입니다."));
        return new ReservationTimeResponseDto(
                reservationTime.getId(),
                reservationTime.getStartAt()
        );
    }

    public List<AvailableTimesResponseDto> readAvailableTimes(LocalDate date, Long themeId) {
        return reservationTimeRepository.findAvailableTimes(date, themeId);
    }

    public Long createTime(ReservationTimeRequestDto reservationTimeRequestDto) {
        return reservationTimeRepository.add(new ReservationTime(reservationTimeRequestDto.startAt()));
    }

    public void deleteTime(Long timeId) {
        if (reservationRepository.existsByTimeId(timeId)) {
            throw new ReservationTimeException("해당 시간의 예약이 존재하여 시간을 삭제할 수 없습니다.");
        }
        reservationTimeRepository.deleteById(timeId);
    }
}
