package roomescape.util;

import java.time.LocalDate;
import java.time.LocalTime;
import org.springframework.stereotype.Component;
import roomescape.domain.Theme;
import roomescape.repository.ThemeRepository;
import roomescape.domain.ReservationTime;
import roomescape.repository.ReservationTimeRepository;
import roomescape.domain.Reservation;
import roomescape.repository.ReservationRepository;

@Component
public class TestDataInitializer {

    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;
    private final ReservationRepository reservationRepository;

    public TestDataInitializer(ReservationTimeRepository reservationTimeRepository, ThemeRepository themeRepository,
                               ReservationRepository reservationRepository) {
        this.reservationTimeRepository = reservationTimeRepository;
        this.themeRepository = themeRepository;
        this.reservationRepository = reservationRepository;
    }

    public ReservationTime initializeReservationTime(LocalTime localTime) {
        return reservationTimeRepository.save(new ReservationTime(localTime.withSecond(0).withNano(0)));
    }

    public Theme initializeTheme(String name, String description, String imageUrl) {
        return themeRepository.save(new Theme(null, name, description, imageUrl));
    }

    public void initializeReservation(String name, LocalDate date, Long timeId, Long themeId) {
        ReservationTime reservationTime = reservationTimeRepository.findById(timeId);
        Theme theme = themeRepository.findById(themeId);
        reservationRepository.save(new Reservation(null, name, date, reservationTime, theme));
    }
}
