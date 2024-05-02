package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationDate;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.dto.AvailableReservationTimeResponse;
import roomescape.dto.SaveReservationRequest;
import roomescape.dto.SaveReservationTimeRequest;
import roomescape.dto.SaveThemeRequest;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;

    public ReservationService(
            final ReservationRepository reservationRepository,
            final ReservationTimeRepository reservationTimeRepository,
            final ThemeRepository themeRepository
    ) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.themeRepository = themeRepository;
    }

    public List<Reservation> getReservations() {
        return reservationRepository.findAll();
    }

    public Reservation saveReservation(final SaveReservationRequest request) {
        final ReservationTime reservationTime = reservationTimeRepository.findById(request.timeId())
                .orElseThrow(() -> new NoSuchElementException("해당 id의 예약 시간이 존재하지 않습니다."));
        final Theme theme = themeRepository.findById(request.themeId())
                .orElseThrow(() -> new NoSuchElementException("해당 id의 테마가 존재하지 않습니다."));

        validateReservationDuplication(request);

        return reservationRepository.save(request.toReservation(reservationTime, theme));
    }

    private void validateReservationDuplication(final SaveReservationRequest request) {
        if (reservationRepository.existByDateAndTimeIdAndThemeId(request.date(), request.timeId(), request.themeId())) {
            throw new IllegalArgumentException("이미 해당 날짜/시간의 테마 예약이 있습니다.");
        }
    }

    public void deleteReservation(final Long reservationId) {
        checkReservationExist(reservationId);
        reservationRepository.deleteById(reservationId);
    }

    private void checkReservationExist(final Long reservationId) {
        reservationRepository.findById(reservationId)
                .orElseThrow(() -> new NoSuchElementException("해당 id의 예약이 존재하지 않습니다."));
    }

    public List<ReservationTime> getReservationTimes() {
        return reservationTimeRepository.findAll();
    }

    public ReservationTime saveReservationTime(final SaveReservationTimeRequest request) {
        validateReservationTimeDuplication(request);

        return reservationTimeRepository.save(request.toReservationTime());
    }

    private void validateReservationTimeDuplication(final SaveReservationTimeRequest request) {
        if (reservationTimeRepository.existByStartAt(request.startAt())) {
            throw new IllegalArgumentException("이미 존재하는 예약시간이 있습니다.");
        }
    }

    public void deleteReservationTime(final Long reservationTimeId) {
        checkReservationTimeExist(reservationTimeId);
        validateReservationTimeExist(reservationTimeId);
        reservationTimeRepository.deleteById(reservationTimeId);
    }

    private void checkReservationTimeExist(final Long reservationTimeId) {
        if (!reservationTimeRepository.existById(reservationTimeId)) {
            throw new NoSuchElementException("해당 id의 예약 시간이 존재하지 않습니다.");
        }
    }

    private void validateReservationTimeExist(final Long reservationTimeId) {
        if (reservationRepository.existByTimeId(reservationTimeId)) {
            throw new IllegalArgumentException("예약에 포함된 시간 정보는 삭제할 수 없습니다.");
        }
    }

    public List<Theme> getThemes() {
        return themeRepository.findAll();
    }

    public Theme saveTheme(final SaveThemeRequest saveThemeRequest) {
        return themeRepository.save(saveThemeRequest.toTheme());
    }

    public void deleteTheme(final Long themeId) {
        validateThemeExist(themeId);

        themeRepository.deleteById(themeId);
    }

    private void validateThemeExist(final Long themeId) {
        if (!themeRepository.existById(themeId)) {
            throw new NoSuchElementException("해당 id의 테마 정보가 존재하지 않습니다.");
        }
    }

    // TODO : 서비스가 컨트롤러에서 응답용으로 생성할 DTO를 만드는게 좋은걸까...? 고민이 필요
    // TODO : AvailableReservationTimeResponse를 별도의 객체로 변환할 수 있지 않을까
    public List<AvailableReservationTimeResponse> getAvailableReservationTimes(final LocalDate date, final Long themeId) {
        final List<ReservationTime> reservationTimes = reservationTimeRepository.findAll();
        final List<Reservation> reservations = reservationRepository.findAllByDateAndThemeId(date, themeId);

        return reservationTimes.stream()
                .map(reservationTime -> AvailableReservationTimeResponse.of(reservationTime, reservations))
                .toList();
    }

    public List<Theme> getPopularThemes() {
        final ReservationDate startAt = new ReservationDate(LocalDate.now().minusDays(7));
        final ReservationDate endAt = new ReservationDate(LocalDate.now().minusDays(1));
        final int maximumThemeCount = 10;

        return themeRepository.findPopularThemes(startAt, endAt, maximumThemeCount);
    }
}
