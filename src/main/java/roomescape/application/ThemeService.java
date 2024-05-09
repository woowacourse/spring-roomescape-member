package roomescape.application;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.reservation.repository.ReservationRepository;
import roomescape.domain.theme.Theme;
import roomescape.domain.theme.repository.ThemeRepository;
import roomescape.domain.time.ReservationTime;
import roomescape.domain.time.repository.ReservationTimeRepository;
import roomescape.dto.theme.AvailableTimeResponse;
import roomescape.dto.theme.ThemeRequest;

@Service
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

    public List<Theme> getThemes() {
        return themeRepository.findAll();
    }

    public Theme save(ThemeRequest request) {
        validateDuplicateName(request.name());
        Theme theme = new Theme(request.name(), request.description(), request.thumbnail());
        return themeRepository.save(theme);
    }

    private void validateDuplicateName(String name) {
        if (themeRepository.existsByName(name)) {
            throw new IllegalArgumentException("테마 이름이 존재합니다.");
        }
    }

    public void delete(long id) {
        validateIdIsExist(id);
        themeRepository.deleteById(id);
    }

    private void validateIdIsExist(long id) {
        themeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("삭제하고자 하는 id가 존재하지 않습니다."));
    }

    public List<Theme> getPopularThemes() {
        return themeRepository.findPopularThemes(LocalDate.now());
    }

    public List<AvailableTimeResponse> getAvailableTimes(long id, LocalDate date) {
        List<AvailableTimeResponse> responses = new ArrayList<>();
        for (ReservationTime reservationTime : reservationTimeRepository.findAll()) {
            boolean alreadyBooked = reservationRepository.existsByReservationDateTimeAndTheme(date,
                    reservationTime.getId(), id);
            AvailableTimeResponse response = AvailableTimeResponse.from(reservationTime, alreadyBooked);
            responses.add(response);
        }
        return responses;
    }
}
