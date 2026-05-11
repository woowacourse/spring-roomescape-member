package roomescape.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.dto.ThemeAllResponse;
import roomescape.dto.ThemeRequest;
import roomescape.dto.ThemeResponse;
import roomescape.model.Theme;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ThemeRepository;

@Service
public class ThemeService {

    private final ThemeRepository themeRepository;
    private final ReservationRepository reservationRepository;

    public ThemeService(ThemeRepository themeRepository, ReservationRepository reservationRepository) {
        this.themeRepository = themeRepository;
        this.reservationRepository = reservationRepository;
    }

    public ThemeResponse register(ThemeRequest themeRequest) {
        Theme theme = new Theme(null, themeRequest.name(), themeRequest.description(), themeRequest.url());
        Theme saved = themeRepository.save(theme);
        return ThemeResponse.from(saved);
    }

    public void removeById(Long id) {
        if (reservationRepository.existsByThemeId(id)) {
            throw new IllegalArgumentException("해당 테마에 예약이 존재하여 삭제할 수 없습니다.");
        }
        int deleteCnt = themeRepository.deleteById(id);
        if (deleteCnt == 0) {
            throw new IllegalArgumentException("존재하지 않는 테마의 ID 입니다.");
        }
    }

    public ThemeAllResponse readAll() {
        List<Theme> themes = themeRepository.findAll();
        List<ThemeResponse> responses = themes.stream()
                .map(ThemeResponse::from)
                .toList();
        return new ThemeAllResponse(responses);
    }

    public ThemeAllResponse readRanks(Long limit) {
        LocalDate currentDay = LocalDate.now().minusDays(1);
        LocalDate lastWeekDay = LocalDate.now().minusWeeks(1);
        List<Theme> themes = themeRepository.findByCurrentDateAndLastWeekDateAndLimit(currentDay.toString(),
                lastWeekDay.toString(), limit);

        List<ThemeResponse> responses = themes.stream()
                .map(ThemeResponse::from)
                .toList();
        return new ThemeAllResponse(responses);
    }
}
