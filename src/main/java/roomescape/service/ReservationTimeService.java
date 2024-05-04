package roomescape.service;

import static roomescape.exception.ExceptionType.DELETE_USED_TIME;
import static roomescape.exception.ExceptionType.DUPLICATE_RESERVATION_TIME;
import static roomescape.exception.ExceptionType.NOT_FOUND_THEME;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.dto.AvailableTimeResponse;
import roomescape.dto.ReservationTimeRequest;
import roomescape.dto.ReservationTimeResponse;
import roomescape.exception.RoomescapeException;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;

@Service
public class ReservationTimeService {
    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;

    public ReservationTimeService(ReservationRepository reservationRepository,
                                  ReservationTimeRepository reservationTimeRepository, ThemeRepository themeRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.themeRepository = themeRepository;
    }

    public ReservationTimeResponse save(ReservationTimeRequest reservationTimeRequest) {
        if (reservationTimeRepository.existsByStartAt(reservationTimeRequest.startAt())) {
            throw new RoomescapeException(DUPLICATE_RESERVATION_TIME);
        }
        ReservationTime reservationTime = new ReservationTime(reservationTimeRequest.startAt());
        ReservationTime saved = reservationTimeRepository.save(reservationTime);
        return toResponse(saved);
    }

    private ReservationTimeResponse toResponse(ReservationTime saved) {
        return new ReservationTimeResponse(saved.getId(), saved.getStartAt());
    }

    public List<ReservationTimeResponse> findAll() {
        return reservationTimeRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    //todo : 메서드 개선
    public List<AvailableTimeResponse> findByThemeAndDate(LocalDate date, long themeId) {
        Theme theme = themeRepository.findById(themeId)
                .orElseThrow(() -> new RoomescapeException(NOT_FOUND_THEME));
        List<Reservation> findReservations = reservationRepository.findByThemeAndDate(theme, date);

        return reservationTimeRepository.findAll().stream()
                .map(reservationTime -> new AvailableTimeResponse(
                        reservationTime.getId(),
                        reservationTime.getStartAt(),
                        findReservations.stream().anyMatch(
                                reservation -> reservation.isReservationTimeOf(reservationTime.getId())
                        )
                ))
                .toList();
    }

    public void delete(long id) {
        //todo SQL로 구현
        List<Reservation> reservations = reservationRepository.findAll();
        if (isUsedTime(id, reservations)) {
            throw new RoomescapeException(DELETE_USED_TIME);
        }
        reservationTimeRepository.delete(id);
    }

    private static boolean isUsedTime(long id, List<Reservation> reservations) {
        return reservations.stream()
                .anyMatch(reservation -> reservation.isReservationTimeOf(id));
    }
}
