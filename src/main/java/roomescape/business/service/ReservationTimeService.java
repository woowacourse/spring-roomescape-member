package roomescape.business.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import roomescape.business.ReservationTime;
import roomescape.business.dto.AvailableTimesResponseDto;
import roomescape.business.dto.ReservationTimeRequestDto;
import roomescape.business.dto.ReservationTimeResponseDto;
import roomescape.persistence.ReservationRepository;
import roomescape.persistence.ReservationTimeRepository;

@Service
public final class ReservationTimeService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;

    @Autowired
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

    public Long createTime(ReservationTimeRequestDto reservationTimeRequestDto) {
        return reservationTimeRepository.add(new ReservationTime(reservationTimeRequestDto.startAt()));
    }

    public List<AvailableTimesResponseDto> readAvailableTimes(LocalDate date, Long themeId) {
        return reservationTimeRepository.findAvailableTimes(date, themeId);
    }

    public ReservationTimeResponseDto readTimeOne(Long id) {
        ReservationTime reservationTime = reservationTimeRepository.findById(id);
        return new ReservationTimeResponseDto(
                reservationTime.getId(),
                reservationTime.getStartAt()
        );
    }

    public void deleteTime(Long timeId) {
        if (reservationRepository.existsByTimeId(timeId)) {
            throw new IllegalArgumentException("해당 시간의 예약이 존재하여 시간을 삭제할 수 없습니다.");
        }
        reservationTimeRepository.deleteById(timeId);
    }
}
