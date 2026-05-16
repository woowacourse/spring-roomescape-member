package roomescape.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import roomescape.dto.ReservationRequest;
import roomescape.dto.ReservationResponse;
import roomescape.dto.ReservationUpdateRequest;
import roomescape.exception.ErrorCode;
import roomescape.exception.RoomescapeException;
import roomescape.model.Reservation;
import roomescape.model.ReservationTime;
import roomescape.model.Theme;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ThemeRepository;
import roomescape.repository.TimeRepository;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final TimeRepository timeRepository;
    private final ThemeRepository themeRepository;

    public ReservationService(ReservationRepository reservationRepository, TimeRepository timeRepository,
                              ThemeRepository themeRepository) {
        this.reservationRepository = reservationRepository;
        this.timeRepository = timeRepository;
        this.themeRepository = themeRepository;
    }

    public List<ReservationResponse> read() {
        List<Reservation> reservations = reservationRepository.findAll();
        return reservations.stream()
                .map(ReservationResponse::from)
                .collect(Collectors.toList());
    }

    public List<ReservationResponse> readAllByName(String name) {
        List<Reservation> reservations = reservationRepository.findAllByName(name);
        return reservations.stream()
                .map(ReservationResponse::from)
                .collect(Collectors.toList());
    }

    public void removeById(Long id) {
        Reservation reservation = getReservation(id);
        validatePastUpdate(reservation.getDate(), reservation.getTime());
        reservationRepository.deleteById(id);
    }

    public void cancelByIdAndName(Long id, String username) {
        Reservation reservation = getReservation(id);
        validateOwner(username, reservation);
        validatePastUpdate(reservation.getDate(), reservation.getTime());
        reservationRepository.deleteById(id);
    }

    private void validateOwner(String username, Reservation reservation) {
        if (!reservation.getName().equals(username)) {
            throw new RoomescapeException(ErrorCode.RESERVATION_NOT_OWNER);
        }
    }

    public ReservationResponse register(ReservationRequest reservationRequest) {
        ReservationTime reservationTime = getReservationTime(reservationRequest.timeId());
        Theme theme = getTheme(reservationRequest.themeId());

        validatePastRegister(reservationRequest.date(), reservationTime);
        validateDuplicate(reservationRequest.date(), reservationRequest.timeId(), reservationRequest.themeId());

        Reservation savedReservation = reservationRepository.save(reservationRequest.name(), reservationRequest.date(),
                reservationRequest.timeId(),
                reservationRequest.themeId(), reservationTime, theme);
        return ReservationResponse.from(savedReservation);
    }

    public ReservationResponse update(Long id, String username, ReservationUpdateRequest reservationUpdateRequest) {
        Reservation reservation = getReservation(id);
        validateOwner(username, reservation);
        validatePastUpdate(reservation.getDate(), reservation.getTime());

        ReservationTime reservationTime = getReservationTime(reservationUpdateRequest.timeId());
        validatePastUpdate(reservationUpdateRequest.date(), reservationTime);
        validateDuplicate(reservationUpdateRequest.date(), reservationUpdateRequest.timeId(),
                reservation.getTheme().getId());

        Reservation updated = reservationRepository.update(id, reservationUpdateRequest.date(),
                reservationUpdateRequest.timeId());
        return ReservationResponse.from(updated);
    }

    private Reservation getReservation(Long id) {
        return reservationRepository.findById(id).orElseThrow(
                () -> new RoomescapeException(ErrorCode.RESERVATION_NOT_FOUND)
        );
    }

    private ReservationTime getReservationTime(Long timeId) {
        return timeRepository.findById(timeId)
                .orElseThrow(() -> new RoomescapeException(ErrorCode.TIME_NOT_FOUND));
    }

    private Theme getTheme(Long themeId) {
        return themeRepository.findById(themeId)
                .orElseThrow(() -> new RoomescapeException(ErrorCode.THEME_NOT_FOUND));
    }

    private boolean isOverDateAndTime(LocalDate date, ReservationTime time) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime reservation = LocalDateTime.of(date, time.getStartAt());

        return now.isAfter(reservation);
    }

    private void validatePastUpdate(LocalDate date, ReservationTime time) {
        if (isOverDateAndTime(date, time)) {
            throw new RoomescapeException(ErrorCode.RESERVATION_PAST_UPDATE);
        }
    }

    private void validatePastRegister(LocalDate date, ReservationTime time) {
        if (isOverDateAndTime(date, time)) {
            throw new RoomescapeException(ErrorCode.RESERVATION_PAST_DATE);
        }
    }

    private void validateDuplicate(LocalDate date, Long timeId, Long themeId) {
        if (reservationRepository.existsByDateAndTimeIdAndThemeId(date,
                timeId,
                themeId)) {
            throw new RoomescapeException(ErrorCode.RESERVATION_TIME_ALREADY_BOOKED);
        }
    }

}
