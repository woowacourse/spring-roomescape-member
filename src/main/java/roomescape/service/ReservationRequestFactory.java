package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Component;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.dto.ReservationCreateRequestDto;
import roomescape.exception.DuplicateContentException;
import roomescape.exception.InvalidRequestException;
import roomescape.exception.NotFoundException;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;

@Component
public class ReservationRequestFactory {

    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;

    public ReservationRequestFactory(ReservationTimeRepository reservationTimeRepository, ThemeRepository themeRepository) {
        this.reservationTimeRepository = reservationTimeRepository;
        this.themeRepository = themeRepository;
    }

    public Reservation createReservationRequest(ReservationCreateRequestDto dto, ReservationRepository reservationRepository) {
        ReservationTime reservationTime = reservationTimeRepository.findById(dto.timeId())
                .orElseThrow(() -> new NotFoundException("[ERROR] 예약 시간을 찾을 수 없습니다. id : " + dto.timeId()));
        Theme theme = themeRepository.findById(dto.themeId())
                .orElseThrow(() -> new NotFoundException("[ERROR] 테마를 찾을 수 없습니다. id : " + dto.themeId()));

        Reservation reservation = dto.createWithoutId(reservationTime, theme);

        validateReservationRequest(reservation, reservationRepository);

        return reservation;
    }

    private void validateReservationRequest(Reservation reservationRequest, ReservationRepository reservationRepository) {
        if (reservationRequest.isBeforeCurrentDateTime()) {
            throw new InvalidRequestException("[ERROR] 현 시점 이후의 날짜와 시간을 선택해주세요.");
        }

        ReservationTime time = reservationRequest.getTime();
        Theme theme = reservationRequest.getTheme();
        List<Reservation> reservations = reservationRepository.findByDateTimeTheme(reservationRequest.getDate(), time.getStartAt(), theme.getId());
        if (!reservations.isEmpty()) {
            throw new DuplicateContentException("[ERROR] 이미 예약이 존재합니다. 다른 예약 일정을 선택해주세요.");
        }
    }
}
