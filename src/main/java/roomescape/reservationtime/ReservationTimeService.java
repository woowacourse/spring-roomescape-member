package roomescape.reservationtime;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import roomescape.globalexception.BadRequestException;
import roomescape.globalexception.ConflictException;
import roomescape.globalexception.NotFoundException;
import roomescape.reservation.ReservationRepository;
import roomescape.reservationtime.dto.AvailableReservationTimeResponse;
import roomescape.reservationtime.dto.ReservationTimeRequest;
import roomescape.reservationtime.dto.ReservationTimeResponse;

@Service
public class ReservationTimeService {

    private final ReservationTimeRepository reservationTimeRepository;
    private final ReservationRepository reservationRepository;

    @Autowired
    public ReservationTimeService(
            final ReservationTimeRepository reservationTimeRepository,
            final ReservationRepository reservationRepository
    ) {
        this.reservationTimeRepository = reservationTimeRepository;
        this.reservationRepository = reservationRepository;
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
        validateExistsResrvationTime(id);
        validateUnusedReservationTime(id);

        reservationTimeRepository.deleteById(id);
    }

    private void validateUnusedReservationTime(final Long id) {
        if (reservationRepository.existsByReservationTime(id)) {
            throw new BadRequestException("예약시간이 사용중입니다.");
        }
    }

    private void validateExistsResrvationTime(final Long id) {
        if (!reservationTimeRepository.existsById(id)) {
            throw new NotFoundException("시간이 존재하지 않습니다.");
        }
    }

    private void validateDuplicateTime(final LocalTime startAt) {
        if (reservationTimeRepository.existsByStartAt(startAt)) {
            throw new ConflictException("이미 등록된 시간입니다.");
        }
    }
}
