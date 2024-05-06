package com.api.documentApp.controller.email;

import com.api.documentApp.domain.DTO.email.EmailRequestDTO;
import com.api.documentApp.security.JwtService;
import com.api.documentApp.service.email.EmailService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/mailer")
public class EmailController {
    private final JwtService jwtService;
    private final EmailService emailService;

    @PostMapping("/send-message")
    public ResponseEntity<?> sendEmail(
            @RequestBody EmailRequestDTO emailRequestDTO,
            HttpServletRequest request
    ) {
        try {
            String authorizationHeader = request.getHeader("Authorization");
            String token = authorizationHeader.substring(7);
            String usernameFromAccess = jwtService.extractUserName(token);
            return ResponseEntity.ok().body(
              emailService.sendEmailWithAttachment(
                      emailRequestDTO,
                      usernameFromAccess
              )
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
