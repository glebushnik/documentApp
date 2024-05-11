package com.api.documentApp.service.email.impl;

import com.api.documentApp.domain.DTO.email.EmailRequestDTO;
import com.api.documentApp.domain.DTO.email.EmailResponseDTO;
import com.api.documentApp.domain.enums.Role;
import com.api.documentApp.domain.mapper.document.DocumentResponseMapper;
import com.api.documentApp.exception.document.DocumentNotFoundByIdException;
import com.api.documentApp.exception.user.NotEnoughRightsException;
import com.api.documentApp.exception.user.UserNotFoundByIdException;
import com.api.documentApp.repo.document.DocumentRepo;
import com.api.documentApp.repo.user.UserRepo;
import com.api.documentApp.service.document.DocumentService;
import com.api.documentApp.service.email.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender javaMailSender;
    private final UserRepo userRepo;
    private final DocumentRepo documentRepo;
    private final DocumentService documentService;
    private final DocumentResponseMapper documentResponseMapper;
    @Override
    public EmailResponseDTO sendEmailWithAttachment(EmailRequestDTO emailRequestDTO, String userNameFromAccess) throws UserNotFoundByIdException, DocumentNotFoundByIdException, NotEnoughRightsException, MessagingException {

        var doc = documentRepo.findById(emailRequestDTO.getDocId()).orElseThrow(
                ()-> new DocumentNotFoundByIdException(String.format("Документ с id : %s не найден.", emailRequestDTO.getDocId()))
        );

        var currentUser = userRepo.findByEmail(userNameFromAccess).get();
        var groupDocs = documentResponseMapper.toEntity(documentService.getGroupDocs(userNameFromAccess));
        if(
                currentUser.getRole()== Role.ADMIN
                || groupDocs.contains(doc)
        ) {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message, true);
            mimeMessageHelper.setFrom("document.app.help@gmail.com");
            mimeMessageHelper.setTo(emailRequestDTO.getEmail());
            mimeMessageHelper.setSubject(String.format(
                    "Письмо от %s %s %s (%s). Тема: " + emailRequestDTO.getHeader(),
                    currentUser.getLastName(),
                    currentUser.getFirstName(),
                    currentUser.getPatronymic(),
                    currentUser.getEmail()
            ));
            mimeMessageHelper.setText(emailRequestDTO.getBody());
            ByteArrayResource resource = new ByteArrayResource(doc.getData());
            mimeMessageHelper.addAttachment(doc.getFileName(), resource);
            javaMailSender.send(message);
            return EmailResponseDTO.builder()
                    .emailReceiver(emailRequestDTO.getEmail())
                    .docId(doc.getId())
                    .sentTime(Instant.now())
                    .build();
        } else {
            throw new NotEnoughRightsException("Недостаточно прав.");
        }
    }
}
