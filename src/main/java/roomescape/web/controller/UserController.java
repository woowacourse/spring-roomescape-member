package roomescape.web.controller;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import roomescape.web.dto.request.LoginRequest;

@RestController
@RequestMapping("/login")
public class UserController {
    @PostMapping
    public ResponseEntity<Void> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok().build();
    }
}
