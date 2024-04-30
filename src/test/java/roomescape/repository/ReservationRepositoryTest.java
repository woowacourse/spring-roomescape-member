package roomescape.repository;

interface ReservationRepositoryTest {

    void save();

    void findAllByDateAndTime();

    void findAll();

    void findById();

    void findByNotExistingId();

    void deleteById();
}
