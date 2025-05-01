package roomescape.service;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.dto.ReservationCreationRequest;
import roomescape.exception.BadRequestException;
import roomescape.exception.NotFoundException;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;

    public ReservationService(final ReservationRepository reservationRepository,
            final ReservationTimeRepository reservationTimeRepository,
            final ThemeRepository themeRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.themeRepository = themeRepository;
    }

    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    public Reservation getReservationById(long id) {
        return loadReservationById(id);
    }

    public long saveReservation(ReservationCreationRequest request) {
        Theme theme = loadThemeById(request.getThemeId());
        ReservationTime reservationTime = loadReservationTimeById(request.getTimeId());
        Reservation reservation = Reservation.createWithoutId(
                request.getName(), request.getDate(), reservationTime, theme);

        reservation.validatePastDateTime();
        //TODO: 이미 존재하는 예약인지 체크할때 테마 조건도 추가
        validateAlreadyReserved(reservation);

        return reservationRepository.add(reservation);
    }

    public void deleteReservation(long id) {
        validateReservationById(id);
        reservationRepository.deleteById(id);
    }

    private Reservation loadReservationById(long reservationId) {
        Optional<Reservation> reservation = reservationRepository.findById(reservationId);
        return reservation
                .orElseThrow(() -> new NotFoundException("[ERROR] ID에 해당하는 예약이 존재하지 않습니다."));
    }

    private ReservationTime loadReservationTimeById(long reservationTimeId) {
        Optional<ReservationTime> reservationTime = reservationTimeRepository.findById(reservationTimeId);
        return reservationTime
                .orElseThrow(() -> new NotFoundException("[ERROR] ID에 해당하는 예약시간이 존재하지 않습니다."));
    }

    private Theme loadThemeById(long themeId) {
        Optional<Theme> theme = themeRepository.findById(themeId);
        return theme
                .orElseThrow(() -> new NotFoundException("[ERROR] ID에 해당하는 테마가 존재하지 않습니다."));
    }

    private void validateAlreadyReserved(Reservation reservation) {
        boolean isAlreadyReserved = reservationRepository.checkAlreadyReserved(
                reservation.getDate(), reservation.getTime().getId(), reservation.getTheme().getId());
        if (isAlreadyReserved) {
            throw new BadRequestException("[ERROR] 이미 존재하는 예약입니다.");
        }
    }

    private void validateReservationById(long reservationId) {
        Optional<Reservation> reservation = reservationRepository.findById(reservationId);
        if (reservation.isEmpty()) {
            throw new NotFoundException("[ERROR] ID에 해당하는 예약이 존재하지 않습니다.");
        }
    }
}
