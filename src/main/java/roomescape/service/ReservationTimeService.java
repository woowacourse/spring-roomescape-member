package roomescape.service;

import static roomescape.exception.ExceptionType.DELETE_USED_TIME;
import static roomescape.exception.ExceptionType.DUPLICATE_RESERVATION_TIME;
import static roomescape.exception.ExceptionType.NOT_FOUND_THEME;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.ReservationTime;
import roomescape.domain.Reservations;
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
                                  ReservationTimeRepository reservationTimeRepository,
                                  ThemeRepository themeRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.themeRepository = themeRepository;
    }

    public ReservationTimeResponse save(ReservationTimeRequest reservationTimeRequest) {
        if (reservationTimeRepository.existsByStartAt(reservationTimeRequest.startAt())) {
            throw new RoomescapeException(DUPLICATE_RESERVATION_TIME);
        }
        ReservationTime beforeSavedReservationTime = new ReservationTime(reservationTimeRequest.startAt());
        ReservationTime savedReservationTime = reservationTimeRepository.save(beforeSavedReservationTime);
        return toResponse(savedReservationTime);
    }

    private ReservationTimeResponse toResponse(ReservationTime saved) {
        return new ReservationTimeResponse(saved.getId(), saved.getStartAt());
    }

    public List<ReservationTimeResponse> findAll() {
        return reservationTimeRepository.findAll().getReservationTimes().stream()
                .map(this::toResponse)
                .toList();
    }

    //todo : 메서드 개선
    public List<AvailableTimeResponse> findByThemeAndDate(LocalDate date, long themeId) {
        Theme requestedTheme = themeRepository.findById(themeId)
                .orElseThrow(() -> new RoomescapeException(NOT_FOUND_THEME));
        Reservations findReservations = reservationRepository.findByThemeAndDate(requestedTheme, date);

        return reservationTimeRepository.findAll().getReservationTimes().stream()
                .map(reservationTime ->
                        new AvailableTimeResponse(
                                reservationTime.getId(),
                                reservationTime.getStartAt(),
                                findReservations.hasReservationTimeOf(reservationTime.getId())
                        )
                )
                .toList();
    }

    public void delete(long timeId) {
        if (isUsedTime(timeId)) {
            throw new RoomescapeException(DELETE_USED_TIME);
        }
        reservationTimeRepository.delete(timeId);
    }

    //todo SQL로 구현
    private boolean isUsedTime(long timeId) {
        return reservationRepository.findAll().hasReservationTimeOf(timeId);
    }
}
