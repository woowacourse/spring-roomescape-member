package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.ReservationTheme;
import roomescape.dto.ReservationThemeRequest;
import roomescape.dto.ReservationThemeResponse;
import roomescape.exception.exception.DataNotFoundException;
import roomescape.exception.exception.DeletionNotAllowedException;
import roomescape.repository.RoomescapeRepository;
import roomescape.repository.RoomescapeThemeRepository;

@Service
public class ReservationThemeService {

    private static final int POPULAR_RESERVATION_DAYS_CRITERIA = 7;

    private final RoomescapeRepository roomescapeRepository;
    private final RoomescapeThemeRepository themeRepository;

    public ReservationThemeService(final RoomescapeRepository roomescapeRepository,
                                   RoomescapeThemeRepository themeRepository) {
        this.roomescapeRepository = roomescapeRepository;
        this.themeRepository = themeRepository;
    }

    public List<ReservationThemeResponse> findReservationThemes() {
        List<ReservationTheme> reservationThemes = themeRepository.findAll();
        return reservationThemes.stream().map(ReservationThemeResponse::of).toList();
    }

    public List<ReservationThemeResponse> findPopularReservations() {
        List<ReservationTheme> popularReservationThemes = themeRepository.findTopThemeOrderByCountWithinDaysDesc(
                POPULAR_RESERVATION_DAYS_CRITERIA);
        return popularReservationThemes.stream().map(ReservationThemeResponse::of).toList();
    }

    public ReservationThemeResponse addReservationTheme(final ReservationThemeRequest request) {
        ReservationTheme reservationTheme = new ReservationTheme(request.name(), request.description(),
                request.thumbnail());
        ReservationTheme saved = themeRepository.save(reservationTheme);
        return ReservationThemeResponse.of(saved);
    }

    public void removeReservationTheme(final long themeId) {
        if (roomescapeRepository.existsByThemeId(themeId)) {
            throw new DeletionNotAllowedException("[ERROR] 예약이 연결된 테마는 삭제할 수 없습니다. 관련 예약을 먼저 삭제해주세요.");
        }
        if (!themeRepository.deleteById(themeId)) {
            throw new DataNotFoundException(String.format("[ERROR] 예약 테마 %d번에 해당하는 테마가 없습니다.", themeId));
        }
    }
}
