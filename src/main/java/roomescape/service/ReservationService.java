package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.dto.ReservationAllResponse;
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
        reservationRequestValidate(reservationRequest);
        ReservationTime time = timeRepository.findById(reservationRequest.timeId())
                .orElseThrow(() -> new RoomescapeException(ErrorCode.RESERVATION_TIMEID_NOT_FOUND));
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

    private void reservationRequestValidate(ReservationRequest reservationRequest) {
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
}
