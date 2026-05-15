package roomescape.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import roomescape.controller.dto.ThemeCreateRequest;
import roomescape.controller.dto.ThemeFamousFindRequest;
import roomescape.domain.Theme;
import roomescape.exception.ConflictException;
import roomescape.exception.NotFoundException;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ThemeRepository;

@Service
public class ThemeService {
    private static final long DEFAULT_DAYS = 7;
    private static final long DEFAULT_LIMIT = 10;
    private static final String THEME_DOES_NOT_EXISTS = "존재하지 않는 테마입니다";
    private static final String THEME_HAS_RESERVATION = "해당 테마에 예약이 존재하여 삭제할 수 없습니다.";

    private final ThemeRepository themeRepository;
    private final ReservationRepository reservationRepository;

    public ThemeService(ThemeRepository themeRepository, ReservationRepository reservationRepository) {
        this.themeRepository = themeRepository;
        this.reservationRepository = reservationRepository;
    }

    public Theme find(long themeId) {
        return themeRepository.findById(themeId)
                .orElseThrow(() -> new NotFoundException(THEME_DOES_NOT_EXISTS));
    }

    public List<Theme> findAll() {
        return themeRepository.findAll();
    }

    public Theme create(ThemeCreateRequest request) {
        Theme theme = Theme.of(request.getName(), request.getDescription(), request.getThumbnailUrl());
        return themeRepository.save(theme);
    }

    public void delete(long id) {
        themeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(THEME_DOES_NOT_EXISTS));
        if (reservationRepository.existsByThemeId(id)) {
            throw new ConflictException(THEME_HAS_RESERVATION);
        }
        themeRepository.deleteById(id);
    }

    public List<Theme> findFamous(ThemeFamousFindRequest request) {
        long days = Optional.ofNullable(request.getDays()).orElse(DEFAULT_DAYS);
        long limit = Optional.ofNullable(request.getLimit()).orElse(DEFAULT_LIMIT);
        LocalDate date = Optional.ofNullable(request.getDate()).orElseGet(LocalDate::now);

        return themeRepository.findFamous(days, date, limit);
    }
}
