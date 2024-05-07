package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.domain.Theme;
import roomescape.exception.IllegalUserRequestException;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ThemeRepository;
import roomescape.service.dto.ThemeSaveRequest;

import java.time.LocalDate;
import java.util.List;

@Service
public class ThemeService {

    private static final int START_DAYS_SUBTRACT = 7;
    private static final int END_DAYS_SUBTRACT = 1;
    private static final int RANK_COUNT = 10;

    private final ThemeRepository themeRepository;
    private final ReservationRepository reservationRepository;

    public ThemeService(ThemeRepository themeRepository, ReservationRepository reservationRepository) {
        this.themeRepository = themeRepository;
        this.reservationRepository = reservationRepository;
    }


    public Theme createTheme(ThemeSaveRequest request) {
        Theme theme = ThemeSaveRequest.toEntity(request);
        return themeRepository.save(theme);
    }

    public List<Theme> findThemes() {
        return themeRepository.findAll();
    }

    public List<Theme> findTop10Recent7Days() {
        return themeRepository.findRanksBetween(
                LocalDate.now().minusDays(START_DAYS_SUBTRACT),
                LocalDate.now().minusDays(END_DAYS_SUBTRACT),
                RANK_COUNT);
    }

    public void deleteTheme(Long id) {
        if (reservationRepository.existsByReservationThemeId(id)) {
            throw new IllegalUserRequestException("이미 예약중인 테마는 삭제할 수 없습니다.");
        }

        int deleteCount = themeRepository.deleteById(id);
        if (deleteCount == 0) {
            throw new IllegalUserRequestException("존재하지 않는 테마입니다.");
        }
    }
}
