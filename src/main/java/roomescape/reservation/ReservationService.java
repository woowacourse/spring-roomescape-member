package roomescape.reservation;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
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
        ReservationTime reservationTime = reservationTimeRepository.findById(reservationRequest.timeId())
                .orElseThrow(() -> new RoomescapeException(ErrorCode.RESERVATION_TIME_NOT_FOUND));

        Theme theme = themeRepository.findById(reservationRequest.themeId()).stream().findFirst()
                .orElseThrow(() -> new RoomescapeException(ErrorCode.THEME_NOT_FOUND));

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
        Reservation saved = reservationRepository.save(reservation);
        return ReservationResponse.from(saved);
    }

    public List<ReservationResponse> read() {
        return reservationRepository.findAll().stream()
                .map(ReservationResponse::from)
                .toList();
    }

    public List<ReservationResponse> readByUserName(String userName) {
        return reservationRepository.findByUserName(userName).stream()
                .map(ReservationResponse::from)
                .toList();
    }

    public void delete(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RoomescapeException(ErrorCode.RESERVATION_NOT_FOUND));
        reservation.cancel(LocalDateTime.now(clock));
        reservationRepository.update(reservation);
    }

    @Transactional
    public ReservationResponse update(Long id, ReservationUpdateRequest reservationUpdateRequest) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RoomescapeException(ErrorCode.RESERVATION_NOT_FOUND));

        if (!reservation.belongsTo(reservationUpdateRequest.userName())) {
            throw new RoomescapeException(ErrorCode.RESERVATION_NOT_OWNER);
        }

        ReservationTime reservationTime = reservationTimeRepository.findById(reservationUpdateRequest.timeId())
                .orElseThrow(() -> new RoomescapeException(ErrorCode.RESERVATION_TIME_NOT_FOUND));

        Theme theme = themeRepository.findById(reservationUpdateRequest.themeId()).stream().findFirst()
                .orElseThrow(() -> new RoomescapeException(ErrorCode.THEME_NOT_FOUND));

        if (reservationRepository.existsActiveByDateAndThemeAndTimeExcludingId(reservationUpdateRequest.date(),
                theme.getId(),
                reservationTime.getId(), id)) {
            throw new RoomescapeException(ErrorCode.RESERVATION_DUPLICATE);
        }

        Reservation updated = reservation.change(theme, reservationUpdateRequest.date(), reservationTime,
                LocalDateTime.now(clock));
        reservationRepository.update(updated);

        return ReservationResponse.from(updated);
    }
}
