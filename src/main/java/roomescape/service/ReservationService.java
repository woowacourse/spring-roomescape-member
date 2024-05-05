package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationDate;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.domain.repostiory.ReservationRepository;
import roomescape.domain.repostiory.ReservationTimeRepository;
import roomescape.domain.repostiory.ThemeRepository;
import roomescape.exception.InvalidReservationException;
import roomescape.service.dto.ReservationRequest;
import roomescape.service.dto.ReservationResponse;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;

    public ReservationService(final ReservationRepository reservationRepository,
                              final ReservationTimeRepository reservationTimeRepository,
                              ThemeRepository themeRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.themeRepository = themeRepository;
    }

    public ReservationResponse create(final ReservationRequest reservationRequest) {
        ReservationTime reservationTime = findTimeById(reservationRequest.timeId());
        Theme theme = findThemeById(reservationRequest.themeId());

        validate(reservationRequest.date(), reservationTime, theme);

        Reservation reservation = new Reservation(reservationRequest.name(), reservationRequest.date(), reservationTime, theme);

        return new ReservationResponse(reservationRepository.save(reservation));
    }

    private ReservationTime findTimeById(final long timeId) {
        return reservationTimeRepository.getById(timeId);
    }

    private Theme findThemeById(long themeId) {
        return themeRepository.getById(themeId);
    }

    private void validate(final String date, final ReservationTime reservationTime, final Theme theme) {
        ReservationDate reservationDate = new ReservationDate(date);
        validateIfBefore(reservationDate, reservationTime);
        validateDuplicated(date, reservationTime, theme);
    }

    private void validateIfBefore(final ReservationDate date, final ReservationTime time) {
        LocalDateTime value = LocalDateTime.of(date.getValue(), time.getStartAt());
        if (value.isBefore(LocalDateTime.now())) {
            throw new InvalidReservationException("현재보다 이전으로 일정을 설정할 수 없습니다.");
        }
    }

    private void validateDuplicated(final String date, final ReservationTime reservationTime, final Theme theme) {
        if (reservationRepository.existsByDateAndTimeAndTheme(date, reservationTime.getId(), theme.getId())) {
            throw new InvalidReservationException("선택하신 테마와 일정은 이미 예약이 존재합니다.");
        }
    }

    public List<ReservationResponse> findAll() {
        return reservationRepository.findAll().stream()
                .map(ReservationResponse::new)
                .toList();
    }

    public void deleteById(final long id) {
        reservationRepository.deleteById(id);
    }
}
