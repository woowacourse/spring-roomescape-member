package roomescape.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.dto.ReservationRequest;
import roomescape.dto.ReservationResponse;
import roomescape.repository.JdbcReservationRepository;
import roomescape.repository.JdbcReservationTimeRepository;
import roomescape.repository.JdbcThemeRepository;

@Service
public class ReservationService {

    private final JdbcReservationRepository reservationRepository;
    private final JdbcReservationTimeRepository reservationTimeRepository;
    private final JdbcThemeRepository themeRepository;

    public ReservationService(JdbcReservationRepository reservationRepository,
                              JdbcReservationTimeRepository reservationTimeRepository,
                              JdbcThemeRepository themeRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.themeRepository = themeRepository;
    }

    public List<ReservationResponse> getAllReservations() {
        return reservationRepository.findAll()
                .stream()
                .map(ReservationResponse::from)
                .toList();
    }

    public ReservationResponse addReservation(ReservationRequest request) {
        ReservationTime reservationTime = reservationTimeRepository.findById(request.timeId());
        validateNotDuplicatedTime(request.date(), request.timeId(), request.themeId());
        Theme theme = themeRepository.findById(request.themeId());

        Reservation reservation = new Reservation(request.name(), request.date(), reservationTime,
                theme, LocalDateTime.now());
        Reservation savedReservation = reservationRepository.save(reservation);

        return ReservationResponse.from(savedReservation);
    }

    private void validateNotDuplicatedTime(LocalDate date, Long timeId, Long themeId) {
        if (reservationRepository.existByDateAndTimeIdAndThemeId(date, timeId, themeId)) {
            throw new IllegalArgumentException("중복 예약은 불가능하다.");
        }
    }

    public void deleteById(Long id) {
        reservationRepository.deleteById(id);
    }
}
