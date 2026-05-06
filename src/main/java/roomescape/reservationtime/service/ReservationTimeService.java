package roomescape.reservationtime.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.reservationtime.entity.ReservationTime;
import roomescape.reservationtime.payload.ReservationTimeRequest;
import roomescape.reservationtime.repository.ReservationTimeRepository;

@Service
public class ReservationTimeService {

    private final ReservationTimeRepository reservationTimeRepository;
    private final ReservationRepository reservationRepository;

    public ReservationTimeService(ReservationTimeRepository reservationTimeRepository,
                                  ReservationRepository reservationRepository) {
        this.reservationTimeRepository = reservationTimeRepository;
        this.reservationRepository = reservationRepository;
    }

    @Transactional
    public ReservationTime save(ReservationTimeRequest request) {
        return reservationTimeRepository.save(request.startAt());
    }

    @Transactional(readOnly = true)
    public List<ReservationTime> findAll() {
        return reservationTimeRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<ReservationTime> findAvailableReservationTimes(LocalDate date, Long themeId) {
        List<ReservationTime> reservationTimes = reservationTimeRepository.findAll();
        List<Long> reservedTimeIdsByDateAndThemeId =
                reservationRepository.findReservedTimeIdsByDateAndThemeId(date, themeId);

        return reservationTimes.stream()
                .filter(reservationTime -> !reservedTimeIdsByDateAndThemeId.contains(reservationTime.getId()))
                .toList();
    }

    @Transactional
    public void deleteById(Long id) {
        reservationTimeRepository.deleteById(id);
    }

}
