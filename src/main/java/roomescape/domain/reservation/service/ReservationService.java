package roomescape.domain.reservation.service;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.reservation.entity.Reservation;
import roomescape.domain.reservation.exception.DuplicateReservationException;
import roomescape.domain.reservation.exception.PastReservationException;
import roomescape.domain.reservation.repository.ReservationRepository;
import roomescape.domain.reservation.request.ReservationCreateRequest;
import roomescape.domain.reservation.response.ReservationResponse;
import roomescape.domain.reservation.response.ReservationsResponse;
import roomescape.domain.reservationtime.entity.ReservationTime;
import roomescape.domain.reservationtime.repository.ReservationTimeRepository;
import roomescape.domain.theme.entity.Theme;
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
                .orElseThrow(() -> new IllegalArgumentException(
                        "해당 id의 ReservationTime이 존재하지 않습니다. timeId=" + request.timeId()));

        Theme theme = themeRepository.findById(request.themeId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "해당 id에 해당하는 theme가 존재하지 않습니다. themeId=" + request.themeId()));

        if (request.date().isBefore(LocalDate.now(clock))) {
            throw new PastReservationException();
        }

        if (request.date().isEqual(LocalDate.now(clock)) && time.getStartAt().isBefore(LocalTime.now(clock))) {
            throw new PastReservationException();
        }

        if (reservationRepository.existsByThemeIdAndDateAndTimeId(request.themeId(), request.date(),
                request.timeId())) {
            throw new DuplicateReservationException();
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
                .orElseThrow(() -> new IllegalArgumentException(
                        "해당 id의 ReservationTime이 존재하지 않습니다. timeId=" + request.timeId()));

        Theme theme = themeRepository.findById(request.themeId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "해당 id에 해당하는 theme가 존재하지 않습니다. themeId=" + request.themeId()));

        if (reservationRepository.existsByThemeIdAndDateAndTimeId(request.themeId(), request.date(),
                request.timeId())) {
            throw new DuplicateReservationException();
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
