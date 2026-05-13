package roomescape.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import roomescape.dto.ReservationRequest;
import roomescape.dto.ReservationResponse;
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
        Reservation reservation = reservationRepository.findById(id).orElseThrow(
                () -> new RoomescapeException(ErrorCode.RESERVATION_NOT_FOUND));

        if (isOverDateAndTime(reservation.getDate(), reservation.getTime())) {
            throw new RoomescapeException(ErrorCode.RESERVATION_PAST_CANCEL);
        }
        reservationRepository.deleteById(id);
    }

    public void removeByIdAndName(Long id, String username) {
        Reservation reservation = reservationRepository.findById(id).orElseThrow(
                () -> new RoomescapeException(ErrorCode.RESERVATION_NOT_FOUND)
        );
        if (!reservation.getName().equals(username)) {
            throw new RoomescapeException(ErrorCode.RESERVATION_NOT_USER_CANCEL);
        }
        if (isOverDateAndTime(reservation.getDate(), reservation.getTime())) {
            throw new RoomescapeException(ErrorCode.RESERVATION_PAST_CANCEL);
        }
        reservationRepository.deleteById(id);
    }

    public ReservationResponse register(ReservationRequest reservationRequest) {
        ReservationTime reservationTime = timeRepository.findById(reservationRequest.timeId())
                .orElseThrow(() -> new RoomescapeException(ErrorCode.TIME_NOT_FOUND));
        Theme theme = themeRepository.findById(reservationRequest.themeId())
                .orElseThrow(() -> new RoomescapeException(ErrorCode.THEME_NOT_FOUND));

        if (isOverDateAndTime(reservationRequest.date(), reservationTime)) {
            throw new RoomescapeException(ErrorCode.RESERVATION_PAST_DATE);
        }

        if (reservationRepository.existsByDateAndTimeIdAndThemeId(reservationRequest.date(),
                reservationRequest.timeId(),
                reservationRequest.themeId())) {
            throw new RoomescapeException(ErrorCode.RESERVATION_DUPLICATED);
        }

        Reservation reservation = reservationRepository.save(reservationRequest.name(), reservationRequest.date(),
                reservationRequest.timeId(),
                reservationRequest.themeId(), reservationTime, theme);
        return ReservationResponse.from(reservation);
    }

    private boolean isOverDateAndTime(LocalDate date, ReservationTime time) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime reservation = LocalDateTime.of(date, time.getStartAt());

        return now.isAfter(reservation);
    }
}
