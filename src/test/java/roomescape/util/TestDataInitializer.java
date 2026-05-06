package roomescape.util;

import java.time.LocalDate;
import java.time.LocalTime;
import org.springframework.stereotype.Component;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.reservation.repository.dto.CreateReservationParams;
import roomescape.theme.domain.Theme;
import roomescape.theme.repository.ThemeRepository;
import roomescape.theme.repository.dto.CreateThemeParams;
import roomescape.time.domain.ReservationTime;
import roomescape.time.repository.ReservationTimeRepository;
import roomescape.time.repository.dto.CreateReservationTimeParams;

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
        CreateReservationTimeParams createReservationTimeParams = new CreateReservationTimeParams(localTime.withSecond(0).withNano(0));
        return reservationTimeRepository.save(createReservationTimeParams);
    }

    public Theme initializeTheme(String name, String description, String imageUrl) {
        CreateThemeParams createThemeParams = new CreateThemeParams(name, description, imageUrl);
        return themeRepository.save(createThemeParams);
    }

    public void initializeReservation(String name, LocalDate date, Long timeId, Long themeId) {
        CreateReservationParams createReservationParams = new CreateReservationParams(name, date, timeId, themeId);
        reservationRepository.save(createReservationParams);
    }
}
