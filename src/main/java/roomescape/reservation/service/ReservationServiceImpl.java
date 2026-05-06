package roomescape.reservation.service;

import java.util.List;

import org.springframework.stereotype.Service;

import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationTime;
import roomescape.reservation.exception.ReservationNotFoundException;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.reservation.service.dto.ReservationSaveServiceDto;
import roomescape.time.service.TimeService;
import roomescape.theme.repository.ThemeRepository;

@Service
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final TimeService timeService;
    private final ThemeRepository themeRepository;

    public ReservationServiceImpl(
            ReservationRepository reservationRepository,
            TimeService timeService,
            ThemeRepository themeRepository
    ) {
        this.reservationRepository = reservationRepository;
        this.timeService = timeService;
        this.themeRepository = themeRepository;
    }

    @Override
    public List<Reservation> getAll() {
        return reservationRepository.findAll();
    }

    @Override
    public Reservation create(ReservationSaveServiceDto reservation) {
        ReservationTime time = findTime(reservation.getTime());
        Long themeId = findThemeId(reservation.getThemeId());
        Reservation newReservation = new Reservation(
                reservation.getName(),
                reservation.getDate(),
                time,
                themeId
        );
        return reservationRepository.save(newReservation);
    }

    private ReservationTime findTime(String startAt) {
        return timeService.findByStartAt(startAt);
    }

    private Long findThemeId(Long themeId) {
        if (themeId == null) {
            throw new IllegalArgumentException("테마는 필수입니다.");
        }
        if (!themeRepository.existsById(themeId)) {
            throw new IllegalArgumentException("테마가 존재하지 않습니다. id=" + themeId);
        }
        return themeId;
    }

    @Override
    public void cancel(Long id) {
        if (!reservationRepository.deleteById(id)) {
            throw new ReservationNotFoundException(id);
        }
    }
}