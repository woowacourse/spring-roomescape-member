package roomescape.time.controller;

import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import roomescape.time.dao.TimeDao;
import roomescape.time.entity.TimeEntity;

@RestController
@RequestMapping("/times")
public class TimeController {

    private TimeDao timeDao;

    public TimeController(TimeDao timeDao) {
        this.timeDao = timeDao;
    }

    @PostMapping
    public ResponseEntity<Void> createTime(
            @RequestBody TimeEntity timeEntity
    ){
        int timeId = timeDao.insert(timeEntity);
        return ResponseEntity.created(createUri(timeId)).build();
    }

    @GetMapping
    public ResponseEntity<List<TimeEntity>> getTimes(
    ){
        return ResponseEntity.ok().body(
                timeDao.findAllTimes()
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTime(
            @PathVariable Long id
    ){
        timeDao.delete(id);
        return ResponseEntity.noContent().build();
    }

    private URI createUri(int reservationId) {
        return ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(reservationId)
                .toUri();
    }
}
