package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.domain.Reservation;
import roomescape.domain.Theme;
import roomescape.dto.ThemeCreateRequest;
import roomescape.dto.ThemeResponse;
import roomescape.exception.BadRequestException;
import roomescape.repository.ThemeRepository;
import roomescape.repository.reservation.ReservationRepository;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    public List<ThemeResponse> readPopularThemes() {
        LocalDate end = LocalDate.now().minusDays(1L);
        LocalDate start = end.minusDays(7L);

        Map<Long, List<Reservation>> reservationMap = reservationRepository.findByDateBetween(start, end).stream()
                .collect(Collectors.groupingBy(reservation -> reservation.getTheme().getId()));

        return reservationMap.keySet().stream()
                .sorted(Comparator.comparing(k -> reservationMap.get(k).size()).reversed())
                .limit(10L)
                .map(this::readTheme)
                .toList();
    }

    public void deleteTheme(Long id) {
        if (reservationRepository.existsByThemeId(id)) {
            throw new BadRequestException("해당 테마에 예약이 존재합니다.");
        }
        themeRepository.delete(id);
    }
}
