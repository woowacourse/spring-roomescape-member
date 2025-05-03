package roomescape.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationSlots;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.domain.ThemeRanking;
import roomescape.dto.request.AddReservationRequest;
import roomescape.dto.request.AvailableTimeRequest;
import roomescape.exception.InvalidReservationException;
import roomescape.exception.InvalidReservationTimeException;
import roomescape.exception.InvalidThemeException;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;

@Service
public class ReservationService {

    private static final int THEME_RANKING_END_RANGE = 7;
    private static final int THEME_RANKING_START_RANGE = 1;

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;

    public ReservationService(ReservationRepository reservationRepository,
                              ReservationTimeRepository reservationTimeRepository,
                              ThemeRepository themeRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.themeRepository = themeRepository;
    }

    public Reservation addReservation(AddReservationRequest newReservation) {
        ReservationTime reservationTime = reservationTimeRepository.findById(newReservation.timeId())
                .orElseThrow(() -> new InvalidReservationTimeException("존재하지 않는 예약 시간 id입니다."));
        Theme theme = themeRepository.findById(newReservation.themeId())
                .orElseThrow(() -> new InvalidThemeException("존재하지 않는 테마 id입니다."));

        Reservation reservation = newReservation.toReservation(reservationTime, theme);

        validateDuplicateReservation(reservation);
        LocalDateTime currentDateTime = LocalDateTime.of(LocalDate.now(), LocalTime.now());
        validateAddReservationDateTime(reservation, currentDateTime);
        return reservationRepository.add(reservation);
    }

    private void validateDuplicateReservation(Reservation reservation) {
        if (reservationRepository.existsByDateAndTimeIdAndTheme(reservation)) {
            throw new InvalidReservationException("중복된 예약신청입니다");
        }
    }

    private void validateAddReservationDateTime(Reservation newReservation, LocalDateTime currentDateTime) {
        if (newReservation.isBefore(currentDateTime)) {
            throw new InvalidReservationException("과거 시간에 예약할 수 없습니다.");
        }
    }

    public List<Reservation> allReservations() {
        return reservationRepository.findAll();
    }

    public Reservation getReservationById(long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new InvalidReservationException("존재하지 않는 예약입니다."));
    }

    public void deleteReservation(Long id) {
        reservationRepository.deleteById(id);
    }

    public ReservationSlots getReservationSlots(AvailableTimeRequest availableTimeRequest) {
        List<ReservationTime> times = reservationTimeRepository.findAll();

        List<Reservation> alreadyReservedReservations = reservationRepository.findAllByDateAndThemeId(
                availableTimeRequest.date(), availableTimeRequest.themeId());

        return new ReservationSlots(times, alreadyReservedReservations);
    }

    public List<Theme> getRankingThemes(LocalDate originDate) {
        LocalDate end = originDate.minusDays(THEME_RANKING_START_RANGE);
        LocalDate start = end.minusDays(THEME_RANKING_END_RANGE);
        List<Reservation> inRangeReservations = reservationRepository.findAllByDateInRange(start, end);

        ThemeRanking themeRanking = new ThemeRanking(inRangeReservations);
        return themeRanking.getAscendingRanking();
    }
}
