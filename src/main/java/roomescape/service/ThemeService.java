package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.ReservationRepository;
import roomescape.domain.Theme;
import roomescape.domain.ThemeRepository;
import roomescape.service.dto.ThemeRequestDto;
import roomescape.service.dto.ThemeResponseDto;

@Service
public class ThemeService {

    private final ThemeRepository themeRepository;
    private final ReservationRepository reservationRepository;

    public ThemeService(ThemeRepository themeRepository, ReservationRepository reservationRepository) {
        this.themeRepository = themeRepository;
        this.reservationRepository = reservationRepository;
    }

    public List<ThemeResponseDto> findAllThemes() {
        return themeRepository.findAllThemes()
                .stream()
                .map(ThemeResponseDto::new)
                .toList();
    }

    public ThemeResponseDto createTheme(ThemeRequestDto requestDto) {
        Theme theme = themeRepository.insertTheme(requestDto.toTheme());
        return new ThemeResponseDto(theme);
    }

    public void deleteTheme(long id) {
        if (!themeRepository.isExistThemeOf(id)) {
            throw new IllegalArgumentException("존재하지 않는 아이디입니다.");
        }
        if (reservationRepository.hasReservationOfThemeId(id)) {
            throw new IllegalArgumentException("해당 테마에 예약이 있어 삭제할 수 없습니다.");
        }
        themeRepository.deleteThemeById(id);
    }
}
