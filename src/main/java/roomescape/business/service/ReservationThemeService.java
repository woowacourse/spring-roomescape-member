package roomescape.business.service;

import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;
import roomescape.business.ReservationTheme;
import roomescape.exception.ReservationThemeException;
import roomescape.persistence.ReservationRepository;
import roomescape.persistence.ReservationThemeRepository;
import roomescape.presentation.dto.request.ReservationThemeRequestDto;
import roomescape.presentation.dto.response.ReservationThemeResponseDto;

@Named
@Transactional
public class ReservationThemeService {

    private final ReservationRepository reservationRepository;
    private final ReservationThemeRepository reservationThemeRepository;

    @Inject
    public ReservationThemeService(ReservationRepository reservationRepository,
                                   ReservationThemeRepository reservationThemeRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationThemeRepository = reservationThemeRepository;
    }

    public List<ReservationThemeResponseDto> readThemeAll() {
        List<ReservationTheme> reservationThemes = reservationThemeRepository.findAll();
        return reservationThemes.stream()
                .map(reservationTheme -> new ReservationThemeResponseDto(
                                reservationTheme.getId(),
                                reservationTheme.getName(),
                                reservationTheme.getDescription(),
                                reservationTheme.getThumbnail()
                        )
                )
                .toList();
    }

    public List<ReservationThemeResponseDto> readBestReservedThemes() {
        LocalDate now = LocalDate.now(ZoneId.of("Asia/Seoul"));
        LocalDate start = calculateStartDate(now);
        LocalDate end = calculateEndDate(now);
        List<ReservationTheme> bestReservedReservationThemes = reservationThemeRepository.findByStartDateAndEndDateOrderByReservedDesc(
                start, end, 10);
        return bestReservedReservationThemes.stream()
                .map(bestTheme -> new ReservationThemeResponseDto(
                                bestTheme.getId(),
                                bestTheme.getName(),
                                bestTheme.getDescription(),
                                bestTheme.getThumbnail()
                        )
                )
                .toList();
    }

    private static LocalDate calculateEndDate(LocalDate nowDate) {
        return nowDate.minusDays(1);
    }

    private static LocalDate calculateStartDate(LocalDate nowDate) {
        return nowDate.minusDays(7);
    }

    public ReservationThemeResponseDto createTheme(ReservationThemeRequestDto reservationThemeDto) {
        try {
            Long id = reservationThemeRepository.add(new ReservationTheme(
                            reservationThemeDto.name(),
                            reservationThemeDto.description(),
                            reservationThemeDto.thumbnail()
                    )
            );
            return new ReservationThemeResponseDto(
                    id,
                    reservationThemeDto.name(),
                    reservationThemeDto.description(),
                    reservationThemeDto.thumbnail()
            );
        } catch (DataIntegrityViolationException exception) {
            throw new ReservationThemeException("동일한 이름의 테마를 추가할 수 없습니다.");
        }
    }

    public void deleteTheme(Long id) {
        if (reservationRepository.existByThemeId(id)) {
            throw new ReservationThemeException("해당 테마의 예약이 존재하여 삭제할 수 없습니다.");
        }
        reservationThemeRepository.deleteById(id);
    }
}
