package roomescape.service;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.dto.ReservationAllResponse;
import roomescape.dto.ReservationPatchRequest;
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
    private final Clock clock;

    public ReservationService(ReservationRepository reservationRepository, TimeRepository timeRepository,
                              ThemeRepository themeRepository, Clock clock) {
        this.reservationRepository = reservationRepository;
        this.timeRepository = timeRepository;
        this.themeRepository = themeRepository;
        this.clock = clock;
    }

    public ReservationAllResponse read() {
        List<Reservation> reservations = reservationRepository.findAll();
        List<ReservationResponse> responses = reservations.stream()
                .map(ReservationResponse::from)
                .toList();
        return new ReservationAllResponse(responses);
    }

    public void removeById(Long id) {
        int deleteCnt = reservationRepository.deleteById(id);
        if (deleteCnt == 0) {
            throw new RoomescapeException(ErrorCode.RESERVATION_NOT_FOUND);
        }
    }

    public ReservationResponse register(ReservationRequest reservationRequest) {
        validateNullFields(reservationRequest);
        ReservationTime time = timeRepository.findById(reservationRequest.timeId())
                .orElseThrow(() -> new RoomescapeException(ErrorCode.RESERVATION_TIMEID_NOT_FOUND));
        reservationRequestDayCheck(reservationRequest.date(), time);
        Theme theme = themeRepository.findById(reservationRequest.themeId())
                .orElseThrow(() -> new RoomescapeException(ErrorCode.RESERVATION_THEMEID_NOT_FOUND));

        if (reservationRepository.existsByDateAndTimeIdAndThemeId(reservationRequest.date(),
                reservationRequest.timeId(),
                reservationRequest.themeId())) {
            throw new RoomescapeException(ErrorCode.RESERVATION_DUPLICATE);
        }

        Reservation reservation = new Reservation(null, reservationRequest.name(), reservationRequest.date(),
                time, theme);
        Reservation saved = reservationRepository.save(reservation);
        return ReservationResponse.from(saved);
    }

    private void reservationRequestDayCheck(LocalDate date, ReservationTime time) {
        LocalDateTime now = LocalDateTime.now(clock);
        if (date.isBefore(now.toLocalDate())) {
            throw new RoomescapeException(ErrorCode.RESERVATION_WRONG_DATE);
        }
        if (date.isEqual(now.toLocalDate()) && time.startAt().getHour() <= now.getHour()) {
            throw new RoomescapeException(ErrorCode.RESERVATION_WRONG_TIME);
        }
    }

    private void validateNullFields(ReservationRequest reservationRequest) {
        if (reservationRequest.name() == null) {
            throw new RoomescapeException(ErrorCode.RESERVATION_BLANK_NAME);
        }
        if (reservationRequest.date() == null) {
            throw new RoomescapeException(ErrorCode.RESERVATION_BLANK_DATE);
        }
        if (reservationRequest.themeId() == null) {
            throw new RoomescapeException(ErrorCode.RESERVATION_BLANK_THEMEID);
        }
        if (reservationRequest.timeId() == null) {
            throw new RoomescapeException(ErrorCode.RESERVATION_BLANK_TIMEID);
        }
    }

    public ReservationAllResponse readByName(String name) {
        List<Reservation> reservations = reservationRepository.findByName(name);
        List<ReservationResponse> responses = reservations.stream()
                .map(ReservationResponse::from)
                .toList();
        return new ReservationAllResponse(responses);
    }

    public ReservationResponse patchById(Long id, ReservationPatchRequest reservationPatchRequest) {
        Reservation reservation = reservationRepository.findById(id);
        ReservationTime time = timeRepository.findById(reservationPatchRequest.timeId())
                .orElseThrow(() -> new RoomescapeException(ErrorCode.RESERVATION_TIMEID_NOT_FOUND));
        reservationRequestDayCheck(reservation.date(), reservation.time());
        reservationRequestDayCheck(reservationPatchRequest.date(), time);

        Theme theme = reservation.theme();
        if (reservationRepository.existsByDateAndTimeIdAndThemeId(reservationPatchRequest.date(),
                reservationPatchRequest.timeId(), theme.id())) {
            throw new RoomescapeException(ErrorCode.RESERVATION_DUPLICATE);
        }

        return ReservationResponse.from(
                reservationRepository.update(reservationPatchRequest.date(), reservationPatchRequest.timeId(), id));
    }
}
