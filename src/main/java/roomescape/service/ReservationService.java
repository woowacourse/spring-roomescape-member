package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.dao.ReservationDao;
import roomescape.dao.ReservationTimeDao;
import roomescape.dao.ThemeDao;
import roomescape.domain.Reservation;
import roomescape.exception.CustomException2;
import roomescape.exception.NotExistsException;
import roomescape.repository.ReservationRepository;
import roomescape.service.dto.input.ReservationInput;
import roomescape.service.dto.output.ReservationOutput;
import roomescape.service.util.DateTimeFormatter;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationDao reservationDao;
    private final ReservationTimeDao reservationTimeDao;
    private final ThemeDao themeDao;
    private final DateTimeFormatter dateTimeFormatter;

    public ReservationService(final ReservationRepository reservationRepository,
                              final ReservationDao reservationDao,
                              final ReservationTimeDao reservationTimeDao,
                              final ThemeDao themeDao,
                              final DateTimeFormatter dateTimeFormatter) {
        this.reservationRepository = reservationRepository;
        this.reservationDao = reservationDao;
        this.reservationTimeDao = reservationTimeDao;
        this.themeDao = themeDao;
        this.dateTimeFormatter = dateTimeFormatter;
    }

    public ReservationOutput createReservation(final ReservationInput input) {
        final var member = reservationRepository.getMemberById(input.memberId());
        final var time = reservationRepository.getReservationTimeById(input.timeId());
        final var theme = reservationRepository.getThemeById(input.themeId());

        final var reservation = new Reservation(member, input.date(), time, theme);

        validatePastDateAndTime(reservation);

        final var savedReservation = reservationDao.create(reservation);
        return ReservationOutput.from(savedReservation);
    }

    private void validatePastDateAndTime(final Reservation reservation) {
        if (reservation.isBefore(dateTimeFormatter.getDate(), dateTimeFormatter.getTime())) {
            throw new CustomException2("지나간 날짜와 시간에 대한 예약 생성은 불가능합니다.");
        }
    }

    public List<ReservationOutput> getAllReservations() {
        final List<Reservation> reservations = reservationDao.getAll();
        return ReservationOutput.list(reservations);
    }

    public void deleteReservation(final long id) {
        if (!reservationDao.isExistById(id)) {
            throw NotExistsException.of("reservationId", id);
        }
        reservationDao.delete(id);
    }

    public List<ReservationOutput> filterReservations(final Long themeId,
                                     final Long memberId,
                                     final String dateFrom,
                                     final String dateTo) {
        final var reservations = reservationDao.filter(themeId, memberId, dateFrom, dateTo);
        return ReservationOutput.list(reservations);
    }
}
