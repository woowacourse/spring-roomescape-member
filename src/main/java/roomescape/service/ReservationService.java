package roomescape.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationSlotTimes;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.dto.AddReservationDto;
import roomescape.dto.AvailableTimeRequestDto;
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
    private static final int RANKING_SIZE = 10;

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;

    public ReservationService(ReservationRepository reservationRepository,
                              ReservationTimeRepository reservationTimeRepository, ThemeRepository themeRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.themeRepository = themeRepository;
    }

    public void deleteReservation(Long id) {
        reservationRepository.deleteById(id);
    }

    public long addReservation(AddReservationDto newReservation) {
        ReservationTime reservationTime = reservationTimeRepository.findById(newReservation.timeId())
                .orElseThrow(() -> new InvalidReservationTimeException("존재하지 않는 예약 시간 id입니다."));
        Theme theme = themeRepository.findById(newReservation.themeId())
                .orElseThrow(() -> new InvalidThemeException("존재하지 않는 테마 id입니다."));

        Reservation reservation = newReservation.toReservation(reservationTime, theme);

        validateSameReservation(reservation);
        LocalDateTime currentDateTime = LocalDateTime.of(LocalDate.now(), LocalTime.now());
        validateAddReservationDateTime(reservation, currentDateTime);
        return reservationRepository.add(reservation);
    }

    private void validateSameReservation(Reservation reservation) {
        if (reservationRepository.existsByDateAndTimeIdAndTheme(reservation)) {
            throw new InvalidReservationException("중복된 예약신청입니다");
        }
    }

    private void validateAddReservationDateTime(Reservation newReservation, LocalDateTime currentDateTime) {
        LocalDateTime reservationDateTime = LocalDateTime.of(newReservation.getDate(), newReservation.getStartAt());
        if (reservationDateTime.isBefore(currentDateTime)) {
            throw new InvalidReservationException("과거 시간에 예약할 수 없습니다.");
        }
    }

    public List<Reservation> allReservations() {
        return reservationRepository.findAll();
    }

    public ReservationSlotTimes availableReservationTimes(AvailableTimeRequestDto availableTimeRequestDto) {
        List<ReservationTime> times = reservationTimeRepository.findAll();

        List<Reservation> alreadyReservedReservations = reservationRepository.findAllByDateAndThemeId(
                availableTimeRequestDto.date(), availableTimeRequestDto.themeId());

        return new ReservationSlotTimes(times, alreadyReservedReservations);
    }

    public Reservation getReservationById(long addedReservationId) {
        return reservationRepository.findById(addedReservationId)
                .orElseThrow(() -> new InvalidReservationException("존재하지 않는 예약입니다."));
    }

    public List<Theme> getRankingThemes(LocalDate originDate) {
        LocalDate end = originDate.minusDays(THEME_RANKING_START_RANGE);
        LocalDate start = end.minusDays(THEME_RANKING_END_RANGE);
        List<Reservation> inRangeReservations = reservationRepository.findAllByDateInRange(start, end);

        Map<Theme, Integer> themeRankCount = new HashMap<>();
        for (Reservation inRangeReservation : inRangeReservations) {
            Theme theme = inRangeReservation.getTheme();
            themeRankCount.put(theme, themeRankCount.getOrDefault(theme, 0) + 1);
        }

        return themeRankCount.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .map(Map.Entry::getKey)
                .limit(RANKING_SIZE)
                .toList();
    }
}
