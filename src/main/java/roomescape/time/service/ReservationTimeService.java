package roomescape.time.service;

import java.time.LocalTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.common.exception.ConflictException;
import roomescape.common.exception.NotFoundException;
import roomescape.time.domain.ReservationTime;
import roomescape.time.dto.request.ReservationTimeSaveDto;
import roomescape.time.dto.response.ReservationTimeDetailDto;
import roomescape.time.repository.ReservationTimeRepository;

@Service
public class ReservationTimeService {
    private final ReservationTimeRepository reservationTimeRepository;

    public ReservationTimeService(ReservationTimeRepository reservationTimeRepository) {
        this.reservationTimeRepository = reservationTimeRepository;
    }

    @Transactional(readOnly = true)
    public List<ReservationTimeDetailDto> findAll() {
        return reservationTimeRepository.findAll().stream()
                .map(ReservationTimeDetailDto::from)
                .toList();
    }

    @Transactional
    public ReservationTimeDetailDto create(ReservationTimeSaveDto reservationTimeSaveDto) {
        LocalTime startAt = reservationTimeSaveDto.startAt();
        validateDuplicateTimeExist(startAt);
        Long id = reservationTimeRepository.save(ReservationTime.create(startAt));
        return new ReservationTimeDetailDto(id, startAt);
    }

    private void validateDuplicateTimeExist(LocalTime startAt) {
        if (reservationTimeRepository.existsByStartAt(startAt)) {
            throw new ConflictException("이미 존재하는 예약 시간입니다.");
        }
    }

    @Transactional
    public ReservationTimeDetailDto delete(Long id) {
        ReservationTime reservationTime = reservationTimeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 예약 시간입니다."));
        reservationTimeRepository.delete(id);
        return ReservationTimeDetailDto.from(reservationTime);
    }
}
