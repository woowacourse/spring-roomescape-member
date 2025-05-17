package roomescape.business.service.admin;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.business.domain.reservation.ReservationTheme;
import roomescape.exception.ReservationThemeException;
import roomescape.persistence.ReservationRepository;
import roomescape.persistence.ReservationThemeRepository;
import roomescape.presentation.admin.dto.ReservationThemeRequestDto;
import roomescape.presentation.admin.dto.ReservationThemeResponseDto;

@Service
public class AdminReservationThemeService {

    private final ReservationRepository reservationRepository;
    private final ReservationThemeRepository reservationThemeRepository;

    public AdminReservationThemeService(ReservationRepository reservationRepository,
                                        ReservationThemeRepository reservationThemeRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationThemeRepository = reservationThemeRepository;
    }

    @Transactional
    public ReservationThemeResponseDto createTheme(ReservationThemeRequestDto reservationThemeDto) {
        if (reservationThemeRepository.existByName(reservationThemeDto.name())) {
            throw new ReservationThemeException("동일한 이름의 테마를 추가할 수 없습니다.");
        }
        ReservationTheme reservationTheme = reservationThemeRepository.add(new ReservationTheme(
                        reservationThemeDto.name(),
                        reservationThemeDto.description(),
                        reservationThemeDto.thumbnail()
                )
        );
        return ReservationThemeResponseDto.toResponse(reservationTheme);
    }

    @Transactional
    public void deleteThemeById(Long id) {
        if (reservationRepository.existsByThemeId(id)) {
            throw new ReservationThemeException("해당 테마의 예약이 존재하여 삭제할 수 없습니다.");
        }
        reservationThemeRepository.deleteById(id);
    }
}
