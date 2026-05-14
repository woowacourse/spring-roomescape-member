package roomescape.domain.reservation.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.reservation.entity.Reservation;
import roomescape.domain.reservation.exception.PastReservationDateException;
import roomescape.domain.reservation.repository.ReservationRepository;
import roomescape.domain.reservation.request.ReservationCreateRequest;
import roomescape.domain.reservation.response.ReservationResponse;
import roomescape.domain.theme.entity.Theme;
import roomescape.domain.theme.repository.ThemeRepository;
import roomescape.domain.time.entity.ReservationTime;
import roomescape.domain.time.repository.ReservationTimeRepository;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;
    private final Clock clock;

    public ReservationService(
            ReservationRepository reservationRepository,
            ReservationTimeRepository reservationTimeRepository,
            ThemeRepository themeRepository, Clock clock
    ) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.themeRepository = themeRepository;
        this.clock = clock;
    }

    public List<ReservationResponse> findAllReservations() {
        return reservationRepository.findAll().stream()
                .map(ReservationResponse::from)
                .toList();
    }

    @Transactional
    public ReservationResponse saveReservation(ReservationCreateRequest request) {
        ReservationTime time = reservationTimeRepository.findById(request.timeId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "해당 id의 ReservationTime이 존재하지 않습니다. timeId=" + request.timeId()));

        validateReservationDateTimeIsNotPast(request.date(), time);

        Theme theme = themeRepository.findById(request.themeId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "해당 id에 해당하는 theme가 존재하지 않습니다. themeId=" + request.themeId()));

        Reservation reservation = new Reservation(
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


    private void validateReservationDateTimeIsNotPast(LocalDate date, ReservationTime time) {
        if (date.isBefore(LocalDate.now(clock))) {
           throw new PastReservationDateException();
        }

        if (date.isEqual(LocalDate.now(clock)) && time.isBefore(LocalTime.now(clock))) {
            throw new PastReservationDateException();
        }
    }
}
