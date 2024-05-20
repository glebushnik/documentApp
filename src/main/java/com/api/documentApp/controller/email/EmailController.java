package com.api.documentApp.controller.email;

import com.api.documentApp.domain.DTO.email.EmailRequestDTO;
import com.api.documentApp.domain.DTO.email.EmailResponseDTO;
import com.api.documentApp.security.JwtService;
import com.api.documentApp.service.email.EmailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
@SecurityRequirement(name = "Bearer Authentication")
public class EmailController {
    private final JwtService jwtService;
    private final EmailService emailService;

    @PostMapping("/send-message")
    @Operation(
            summary = "Send Email",
            description = "Send an email with several attachments. The request body should be an EmailRequestDTO object with receiverId, documentIds and body of message. The email is sent on behalf of the authenticated user.",
            tags = { "documents", "email", "post"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Email sent successfully", content = { @Content(schema = @Schema(implementation = EmailResponseDTO.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = { @Content(schema = @Schema(implementation = String.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = { @Content(schema = @Schema(implementation = String.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = { @Content(schema = @Schema(implementation = String.class), mediaType = "application/json") }) })
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
