package roomescape.domain.reservation;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import roomescape.domain.reservation.dto.CreateReservationRequest;
import roomescape.domain.reservation.dto.CreateReservationResponse;
import roomescape.domain.reservation.dto.ReservationResponse;
import roomescape.domain.reservationdate.ReservationDate;
import roomescape.domain.reservationdate.ReservationDateRepository;
import roomescape.domain.reservationtime.ReservationTime;
import roomescape.domain.reservationtime.ReservationTimeRepository;
import roomescape.domain.theme.Theme;
import roomescape.domain.theme.ThemeRepository;
import roomescape.support.exception.BadRequestException;
import roomescape.support.exception.NotFoundException;
import roomescape.support.exception.ReservationDateErrorCode;
import roomescape.support.exception.ReservationErrorCode;
import roomescape.support.exception.ReservationTimeErrorCode;
import roomescape.support.exception.ThemeErrorCode;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ReservationDateRepository reservationDateRepository;
    private final ThemeRepository themeRepository;
    private final Clock clock;

    public CreateReservationResponse createReservation(CreateReservationRequest request) {
        ReservationTime reservationTime = reservationTimeRepository.findById(request.timeId())
            .orElseThrow(() -> new NotFoundException(ReservationTimeErrorCode.RESERVATION_TIME_NOT_EXIST));
        ReservationDate reservationDate = reservationDateRepository.findById(request.dateId())
            .orElseThrow(() -> new NotFoundException(ReservationDateErrorCode.RESERVATION_DATE_NOT_EXIST));
        checkReservationDateAndReservationTime(reservationDate, reservationTime);
        Theme theme = themeRepository.findById(request.themeId())
            .orElseThrow(() -> new NotFoundException(ThemeErrorCode.THEME_NOT_EXIST));
        checkDuplicated(reservationTime, reservationDate, theme);
        Reservation savedReservation = reservationRepository.save(
            request.toEntity(reservationDate, reservationTime, theme));
        return CreateReservationResponse.from(savedReservation);
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

    private void checkReservationDateAndReservationTime(
        ReservationDate reservationDate,
        ReservationTime reservationTime
    ) {
        LocalDate today = LocalDate.now(clock);
        if (reservationDate.isBefore(today)) {
            throw new BadRequestException(
                ReservationDateErrorCode.RESERVATION_DATE_MUST_BE_TODAY_OR_LATER,
                LocalDate.now(clock)
            );
        }
        if (reservationDate.isSame(today) && reservationTime.isBefore(LocalTime.now(clock))) {
            throw new BadRequestException(
                ReservationTimeErrorCode.RESERVATION_TIME_SHOULD_BE_NOW_OR_LATER,
                LocalTime.now(clock)
            );
        }
    }

    private void checkDuplicated(ReservationTime reservationTime, ReservationDate reservationDate, Theme theme) {
        if (reservationRepository.existsReservation(reservationTime.getId(), reservationDate.getId(), theme.getId())) {
            throw new BadRequestException(ReservationErrorCode.DUPLICATED_RESERVATION);
        }
    }
}
