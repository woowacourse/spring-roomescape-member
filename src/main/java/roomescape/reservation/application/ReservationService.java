package roomescape.reservation.application;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationTime;
import roomescape.reservation.domain.Theme;
import roomescape.reservation.infra.ReservationRepository;
import roomescape.reservation.infra.ReservationTimeRepository;
import roomescape.reservation.infra.ThemeRepository;
import roomescape.reservation.presentation.dto.request.ReservationSaveRequest;
import roomescape.reservation.presentation.dto.response.ReservationFindResponse;
import roomescape.reservation.presentation.dto.response.ReservationSaveResponse;
import org.springframework.stereotype.Service;
import roomescape.reservation.presentation.dto.response.dto.TimeInformation;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;

    @Transactional
    public ReservationSaveResponse save(ReservationSaveRequest body) {
        ReservationTime time = reservationTimeRepository.findById(body.timeId())
                .orElseThrow(() -> new IllegalArgumentException("해당 ID를 갖는 시간대는 존재하지 않습니다."));
        Theme theme = themeRepository.findById(body.themeId())
                .orElseThrow(() -> new IllegalArgumentException("해당 ID를 갖는 테마는 존재하지 않습니다."));
        Reservation reservation = reservationRepository.save(body.toDomain(time, theme));

        return ReservationSaveResponse.of(time, theme, reservation);
    }

    public List<ReservationFindResponse> findAll() {
        return reservationRepository.findAll().stream()
                .map(reservation -> new ReservationFindResponse(
                        reservation.getId(),
                        reservation.getName(),
                        reservation.getDate(),
                        new TimeInformation(
                                reservation.getTimeId(),
                                reservation.getStartAt()
                        ),
                        reservation.getThemeId()
                ))
                .toList();
    }

    @Transactional
    public void delete(Long id) {
        reservationRepository.deleteById(id);
    }
}
