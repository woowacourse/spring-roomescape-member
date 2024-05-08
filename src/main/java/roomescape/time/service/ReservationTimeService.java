package roomescape.time.service;

import org.springframework.stereotype.Service;
import roomescape.time.domain.ReservationTime;
import roomescape.time.domain.ReservationTimeStatus;
import roomescape.time.repository.ReservationTimeRepository;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ReservationTimeService {
    private final ReservationTimeRepository reservationTimeRepository;

    public ReservationTimeService(final ReservationTimeRepository reservationTimeRepository) {
        this.reservationTimeRepository = reservationTimeRepository;
    }

    public List<ReservationTime> findAll() {
        return reservationTimeRepository.findAll();
    }

    public ReservationTime save(final ReservationTime reservationTime) {
        final long timeId = reservationTimeRepository.save(reservationTime);

        return reservationTimeRepository.findById(timeId);
    }

    public void deleteById(final long id) {
        final int deleteCount = reservationTimeRepository.deleteById(id);

        validateDeletionOccurred(deleteCount);
    }

    public List<ReservationTimeStatus> findAvailableTime(final String date, final long themeId) {
        return reservationTimeRepository.findAvailableTime(date, themeId);
    }

    private static void validateDeletionOccurred(int deleteCount) {
        if (deleteCount == 0) {
            throw new NoSuchElementException("해당하는 시간이 없습니다.");
        }
    }
}
