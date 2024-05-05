package roomescape.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.dao.ReservationRepository;
import roomescape.dao.ThemeRepository;
import roomescape.domain.Theme;
import roomescape.exception.InvalidReservationException;
import roomescape.service.dto.ThemeRequest;
import roomescape.service.dto.ThemeResponse;

@Service
public class ThemeService {
    private static final long MAXIMUM_COUNT = 10;

    private final ThemeRepository themeRepository;
    private final ReservationRepository reservationRepository;

    public ThemeService(ThemeRepository themeRepository, ReservationRepository reservationRepository) {
        this.themeRepository = themeRepository;
        this.reservationRepository = reservationRepository;
    }

    public ThemeResponse create(ThemeRequest themeRequest) {
        validateDuplicated(themeRequest);
        Theme theme = new Theme(themeRequest.name(), themeRequest.description(), themeRequest.thumbnail());
        Theme newTheme = themeRepository.save(theme);
        return new ThemeResponse(newTheme);
    }

    private void validateDuplicated(ThemeRequest themeRequest) {
        if (themeRepository.existsByName(themeRequest.name())) {
            throw new InvalidReservationException("이미 존재하는 테마 이름입니다.");
        }
    }

    public List<ThemeResponse> findAll() {
        return themeRepository.findAll().stream()
                .map(ThemeResponse::new)
                .toList();
    }

    public void deleteById(long id) {
        validateByReservation(id);
        themeRepository.deleteById(id);
    }

    private void validateByReservation(long id) {
        if (reservationRepository.existsByThemeId(id)) {
            throw new InvalidReservationException("해당 테마로 예약이 존재해서 삭제할 수 없습니다.");
        }
    }

    public List<ThemeResponse> findPopularThemes() {
        String startDate = LocalDate.now().minusDays(7).format(DateTimeFormatter.ISO_DATE);
        String endDate = LocalDate.now().minusDays(1).format(DateTimeFormatter.ISO_DATE);
        List<Theme> themes = themeRepository.getReferenceByReservationTermAndCount(startDate, endDate, MAXIMUM_COUNT);
        return themes.stream().map(ThemeResponse::new).toList();
    }
}
