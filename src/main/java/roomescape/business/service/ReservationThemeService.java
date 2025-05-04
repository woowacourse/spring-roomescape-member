package roomescape.business.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.business.ReservationTheme;
import roomescape.exception.ReservationThemeException;
import roomescape.presentation.mapper.ReservationThemeMapper;
import roomescape.persistence.ReservationRepository;
import roomescape.persistence.ReservationThemeRepository;
import roomescape.presentation.dto.ReservationThemeRequestDto;
import roomescape.presentation.dto.ReservationThemeResponseDto;

@Service
@Transactional
public class ReservationThemeService {

    private static final int BEST_THEME_RANGE_DAYS = 7;
    private static final int BEST_THEME_END_OFFSET = 1;
    private static final int TOP_THEME_LIMIT = 10;

    private final ReservationRepository reservationRepository;
    private final ReservationThemeRepository reservationThemeRepository;

    @Autowired
    public ReservationThemeService(ReservationRepository reservationRepository,
                                   ReservationThemeRepository reservationThemeRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationThemeRepository = reservationThemeRepository;
    }

    public List<ReservationThemeResponseDto> getAllThemes() {
        List<ReservationTheme> reservationThemes = reservationThemeRepository.findAll();
        return reservationThemes.stream()
                .map(ReservationThemeMapper::toResponse)
                .toList();
    }

    public List<ReservationThemeResponseDto> findBestReservedThemes() {
        LocalDate now = LocalDate.now();
        LocalDate start = calculateStartDate(now);
        LocalDate end = calculateEndDate(now);
        return getBestReservedThemes(start, end, TOP_THEME_LIMIT)
                .stream()
                .map(ReservationThemeMapper::toResponse)
                .toList();
    }

    private List<ReservationTheme> getBestReservedThemes(LocalDate start, LocalDate end, int limit) {
        return reservationThemeRepository.findByStartDateAndEndDateOrderByReservedDesc(start, end, limit);
    }

    private static LocalDate calculateStartDate(LocalDate nowDate) {
        return nowDate.minusDays(BEST_THEME_RANGE_DAYS);
    }

    private static LocalDate calculateEndDate(LocalDate nowDate) {
        return nowDate.minusDays(BEST_THEME_END_OFFSET);
    }

    public ReservationThemeResponseDto createTheme(ReservationThemeRequestDto reservationThemeDto) {
        if (reservationThemeRepository.existByName(reservationThemeDto.name())) {
            throw new ReservationThemeException("동일한 이름의 테마를 추가할 수 없습니다.");
        }
        Long id = reservationThemeRepository.add(new ReservationTheme(
                        reservationThemeDto.name(),
                        reservationThemeDto.description(),
                        reservationThemeDto.thumbnail()
                )
        );
        return ReservationThemeMapper.toResponse(
                new ReservationTheme(
                        id,
                        reservationThemeDto.name(),
                        reservationThemeDto.description(),
                        reservationThemeDto.thumbnail()
                )
        );
    }

    public void deleteThemeById(Long id) {
        if (reservationRepository.existByThemeId(id)) {
            throw new ReservationThemeException("해당 테마의 예약이 존재하여 삭제할 수 없습니다.");
        }
        reservationThemeRepository.deleteById(id);
    }
}
