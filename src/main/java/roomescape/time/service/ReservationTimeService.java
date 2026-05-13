package roomescape.time.service;

import java.time.LocalTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.time.domain.ReservationTime;
import roomescape.time.dto.request.ReservationTimeSaveDto;
import roomescape.time.repository.ReservationTimeRepository;

@Service
@Transactional(readOnly = true)
public class ReservationTimeService {

    private final ReservationTimeRepository reservationTimeRepository;

    public ReservationTimeService(ReservationTimeRepository reservationTimeRepository) {
        this.reservationTimeRepository = reservationTimeRepository;
    }

    public List<ReservationTime> readAll() {
        return reservationTimeRepository.findAll();
    }

    public List<ReservationTime> readAvailableTimes(Long dateId, Long themeId) {
        return reservationTimeRepository.findAvailableByDateIdAndThemeId(dateId, themeId);
    }

    @Transactional
    public ReservationTime register(ReservationTimeSaveDto dto) {
        validateDuplicateTimeExist(dto.startAt());
        return reservationTimeRepository.save(ReservationTime.create(dto.startAt()));
    }

    @Transactional
    public ReservationTime updateStatus(Long id, boolean isActive) {
        ReservationTime reservationTime = getReservationTime(id);
        reservationTime.updateStatus(isActive);
        boolean isUpdated = reservationTimeRepository.updateStatus(reservationTime);
        validateIsUpdated(isUpdated);
        return reservationTime;
    }

    private void validateIsUpdated(boolean isUpdated) {
        if (!isUpdated) {
            throw new IllegalArgumentException("활성화/비활성화 상태 변경에 실패했습니다.");
        }
    }

    private ReservationTime getReservationTime(Long id) {
        return reservationTimeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 예약 시간입니다."));
    }

    private void validateDuplicateTimeExist(LocalTime startAt) {
        if (reservationTimeRepository.existsByStartAt(startAt)) {
            throw new IllegalArgumentException("이미 존재하는 예약 시간입니다.");
        }
    }

}
