package roomescape.time.service;

import java.time.LocalTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.time.domain.ReservationTime;
import roomescape.time.dto.request.ReservationTimeSaveDto;
import roomescape.time.repository.ReservationTimeRepository;

@Service
@Transactional(readOnly = true)
public class ReservationTimeService {

    private final ReservationTimeRepository reservationTimeRepository;
    private final ReservationRepository reservationRepository;

    public ReservationTimeService(ReservationTimeRepository reservationTimeRepository, ReservationRepository reservationRepository) {
        this.reservationTimeRepository = reservationTimeRepository;
        this.reservationRepository = reservationRepository;
    }

    public List<ReservationTime> findAll() {
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
    public ReservationTime delete(Long id) {
        ReservationTime reservationTime = getReservationTime(id);
        validateAlreadyReserved(reservationTime.id());
        boolean isDeleted = reservationTimeRepository.delete(reservationTime.id());
        validateIsDeleted(isDeleted);
        return reservationTime;
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

    private void validateIsDeleted(boolean isDeleted) {
        if (!isDeleted) {
            throw new IllegalArgumentException("예약 시간을 삭제할 수 없습니다.");
        }
    }

    private void validateAlreadyReserved(Long timeId) {
        if (reservationRepository.existsByTimeId(timeId)) {
            throw new IllegalArgumentException("해당 날짜에 예약이 존재하여, 삭제할 수 없습니다.");
        }
    }

}
