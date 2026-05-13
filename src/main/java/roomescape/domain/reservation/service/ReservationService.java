package roomescape.domain.reservation.service;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.common.exception.BusinessException;
import roomescape.domain.reservation.entity.Reservation;
import roomescape.domain.reservation.exception.ReservationErrorCode;
import roomescape.domain.reservation.repository.ReservationRepository;
import roomescape.domain.reservation.request.ReservationCreateRequest;
import roomescape.domain.reservation.response.ReservationResponse;
import roomescape.domain.reservation.response.ReservationsResponse;
import roomescape.domain.reservationtime.entity.ReservationTime;
import roomescape.domain.reservationtime.exception.TimeErrorCode;
import roomescape.domain.reservationtime.repository.ReservationTimeRepository;
import roomescape.domain.theme.entity.Theme;
import roomescape.domain.theme.exception.ThemeErrorCode;
import roomescape.domain.theme.repository.ThemeRepository;

@Service
@Transactional(readOnly = true)
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;
    private final Clock clock;

    @Autowired
    public ReservationService(
            ReservationRepository reservationRepository,
            ReservationTimeRepository reservationTimeRepository,
            ThemeRepository themeRepository,
            Clock clock
    ) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.themeRepository = themeRepository;
        this.clock = clock;
    }

    public ReservationsResponse findAllReservations() {
        List<ReservationResponse> reservations = reservationRepository.findAll().stream()
                .map(ReservationResponse::from)
                .toList();

        return new ReservationsResponse(reservations);
    }

    @Transactional
    public ReservationResponse saveReservationByUser(ReservationCreateRequest request) {
        ReservationTime time = reservationTimeRepository.findById(request.timeId())
                .orElseThrow(() -> new BusinessException(TimeErrorCode.RESERVATION_TIME_NOT_FOUND));

        Theme theme = themeRepository.findById(request.themeId())
                .orElseThrow(() -> new BusinessException(ThemeErrorCode.THEME_NOT_FOUND));

        if (request.date().isBefore(LocalDate.now(clock))) {
            throw new BusinessException(ReservationErrorCode.PAST_RESERVATION);
        }

        if (request.date().isEqual(LocalDate.now(clock)) && time.getStartAt().isBefore(LocalTime.now(clock))) {
            throw new BusinessException(ReservationErrorCode.PAST_RESERVATION);
        }

        if (reservationRepository.existsByThemeIdAndDateAndTimeId(
                request.themeId(),
                request.date(),
                request.timeId())
        ) {
            throw new BusinessException(ReservationErrorCode.DUPLICATE_RESERVATION);
        }

        Reservation reservation = Reservation.create(
                request.username(),
                theme,
                request.date(),
                time
        );

        Reservation savedReservation = reservationRepository.save(reservation);

        return ReservationResponse.from(savedReservation);
    }

    @Transactional
    public ReservationResponse saveReservationByAdmin(ReservationCreateRequest request) {
        ReservationTime time = reservationTimeRepository.findById(request.timeId())
                .orElseThrow(() -> new BusinessException(TimeErrorCode.RESERVATION_TIME_NOT_FOUND));

        Theme theme = themeRepository.findById(request.themeId())
                .orElseThrow(() -> new BusinessException(ThemeErrorCode.THEME_NOT_FOUND));

        if (reservationRepository.existsByThemeIdAndDateAndTimeId(
                request.themeId(),
                request.date(),
                request.timeId())
        ) {
            throw new BusinessException(ReservationErrorCode.DUPLICATE_RESERVATION);
        }

        Reservation reservation = Reservation.create(
                request.username(),
                theme,
                request.date(),
                time
        );

        Reservation savedReservation = reservationRepository.save(reservation);

        return ReservationResponse.from(savedReservation);
    }

    @Transactional
    public void deleteReservationBy(Long id) {
        reservationRepository.deleteById(id);
    }
}
