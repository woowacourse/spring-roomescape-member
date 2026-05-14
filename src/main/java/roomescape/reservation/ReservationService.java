package roomescape.reservation;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.exception.ErrorCode;
import roomescape.exception.RoomescapeException;
import roomescape.reservation.dto.ReservationRequest;
import roomescape.reservation.dto.ReservationResponse;
import roomescape.reservation.dto.ReservationUpdateRequest;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.theme.Theme;
import roomescape.theme.repository.ThemeRepository;
import roomescape.time.ReservationTime;
import roomescape.time.repository.ReservationTimeRepository;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;
    private final Clock clock;

    public ReservationService(ReservationRepository reservationRepository,
                              ReservationTimeRepository reservationTimeRepository, ThemeRepository themeRepository,
                              Clock clock) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.themeRepository = themeRepository;
        this.clock = clock;
    }

    @Transactional
    public ReservationResponse create(ReservationRequest reservationRequest) {
        ReservationTime reservationTime = getReservationTime(reservationRequest.timeId());
        Theme theme = getTheme(reservationRequest.themeId());

        Reservation reservation = new Reservation(
                reservationRequest.userName(),
                theme,
                reservationRequest.date(),
                reservationTime,
                LocalDateTime.now(clock)
        );
        if (reservationRepository.existsActiveByDateAndThemeAndTime(reservation.getDate(), theme.getId(),
                reservationTime.getId())) {
            throw new RoomescapeException(ErrorCode.RESERVATION_DUPLICATE);
        }
        return getReservationResponse(reservation);
    }

    public List<ReservationResponse> read() {
        return reservationRepository.findAll().stream()
                .map(ReservationResponse::from)
                .toList();
    }

    @Transactional
    public ReservationResponse update(Long id, ReservationUpdateRequest reservationUpdateRequest) {
        Reservation reservation = getReservation(id);
        reservation.checkOwner(reservationUpdateRequest.userName());

        ReservationTime reservationTime = getReservationTime(reservationUpdateRequest.timeId());
        Theme theme = getTheme(reservationUpdateRequest.themeId());

        if (reservationRepository.existsActiveByDateAndThemeAndTimeExcludingId(reservationUpdateRequest.date(),
                theme.getId(),
                reservationTime.getId(), id)) {
            throw new RoomescapeException(ErrorCode.RESERVATION_DUPLICATE);
        }

        return getReservationResponse(reservationUpdateRequest, reservation, theme, reservationTime);
    }

    public void delete(Long id) {
        Reservation reservation = getReservation(id);
        reservation.cancel(LocalDateTime.now(clock));
        reservationRepository.update(reservation);
    }

    public List<ReservationResponse> readByUserName(String userName) {
        return reservationRepository.findByUserName(userName).stream()
                .map(ReservationResponse::from)
                .toList();
    }

    private ReservationResponse getReservationResponse(Reservation reservation) {
        try {
            Reservation saved = reservationRepository.save(reservation);
            return ReservationResponse.from(saved);
        } catch (DataIntegrityViolationException e) {
            throw new RoomescapeException(ErrorCode.RESERVATION_DUPLICATE);
        }
    }

    private ReservationResponse getReservationResponse(ReservationUpdateRequest reservationUpdateRequest,
                                                       Reservation reservation, Theme theme,
                                                       ReservationTime reservationTime) {
        try {
            Reservation updated = reservation.change(theme, reservationUpdateRequest.date(), reservationTime,
                    LocalDateTime.now(clock));
            reservationRepository.update(updated);
            return ReservationResponse.from(updated);
        } catch (DataIntegrityViolationException e) {
            throw new RoomescapeException(ErrorCode.RESERVATION_DUPLICATE);
        }
    }

    private Reservation getReservation(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new RoomescapeException(ErrorCode.RESERVATION_NOT_FOUND));
    }

    private Theme getTheme(Long reservationRequest) {
        return themeRepository.findById(reservationRequest).stream().findFirst()
                .orElseThrow(() -> new RoomescapeException(ErrorCode.THEME_NOT_FOUND));
    }

    private ReservationTime getReservationTime(Long reservationRequest) {
        return reservationTimeRepository.findById(reservationRequest)
                .orElseThrow(() -> new RoomescapeException(ErrorCode.RESERVATION_TIME_NOT_FOUND));
    }
}
