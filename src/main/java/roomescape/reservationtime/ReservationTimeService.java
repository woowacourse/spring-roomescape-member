package roomescape.reservationtime;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import roomescape.globalexception.BadRequestException;
import roomescape.globalexception.ConflictException;
import roomescape.reservationtime.dto.AvailableReservationTimeResponse;
import roomescape.reservationtime.dto.ReservationTimeRequest;
import roomescape.reservationtime.dto.ReservationTimeResponse;

@Service
public class ReservationTimeService {

    private final ReservationTimeRepository reservationTimeRepository;

    public ReservationTimeService(
            @Autowired final ReservationTimeRepository reservationTimeRepository
    ) {
        this.reservationTimeRepository = reservationTimeRepository;
    }

    public ReservationTimeResponse create(final ReservationTimeRequest request) {
        validateDuplicateTime(request.startAt());

        final ReservationTime reservationTime = new ReservationTime(request.startAt());
        final Long id = reservationTimeRepository.save(reservationTime);
        final ReservationTime savedReservationTime = reservationTimeRepository.findById(id);
        return ReservationTimeResponse.from(savedReservationTime);
    }

    public List<ReservationTimeResponse> findAll() {
        return reservationTimeRepository.findAll().stream()
                .map(ReservationTimeResponse::from)
                .toList();
    }

    public List<AvailableReservationTimeResponse> findAllAvailableTimes(final Long themeId, final LocalDate date) {
        final List<ReservationTime> allTime = reservationTimeRepository.findAll();
        final List<ReservationTime> allByThemeIdAndDate = reservationTimeRepository.findAllByThemeIdAndDate(themeId,
                date);

        final Set<ReservationTime> reservationTimesByThemeAndDate = new HashSet<>(allByThemeIdAndDate);
        return allTime.stream()
                .map(reservationTime ->
                        AvailableReservationTimeResponse.from(
                                reservationTime,
                                reservationTimesByThemeAndDate.contains(reservationTime)
                        )
                )
                .toList();
    }

    public void deleteById(final Long id) {
        try {
            reservationTimeRepository.deleteById(id);
        } catch (final DataIntegrityViolationException e) {
            throw new BadRequestException("예약 시간이 사용중입니다.");
        }
    }

    private void validateDuplicateTime(final LocalTime startAt) {
        if (reservationTimeRepository.existsByStartAt(startAt)) {
            throw new ConflictException("이미 등록된 시간입니다.");
        }
    }
}
