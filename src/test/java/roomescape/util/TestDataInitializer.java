package roomescape.util;

import java.time.LocalTime;
import org.springframework.stereotype.Component;
import roomescape.theme.repository.ThemeRepository;
import roomescape.theme.repository.dto.CreateThemeParams;
import roomescape.time.domain.ReservationTime;
import roomescape.time.repository.ReservationTimeRepository;
import roomescape.time.repository.dto.CreateReservationTimeParams;

@Component
public class TestDataInitializer {

    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;

    public TestDataInitializer(ReservationTimeRepository reservationTimeRepository, ThemeRepository themeRepository) {
        this.reservationTimeRepository = reservationTimeRepository;
        this.themeRepository = themeRepository;
    }

    public void initializeReservationTime(LocalTime localTime) {
        CreateReservationTimeParams createReservationTimeParams = new CreateReservationTimeParams(localTime.withSecond(0).withNano(0));
        reservationTimeRepository.save(createReservationTimeParams);
    }

    public void initializeTheme(String name, String description, String imageUrl) {
        CreateThemeParams createThemeParams = new CreateThemeParams(name, description, imageUrl);
        themeRepository.save(createThemeParams);
    }
}
