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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
        ReservationDate reservationDate = new ReservationDate(reservationRequest.date());
        ReservationTime reservationTime = findTimeById(reservationRequest.timeId());
        Theme theme = findThemeById(reservationRequest.themeId());

        validate(reservationDate, reservationTime, theme);

        Reservation reservation = new Reservation(reservationRequest.name(), reservationDate, reservationTime, theme);

        return new ReservationResponse(reservationRepository.save(reservation));
    }

    private ReservationTime findTimeById(final long timeId) {
        return reservationTimeRepository.findById(timeId)
                .orElseThrow(() -> new InvalidReservationException("더이상 존재하지 않는 시간입니다."));
    }

    private Theme findThemeById(long themeId) {
        return themeRepository.findById(themeId).
                orElseThrow(() -> new InvalidReservationException("더이상 존재하지 않는 테마입니다."));
    }

    private void validate(final ReservationDate reservationDate, final ReservationTime reservationTime, final Theme theme) {
        validateIfBefore(reservationDate, reservationTime);
        validateDuplicated(reservationDate, reservationTime, theme);
    }

    private void validateIfBefore(final ReservationDate date, final ReservationTime time) {
        LocalDateTime value = LocalDateTime.of(LocalDate.parse(date.getValue()), LocalTime.parse(time.getStartAt()));
        if (value.isBefore(LocalDateTime.now())) {
            throw new InvalidReservationException("현재보다 이전으로 일정을 설정할 수 없습니다.");
        }
    }

    private void validateDuplicated(final ReservationDate reservationDate, final ReservationTime reservationTime, final Theme theme) {
        if (reservationRepository.existsByDateAndTimeAndTheme(reservationDate.getValue(), reservationTime.getId(), theme.getId())) {
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
