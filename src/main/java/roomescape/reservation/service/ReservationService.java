package roomescape.reservation.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.reservation.service.dto.ReservationCommand;
import roomescape.reservation.service.dto.ReservationResult;
import roomescape.theme.service.ThemeService;
import roomescape.time.service.ReservationTimeService;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeService reservationTimeService;
    private final ThemeService themeService;

    public ReservationService(
            ReservationRepository reservationRepository,
            ReservationTimeService reservationTimeService,
            ThemeService themeService
    ) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeService = reservationTimeService;
        this.themeService = themeService;
    }

    @Transactional
    public ReservationResult save(ReservationCommand command) {
        Reservation reservation = Reservation.create(
                command.name(),
                command.date(),
                reservationTimeService.getById(command.timeId()),
                themeService.getById(command.themeId())
        );

        checkDuplicateReservation(
                reservation.getDate(),
                command.timeId(),
                command.themeId()
        );

        return ReservationResult.from(reservationRepository.save(reservation));
    }

    @Transactional
    public void deleteById(Long id) {
        reservationRepository.deleteById(id);
    }

    public List<ReservationResult> findAll() {
        return reservationRepository.findAll()
                .stream()
                .map(ReservationResult::from)
                .toList();
    }

    private void checkDuplicateReservation(LocalDate date, Long timeId, Long themeId) {
        if (reservationRepository.existsByDateAndTimeIdAndThemeId(date, timeId, themeId)) {
            throw new IllegalStateException("이미 해당 날짜와 시간에 예약이 존재합니다.");
        }
    }
}
