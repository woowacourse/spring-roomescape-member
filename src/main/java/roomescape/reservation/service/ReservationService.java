package roomescape.reservation.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.dto.ReservationRequest;
import roomescape.reservation.dto.ReservationResponse;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.time.domain.Time;
import roomescape.theme.repository.ThemeRepository;
import roomescape.theme.domain.Theme;
import roomescape.time.repository.TimeRepository;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final TimeRepository timeRepository;
    private final ThemeRepository themeRepository;

    public ReservationService(ReservationRepository reservationRepository,
                              TimeRepository timeRepository,
                              ThemeRepository themeRepository) {

        this.reservationRepository = reservationRepository;
        this.timeRepository = timeRepository;
        this.themeRepository = themeRepository;
    }

    public ReservationResponse add(ReservationRequest request) {
        Time time = findReservationTimeOrThrow(request.timeId());
        Theme theme = findThemeOrThrow(request.themeId());

        LocalDate date = request.date();
        validateReservationTimeNotInPast(date, time);
        validateNoDuplicateReservation(date, request.timeId(), request.themeId());

        Reservation reservation = new Reservation(null, request.name(), date, time, theme);

        return ReservationResponse.from(reservationRepository.add(reservation));
    }

    public List<ReservationResponse> findAll() {
        return reservationRepository.findAll().stream()
                .map(ReservationResponse::from)
                .toList();
    }

    public void deleteById(Long id) {
        ensureReservationExists(id);
        reservationRepository.deleteById(id);
    }

    private void validateReservationTimeNotInPast(LocalDate date, Time time) {
        if(date.isEqual(LocalDate.now()) && time.isBefore(LocalTime.now())) {
            throw new IllegalArgumentException("[ERROR] 지난 시간으로는 예약할 수 없습니다.");
        }
    }

    private void validateNoDuplicateReservation(LocalDate date, Long timeId, Long themeId) {
        if(reservationRepository.existsByDateAndTimeIdAndThemeId(date, timeId, themeId)) {
            throw new IllegalArgumentException("[ERROR] 이미 예약이 존재합니다.");
        }
    }

    private void ensureReservationExists(Long id) {
        reservationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] 해당 id의 예약이 존재하지 않습니다"));
    }

    private Time findReservationTimeOrThrow(Long timeId) {
        return timeRepository.findById(timeId)
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] 시간 id가 존재하지 않습니다."));
    }

    private Theme findThemeOrThrow(Long themeId) {
        return themeRepository.findById(themeId)
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] 테마 id가 존재하지 않습니다."));
    }
}
