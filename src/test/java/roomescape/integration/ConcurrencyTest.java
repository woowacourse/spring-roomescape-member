package roomescape.integration;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.integration.support.DatabaseHelper;
import roomescape.integration.support.SpringWebTest;

@SpringWebTest
class ConcurrencyTest {

    @Autowired
    DatabaseHelper databaseHelper;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setup() {
        databaseHelper.clear();
    }

    @DisplayName("동일한 예약 요청이 동시에 들어오면 하나만 성공하고 나머지는 중복 예외가 발생한다")
    @Test
    void makeReservation_concurrent_duplicate() throws InterruptedException {
        //given
        createReservationTime("10:00");
        createTheme("동시성 테마", "설명", "thumbnailUrl");

        int threadCount = 20;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch readyLatch = new CountDownLatch(threadCount);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch doneLatch = new CountDownLatch(threadCount);

        AtomicInteger successCount = new AtomicInteger();
        AtomicInteger duplicateCount = new AtomicInteger();

        //when
        for (int i = 0; i < threadCount; i++) {
            int index = i;
            executor.submit(() -> {
                readyLatch.countDown();
                try {
                    startLatch.await();

                    Map<String, Object> request = new HashMap<>();
                    request.put("name", "user-" + index);
                    request.put("date", LocalDate.of(2026, 5, 10).toString());
                    request.put("timeId", 1L);
                    request.put("themeId", 1L);

                    Response response = RestAssured.given()
                            .contentType(ContentType.JSON)
                            .body(request)
                            .when().post("/reservations");

                    if (response.statusCode() == 201) {
                        successCount.incrementAndGet();
                    } else if (response.statusCode() == 409) {
                        duplicateCount.incrementAndGet();
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    doneLatch.countDown();
                }
            });
        }

        readyLatch.await();
        startLatch.countDown();
        doneLatch.await(10, TimeUnit.SECONDS);
        executor.shutdown();


        //then
        Integer savedCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM reservation", Integer.class);

        assertThat(successCount.get()).isEqualTo(1);
        assertThat(duplicateCount.get()).isEqualTo(threadCount - 1);
        assertThat(savedCount).isEqualTo(1);
    }

    @DisplayName("동일한 예약 시간을 동시에 생성하면 하나만 성공하고 나머지는 중복 예외가 발생한다")
    @Test
    void registerTime_concurrent_duplicate() throws InterruptedException {
        //given
        int threadCount = 20;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch readyLatch = new CountDownLatch(threadCount);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch doneLatch = new CountDownLatch(threadCount);

        AtomicInteger successCount = new AtomicInteger();
        AtomicInteger duplicateCount = new AtomicInteger();

        //when
        for (int i = 0; i < threadCount; i++) {
            executor.submit(() -> {
                readyLatch.countDown();
                try {
                    startLatch.await();

                    Map<String, Object> request = new HashMap<>();
                    request.put("startAt", "10:00");

                    Response response = RestAssured.given()
                            .contentType(ContentType.JSON)
                            .body(request)
                            .when().post("/admin/times");

                    if (response.statusCode() == 201) {
                        successCount.incrementAndGet();
                    } else if (response.statusCode() == 409) {
                        duplicateCount.incrementAndGet();
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    doneLatch.countDown();
                }
            });
        }

        readyLatch.await();
        startLatch.countDown();
        doneLatch.await(10, TimeUnit.SECONDS);
        executor.shutdown();

        //then
        Integer savedCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM reservation_time", Integer.class);

        assertThat(successCount.get()).isEqualTo(1);
        assertThat(duplicateCount.get()).isEqualTo(threadCount - 1);
        assertThat(savedCount).isEqualTo(1);
    }

    @DisplayName("서로 다른 예약을 같은 슬롯으로 동시에 수정하면 하나만 성공하고 하나는 중복 예외가 발생한다")
    @Test
    void updateReservation_concurrent_duplicate() throws InterruptedException {
        //given
        createReservationTime("10:00");
        createReservationTime("11:00");
        createReservationTime("12:00");
        createTheme("동시성 테마", "설명", "thumbnailUrl");

        Long reservationId1 = createReservation("user-a", LocalDate.of(2026, 5, 10), 1L, 1L);
        Long reservationId2 = createReservation("user-b", LocalDate.of(2026, 5, 10), 2L, 1L);

        List<UpdateTask> tasks = List.of(
                new UpdateTask(reservationId1, "user-a"),
                new UpdateTask(reservationId2, "user-b")
        );

        ExecutorService executor = Executors.newFixedThreadPool(tasks.size());
        CountDownLatch readyLatch = new CountDownLatch(tasks.size());
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch doneLatch = new CountDownLatch(tasks.size());

        AtomicInteger successCount = new AtomicInteger();
        AtomicInteger duplicateCount = new AtomicInteger();
        List<Integer> unexpectedStatuses = new ArrayList<>();

        //when
        for (UpdateTask task : tasks) {
            executor.submit(() -> {
                readyLatch.countDown();
                try {
                    startLatch.await();

                    Map<String, Object> request = new HashMap<>();
                    request.put("date", LocalDate.of(2026, 5, 10).toString());
                    request.put("timeId", 3L);

                    Response response = RestAssured.given()
                            .header("Authorization", task.authorizationName())
                            .contentType(ContentType.JSON)
                            .body(request)
                            .when().patch("/reservations/" + task.reservationId());

                    int statusCode = response.statusCode();
                    if (statusCode == 204) {
                        successCount.incrementAndGet();
                    } else if (statusCode == 409) {
                        duplicateCount.incrementAndGet();
                    } else {
                        synchronized (unexpectedStatuses) {
                            unexpectedStatuses.add(statusCode);
                        }
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    doneLatch.countDown();
                }
            });
        }

        readyLatch.await();
        startLatch.countDown();
        doneLatch.await(10, TimeUnit.SECONDS);
        executor.shutdown();

        //then
        Integer targetSlotCount = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM reservation WHERE reservation_date = ? AND time_id = ? AND theme_id = ?",
                Integer.class,
                LocalDate.of(2026, 5, 10),
                3L,
                1L
        );

        assertThat(unexpectedStatuses).isEmpty();
        assertThat(successCount.get()).isEqualTo(1);
        assertThat(duplicateCount.get()).isEqualTo(1);
        assertThat(targetSlotCount).isEqualTo(1);
    }

    private void createReservationTime(String startAt) {
        Map<String, Object> reservationTime = new HashMap<>();
        reservationTime.put("startAt", startAt);

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(reservationTime)
                .when().post("/admin/times")
                .then().statusCode(201);
    }

    private void createTheme(String name, String description, String thumbnailUrl) {
        Map<String, Object> theme = new HashMap<>();
        theme.put("name", name);
        theme.put("description", description);
        theme.put("thumbnailUrl", thumbnailUrl);

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(theme)
                .when().post("/admin/themes")
                .then().statusCode(201);
    }

    private Long createReservation(String name, LocalDate date, Long timeId, Long themeId) {
        Map<String, Object> reservation = new HashMap<>();
        reservation.put("name", name);
        reservation.put("date", date.toString());
        reservation.put("timeId", timeId);
        reservation.put("themeId", themeId);

        return RestAssured.given()
                .contentType(ContentType.JSON)
                .body(reservation)
                .when().post("/reservations")
                .then().statusCode(201)
                .extract()
                .jsonPath()
                .getLong("id");
    }

    private record UpdateTask(Long reservationId, String authorizationName) {
    }
}
