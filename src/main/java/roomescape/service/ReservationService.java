package roomescape.service;

import static roomescape.exception.ExceptionType.DUPLICATE_RESERVATION;
import static roomescape.exception.ExceptionType.NOT_FOUND_RESERVATION_TIME;
import static roomescape.exception.ExceptionType.NOT_FOUND_THEME;
import static roomescape.exception.ExceptionType.PAST_TIME_RESERVATION;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Reservations;
import roomescape.domain.Theme;
import roomescape.dto.ReservationRequest;
import roomescape.dto.ReservationResponse;
import roomescape.dto.ReservationTimeResponse;
import roomescape.dto.ThemeResponse;
import roomescape.exception.RoomescapeException;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;

    public ReservationService(ReservationRepository reservationRepository,
                              ReservationTimeRepository reservationTimeRepository, ThemeRepository themeRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.themeRepository = themeRepository;
    }

    public ReservationResponse save(ReservationRequest reservationRequest) {

        ReservationTime requestedTime = reservationTimeRepository.findById(reservationRequest.timeId())
                .orElseThrow(() -> new RoomescapeException(NOT_FOUND_RESERVATION_TIME));
        Theme requestedTheme = themeRepository.findById(reservationRequest.themeId())
                .orElseThrow(() -> new RoomescapeException(NOT_FOUND_THEME));

        Reservation beforeSave = new Reservation(
                reservationRequest.name(),
                reservationRequest.date(),
                requestedTime,
                requestedTheme
        );
        Reservations reservations = reservationRepository.findAll();
        if (reservations.hasSameReservation(beforeSave)) {
            throw new RoomescapeException(DUPLICATE_RESERVATION);
        }
        if (beforeSave.isBefore(LocalDateTime.now())) {
            throw new RoomescapeException(PAST_TIME_RESERVATION);
        }

        return toResponse(reservationRepository.save(beforeSave));
    }

    private ReservationResponse toResponse(Reservation reservation) {
        ReservationTime reservationTime = reservation.getReservationTime();
        ReservationTimeResponse reservationTimeResponse = new ReservationTimeResponse(reservationTime.getId(),
                reservation.getTime());
        Theme theme = reservation.getTheme();
        ThemeResponse themeResponse = new ThemeResponse(theme.getId(), theme.getName(), theme.getDescription(),
                theme.getThumbnail());
        return new ReservationResponse(reservation.getId(),
                reservation.getName(), reservation.getDate(), reservationTimeResponse, themeResponse);
    }

    public List<ReservationResponse> findAll() {
        Reservations reservations = reservationRepository.findAll();
        return reservations.getReservations().stream()
                .map(this::toResponse)
                .toList();
    }

    public void delete(long id) {
        reservationRepository.delete(id);
    }
}
