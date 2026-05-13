package roomescape.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.Theme;
import roomescape.exception.CustomException;
import roomescape.exception.ErrorCode;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ThemeRepository;
import roomescape.service.dto.ServiceThemeRequest;
import roomescape.service.dto.ServiceThemeResponse;

@Service
@Transactional(readOnly = true)
public class ThemeService {

    public static final int RANKING_LIMIT = 10;
    public static final int MAX_RANKING_PERIOD = 366;

    private final ThemeRepository themeRepository;
    private final ReservationRepository reservationRepository;

    public ThemeService(ThemeRepository themeRepository, ReservationRepository reservationRepository) {
        this.themeRepository = themeRepository;
        this.reservationRepository = reservationRepository;
    }

    @Transactional
    public ServiceThemeResponse create(ServiceThemeRequest requestDto) {
        Theme theme = requestDto.toEntity();
        return ServiceThemeResponse.from(themeRepository.create(theme));
    }

    public List<ServiceThemeResponse> readAll() {
        return themeRepository.readAll().stream()
                .map(ServiceThemeResponse::from)
                .toList();
    }

    public List<ServiceThemeResponse> readRanking(LocalDate startDate, LocalDate endDate) {
        LocalDate localDate = LocalDate.now();

        if (startDate.isAfter(localDate) || endDate.isAfter(localDate)) {
            throw new CustomException(ErrorCode.FUTURE_RANKING_PERIOD);
        }
        if (startDate.isAfter(endDate)) {
            throw new CustomException(ErrorCode.INVALID_RANKING_PERIOD);
        }
        if (ChronoUnit.DAYS.between(startDate, endDate) > MAX_RANKING_PERIOD) {
            throw new CustomException(ErrorCode.LONG_RANKING_PERIOD);
        }

        return themeRepository.readRanking(startDate, endDate, RANKING_LIMIT).stream()
                .map(ServiceThemeResponse::from)
                .toList();
    }

    @Transactional
    public void delete(Long id) {
        if (reservationRepository.existByThemeId(id)) {
            throw new CustomException(ErrorCode.REFERENCED_THEME);
        }
        themeRepository.delete(id);
    }
}
