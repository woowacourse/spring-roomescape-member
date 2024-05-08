package roomescape.core.service;

import static java.util.stream.Collectors.toSet;

import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.core.domain.Reservation;
import roomescape.core.domain.ReservationTime;
import roomescape.core.dto.BookingTimeResponseDto;
import roomescape.core.dto.ReservationTimeRequestDto;
import roomescape.core.repository.ReservationRepository;
import roomescape.core.repository.ReservationTimeRepository;
import roomescape.web.exception.BadRequestException;

@Service
public class ReservationTimeService {
    private final ReservationTimeRepository reservationTimeRepository;
    private final ReservationRepository reservationRepository;

    public ReservationTimeService(
            final ReservationTimeRepository reservationTimeRepository,
            final ReservationRepository reservationRepository
    ) {
        this.reservationTimeRepository = reservationTimeRepository;
        this.reservationRepository = reservationRepository;
    }

    @Transactional
    public ReservationTime create(final ReservationTimeRequestDto request) {
        final ReservationTime time = new ReservationTime(request.getStartAt());
        validateDuplicatedStartAt(time);
        final Long id = reservationTimeRepository.save(time);
        return new ReservationTime(id, time.getStartAtString());
    }

    @Transactional(readOnly = true)
    public List<ReservationTime> findAll() {
        return reservationTimeRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<BookingTimeResponseDto> findBookable(final String date, final long themeId) {
        List<ReservationTime> times = reservationTimeRepository.findAll();
        Set<Long> timeIdOfReservation = reservationRepository.findAllByDateAndThemeId(date, themeId)
                .stream()
                .map(Reservation::getTimeId)
                .collect(toSet());
        return times.stream()
                .map(reservationTime -> createBookingTime(reservationTime, timeIdOfReservation))
                .toList();
    }

    private BookingTimeResponseDto createBookingTime(final ReservationTime time, final Set<Long> timeIdOfReservation) {
        if (timeIdOfReservation.contains(time.getId())) {
            return new BookingTimeResponseDto(time.getId(), time.getStartAtString(), true);
        }
        return new BookingTimeResponseDto(time.getId(), time.getStartAtString(), false);
    }

    @Transactional
    public void delete(final long id) {
        final boolean exist = reservationRepository.hasReservationAtTime(id);
        if (exist) {
            throw new BadRequestException("해당 시간에 예약한 내역이 존재하여 삭제할 수 없습니다.");
        }
        reservationTimeRepository.deleteById(id);
    }

    private void validateDuplicatedStartAt(final ReservationTime reservationTime) {
        final boolean exist = reservationTimeRepository.hasDuplicateReservationTime(reservationTime.getStartAtString());
        if (exist) {
            throw new BadRequestException("해당 시간이 이미 존재합니다.");
        }
    }
}
