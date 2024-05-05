package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.dao.ReservationRepository;
import roomescape.dao.ReservationTimeRepository;
import roomescape.dao.ThemeRepository;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.exception.InvalidReservationException;
import roomescape.service.dto.ReservationRequest;
import roomescape.service.dto.ReservationResponse;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;

    public ReservationService(final ReservationRepository reservationRepository,
                              final ReservationTimeRepository reservationTimeRepository,
                              ThemeRepository themeRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.themeRepository = themeRepository;
    }

    public ReservationResponse create(final ReservationRequest reservationRequest) {
        validateDuplicated(reservationRequest);
        ReservationTime reservationTime = findTimeById(reservationRequest.timeId());
        Theme theme = findThemeById(reservationRequest.themeId());
        Reservation reservation = reservationRequest.toReservation(reservationTime, theme);
        Reservation newReservation = reservationRepository.save(reservation);
        return new ReservationResponse(newReservation);
    }

    private void validateDuplicated(ReservationRequest reservationRequest) {
        if (reservationRepository.existsByDateAndTimeAndTheme(reservationRequest.date(), reservationRequest.timeId(),
                reservationRequest.themeId())) {
            throw new InvalidReservationException("선택하신 테마와 일정은 이미 예약이 존재합니다.");
        }
    }

    private ReservationTime findTimeById(final long timeId) {
        return reservationTimeRepository.findById(timeId)
                .orElseThrow(() -> new InvalidReservationException("더이상 존재하지 않는 시간입니다."));
    }

    private Theme findThemeById(long themeId) {
        return themeRepository.findById(themeId).
                orElseThrow(() -> new InvalidReservationException("더이상 존재하지 않는 테마입니다."));
    }

    public List<ReservationResponse> findAll() {
        return reservationRepository.findAll().stream()
                .map(ReservationResponse::new)
                .toList();
    }

    public void deleteById(final long id) {
        reservationRepository.deleteById(id);
    }
}
