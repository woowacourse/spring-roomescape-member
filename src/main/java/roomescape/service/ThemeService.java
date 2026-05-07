package roomescape.service;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;
import roomescape.service.dto.TimeAvailabilityDto;

@Service
@Transactional(readOnly = true)
public class ThemeService {

    private final ThemeRepository themeRepository;
    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;

    public ThemeService(ThemeRepository themeRepository, ReservationRepository reservationRepository,
                        ReservationTimeRepository reservationTimeRepository) {
        this.themeRepository = themeRepository;
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
    }

    public List<Theme> findAll() {
        return themeRepository.findAll();
    }

    @Transactional
    public Theme create(String name, String description, String thumbnail) {
        Theme theme = new Theme(null, name, description, thumbnail);
        Long id = themeRepository.insert(theme);
        return themeRepository.findBy(id)
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] 존재하지 않는 테마입니다."));
    }

    @Transactional
    public void delete(Long id) {
        if (!themeRepository.existsById(id)) {
            throw new NoSuchElementException("[ERROR] 존재하지 않는 ID입니다.");
        }
        if (reservationRepository.existsByThemeId(id)) {
            throw new IllegalArgumentException("[ERROR] 해당 테마의 예약이 존재합니다.");
        }
        themeRepository.delete(id);
    }

    public List<Theme> findWeeklyTopTen() {
        LocalDate startDate = LocalDate.now().minusWeeks(1);
        LocalDate endDate = startDate.plusDays(6);
        return themeRepository.findPopular(startDate, endDate, 10);
    }

    public List<TimeAvailabilityDto> findAvailableTime(Long themeId, LocalDate date) {
        List<ReservationTime> times = reservationTimeRepository.findAll();
        List<Reservation> reservations = reservationRepository.findReservationsByThemeAndDate(themeId, date);

        return times.stream()
                .map(time -> new TimeAvailabilityDto(
                        time,
                        isAvailable(time, reservations)
                ))
                .toList();
    }

    private boolean isAvailable(ReservationTime time, List<Reservation> reservations) {
        return reservations.stream()
                .noneMatch(reservation -> time.equals(reservation.getTime()));
    }
}
