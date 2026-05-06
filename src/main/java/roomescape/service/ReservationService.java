package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.dao.ReservationRepository;
import roomescape.dao.ReservationTimeRepository;
import roomescape.dao.ThemeRepository;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.service.dto.TimeAvailabilityDto;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;

    public ReservationService(ReservationRepository reservationRepository, ReservationTimeRepository reservationTimeRepository, ThemeRepository themeRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.themeRepository = themeRepository;
    }

    public List<Reservation> findAll() {
        return reservationRepository.findAll();
    }

    public Reservation create(String name, LocalDate date, Long timeId, Long themeId) {
        ReservationTime time = findReservationTime(timeId);
        Theme theme = findTheme(themeId);
        Reservation reservation = new Reservation(null, name, date, time, theme);
        Long id = reservationRepository.insert(reservation);
        return reservationRepository.findBy(id)
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] 존재하지 않는 ID입니다."));
    }

    public void delete(Long id) {
        validateId(id);
        reservationRepository.delete(id);
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

    private ReservationTime findReservationTime(Long timeId) {
        return reservationTimeRepository.findBy(timeId)
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] 존재하지 않는 예약 시간입니다."));
    }

    private Theme findTheme(Long themeId) {
        return themeRepository.findBy(themeId)
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] 존재하지 않는 테마입니다."));
    }

    private void validateId(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("[ERROR] id는 양수이어야 합니다.");
        }
    }
}
