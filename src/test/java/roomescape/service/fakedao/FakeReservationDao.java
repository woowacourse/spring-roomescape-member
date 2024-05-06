package roomescape.service.fakedao;

import roomescape.repository.dao.ReservationDao;
import roomescape.repository.dto.ReservationSavedDto;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class FakeReservationDao implements ReservationDao {

    private final AtomicLong index = new AtomicLong(1);
    private final List<ReservationSavedDto> reservations = new ArrayList<>();

    public FakeReservationDao(List<ReservationSavedDto> reservations) {
        reservations.forEach(this::save);
    }

    @Override
    public List<ReservationSavedDto> findAll() {
        return Collections.unmodifiableList(reservations);
    }

    @Override
    public long save(ReservationSavedDto rawDto) {
        long key = index.getAndIncrement();
        ReservationSavedDto reservationSavedDto = new ReservationSavedDto(
                key, rawDto.getName(), rawDto.getDate(),
                rawDto.getTimeId(), rawDto.getThemeId());
        reservations.add(reservationSavedDto);
        return key;
    }

    @Override
    public Optional<ReservationSavedDto> findById(long id) {
        System.out.print("findById = " + id);
        System.out.println(reservations);
        return reservations.stream()
                .filter(reservation -> reservation.getId() == id)
                .findFirst();
    }

    @Override
    public List<ReservationSavedDto> findByDateAndThemeId(LocalDate date, long themeId) {
        return reservations.stream()
                .filter(reservation -> reservation.getDate().equals(date) && reservation.getThemeId() == themeId)
                .toList();
    }

    @Override // TODO: fake object test!
    public List<Long> findThemeIdByDateAndOrderByThemeIdCountAndLimit(LocalDate startDate, LocalDate endDate, int limit) {
        List<ReservationSavedDto> filteredReservations = findBetweenDates(startDate, endDate);
        Map<Long, Long> countOfThemeIds = countByThemeId(filteredReservations);
        return sortByCountAndLimit(countOfThemeIds);
    }

    private List<ReservationSavedDto> findBetweenDates(LocalDate startDate, LocalDate endDate) {
        return reservations.stream()
                .filter(reservation -> isBetweenDate(reservation.getDate(), startDate, endDate))
                .toList();
    }

    private boolean isBetweenDate(LocalDate target, LocalDate startDate, LocalDate endDate) {
        return target.isAfter(startDate) || target.isEqual(startDate)
                && target.isBefore(endDate) || target.isEqual(endDate);
    }

    private Map<Long, Long> countByThemeId(List<ReservationSavedDto> filteredReservations) {
        return filteredReservations.stream()
                .collect(Collectors.groupingBy(ReservationSavedDto::getThemeId, Collectors.counting()));
    }

    private List<Long> sortByCountAndLimit(Map<Long, Long> countOfThemeIds) {
        return countOfThemeIds.entrySet().stream()
                .sorted(Map.Entry.<Long, Long>comparingByValue().reversed())
                .limit(10)
                .map(Map.Entry::getKey)
                .toList();
    }

    @Override
    public void deleteById(long id) {
        ReservationSavedDto foundReservation = reservations.stream()
                .filter(reservation -> reservation.getId() == id)
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("아이디가 존재하지 않습니다."));
        reservations.remove(foundReservation);
    }

    @Override
    public Boolean isExistById(long id) {
        return reservations.stream()
                .anyMatch(reservation -> reservation.getId() == id);
    }

    @Override
    public Boolean isExistByTimeId(long timeId) {
        return reservations.stream()
                .anyMatch(reservation -> reservation.getTimeId() == timeId);
    }

    @Override
    public Boolean isExistByDateAndTimeId(LocalDate date, long timeId) {
        return reservations.stream()
                .anyMatch(reservation -> reservation.getDate().isEqual(date) && reservation.getTimeId() == timeId);
    }
}