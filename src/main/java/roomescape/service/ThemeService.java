package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.domain.Theme;
import roomescape.dto.ThemeCreateRequest;
import roomescape.dto.ThemeResponse;
import roomescape.exception.BadRequestException;
import roomescape.repository.ThemeRepository;
import roomescape.repository.reservation.ReservationRepository;

import java.util.List;

@Service
public class ThemeService {

    private final ThemeRepository themeRepository;
    private final ReservationRepository reservationRepository;

    public ThemeService(ThemeRepository themeRepository, ReservationRepository reservationRepository) {
        this.themeRepository = themeRepository;
        this.reservationRepository = reservationRepository;
    }

    public ThemeResponse createTheme(ThemeCreateRequest request) {
        Theme theme = request.toTheme();
        validateDuplicated(theme);
        Theme savedTheme = themeRepository.save(theme);
        return ThemeResponse.from(savedTheme);
    }

    private void validateDuplicated(Theme theme) {
        boolean isDuplicatedName = themeRepository.findAll().stream()
                .anyMatch(theme::isDuplicated);
        if (isDuplicatedName) {
            throw new BadRequestException("중복된 테마 이름입니다.");
        }
    }

    public ThemeResponse readTheme(Long id) {
        Theme theme = themeRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("존재하지 않는 테마입니다."));
        return ThemeResponse.from(theme);
    }

    public List<ThemeResponse> readThemes() {
        return themeRepository.findAll().stream()
                .map(ThemeResponse::from)
                .toList();
    }

    public void deleteTheme(Long id) {
        if (reservationRepository.existsByThemeId(id)) {
            throw new BadRequestException("해당 테마에 예약이 존재합니다.");
        }
        themeRepository.delete(id);
    }
}
