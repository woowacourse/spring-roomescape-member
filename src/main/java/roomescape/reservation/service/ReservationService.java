package roomescape.reservation.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.dto.request.ReservationCreateRequest;
import roomescape.reservation.dto.response.ReservationResponse;
import roomescape.reservation.exception.ReservationAlreadyExistsException;
import roomescape.reservation.exception.ReservationNotFoundException;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.reservationtime.domain.ReservationTime;
import roomescape.reservationtime.repository.ReservationTimeRepository;
import roomescape.theme.domain.Theme;
import roomescape.theme.repository.ThemeRepository;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;

    public ReservationService(final ReservationRepository reservationRepository,
                              final ReservationTimeRepository reservationTimeRepository,
                              final ThemeRepository themeRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.themeRepository = themeRepository;
    }

    public List<ReservationResponse> getReservations() {
        return reservationRepository.getAll().stream()
                .map(reservation -> {
                    ReservationTime time = reservation.getTime();
                    Theme theme = reservation.getTheme();
                    return ReservationResponse.from(reservation, time, theme);
                })
                .toList();
    }

    public void delete(long id) {
        if (!reservationRepository.deleteById(id)) {
            throw new ReservationNotFoundException("요청한 id와 일치하는 예약 정보가 없습니다.");
        }
    }

    public ReservationResponse create(final ReservationCreateRequest request) {
        if (reservationRepository.existsByDateAndTimeId(request.date(), request.timeId())) {
            throw new ReservationAlreadyExistsException("해당 시간에 이미 예약이 존재합니다.");
        }
        ReservationTime time = reservationTimeRepository.findById(request.timeId())
                .orElseThrow(() -> new ReservationNotFoundException("요청한 id와 일치하는 예약 시간 정보가 없습니다."));

        Theme theme = themeRepository.findById(request.themeId())
                .orElseThrow(() -> new ReservationNotFoundException("요청한 id와 일치하는 테마 정보가 없습니다."));

        Reservation newReservation = reservationRepository.put(
                Reservation.withUnassignedId(request.name(), request.date(), time, theme));
        return ReservationResponse.from(newReservation, time, theme);
    }
}
