package roomescape.time.service;

import org.springframework.stereotype.Service;
import roomescape.time.domain.ReservationTime;
import roomescape.time.dto.ReservationTimeStatus;
import roomescape.time.repository.ReservationTimeRepository;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ReservationTimeService {
    private final ReservationTimeRepository reservationTimeRepository;

    public ReservationTimeService(ReservationTimeRepository reservationTimeRepository) {
        this.reservationTimeRepository = reservationTimeRepository;
    }

    public ReservationTime saveTime(ReservationTime reservationTime) {
        long timeId = reservationTimeRepository.save(reservationTime);

        return reservationTimeRepository.findById(timeId)
                .orElseThrow(() -> new IllegalArgumentException("시간이 없습니다."));
    }

    public List<ReservationTime> findTimeList() {
        List<ReservationTime> times = reservationTimeRepository.findAll();
        validateTimeExists(times);

        return times;
    }

    public void deleteTimeById(long id) {
        int deleteCount = reservationTimeRepository.delete(id);

        validateDeletionOccurred(deleteCount);
    }

    public List<ReservationTimeStatus> findTimeListByDateAndThemeId(String date, long themeId) {
        return reservationTimeRepository.findByDateAndThemeId(date, themeId);
    }

    private void validateTimeExists(List<ReservationTime> times) {
        if (times.isEmpty()) {
            throw new IllegalArgumentException("시간이 없습니다.");
        }
    }

    private void validateDeletionOccurred(int deleteCount) {
        if (deleteCount == 0) {
            throw new NoSuchElementException("해당하는 시간이 없습니다.");
        }
    }
}
