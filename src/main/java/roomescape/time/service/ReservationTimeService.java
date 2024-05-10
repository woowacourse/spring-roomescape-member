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

    public List<ReservationTime> readAll() {
        return reservationTimeRepository.readAll();
    }

    public ReservationTime create(ReservationTime reservationTime) {
        long timeId = reservationTimeRepository.create(reservationTime);

        return reservationTimeRepository.find(timeId);
    }

    public void delete(long id) {
        int deleteCount = reservationTimeRepository.delete(id);

        validateDeletionOccurred(deleteCount);
    }

    public List<ReservationTimeStatus> findAvailableTime(String date, long themeId) {
        return reservationTimeRepository.findAvailableTime(date, themeId);
    }

    private static void validateDeletionOccurred(int deleteCount) {
        if (deleteCount == 0) {
            throw new NoSuchElementException("해당하는 시간이 없습니다.");
        }
    }
}
