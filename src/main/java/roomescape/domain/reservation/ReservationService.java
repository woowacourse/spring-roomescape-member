package roomescape.domain.reservation;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import roomescape.domain.reservation.dto.ReservationCreationRequest;
import roomescape.domain.reservation.dto.ReservationCreationResponse;
import roomescape.domain.reservation.dto.ReservationResponse;
import roomescape.domain.reservationdate.ReservationDate;
import roomescape.domain.reservationdate.ReservationDateRepository;
import roomescape.domain.reservationtime.ReservationTime;
import roomescape.domain.reservationtime.ReservationTimeRepository;
import roomescape.domain.theme.Theme;
import roomescape.domain.theme.ThemeRepository;
import roomescape.support.exception.ReservationDateErrorCode;
import roomescape.support.exception.ReservationTimeErrorCode;
import roomescape.support.exception.RoomescapeException;
import roomescape.support.exception.ThemeErrorCode;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ReservationDateRepository reservationDateRepository;
    private final ThemeRepository themeRepository;

    public ReservationCreationResponse createReservation(ReservationCreationRequest request) {
        ReservationTime reservationTime = reservationTimeRepository.findById(request.timeId())
            .orElseThrow(() -> new RoomescapeException(ReservationTimeErrorCode.RESERVATION_TIME_NOT_EXIST));
        ReservationDate reservationDate = reservationDateRepository.findById(request.dateId())
            .orElseThrow(() -> new RoomescapeException(ReservationDateErrorCode.RESERVATION_DATE_NOT_EXIST));
        Theme theme = themeRepository.findById(request.themeId())
            .orElseThrow(() -> new RoomescapeException(ThemeErrorCode.THEME_NOT_EXIST));
        Reservation savedReservation = reservationRepository.save(request.toEntity(reservationDate, reservationTime, theme));
        return ReservationCreationResponse.from(savedReservation);
    }

    public List<ReservationResponse> getAllReservations() {
        return reservationRepository.findAll().stream()
            .map(ReservationResponse::from)
            .toList();
    }

    public void deleteReservation(Long id) {
        int deletedCount = reservationRepository.deleteById(id);
        if (deletedCount == 0) {
            log.warn("이미 삭제된 예약 삭제 요청이 들어왔습니다. reservationId={}", id);
        }
    }
}
