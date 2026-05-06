package roomescape.reservation.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Service;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.reservationtime.domain.ReservationTime;
import roomescape.reservationtime.service.ReservationTimeService;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final ReservationTimeService reservationTimeService;

    public ReservationService(
            final ReservationRepository reservationRepository,
            final ReservationTimeService reservationTimeService
    ) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeService = reservationTimeService;
    }

    public List<Reservation> getAll() {
        return reservationRepository.findAll();
    }

    public List<ReservationTime> findAvailableTimes(final LocalDate date, final Long themeId) {
        Set<Long> reservedTimeIds = Set.copyOf(reservationRepository.findAllByDateAndThemeId(date, themeId));

        return reservationTimeService.findAllByThemeId(themeId).stream()
                .filter(reservationTime -> !reservedTimeIds.contains(reservationTime.getId()))
                .toList();
    }

<<<<<<< HEAD
    public Reservation save(final String name, final LocalDate date, final Long timeId) {
        ReservationTime reservationTime = reservationTimeService.getById(timeId);
=======
    public Reservation save(final String name, final LocalDate date, final Long timeId) {
        ReservationTime reservationTime = reservationTimeService.getById(timeId);
>>>>>>> 7251e8a (refactor: reservationTime 조회 조건 변경)

        if(reservationRepository.existsByDateAndTimeId(date, timeId)){
            throw new IllegalArgumentException("[ERROR] 동일한 시기에 예약을 할 수 없습니다.");
        }

        Reservation nonIdReservation = Reservation.createNew(name, date, reservationTime);
        return reservationRepository.save(nonIdReservation);
    }

    public void deleteById(final long id) {
        reservationRepository.deleteById(id);
    }
}
