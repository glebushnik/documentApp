package com.api.documentApp.service.email.impl;

import com.api.documentApp.domain.DTO.email.EmailRequestDTO;
import com.api.documentApp.domain.DTO.email.EmailResponseDTO;
import com.api.documentApp.domain.enums.Role;
import com.api.documentApp.exception.document.DocumentNotFoundByIdException;
import com.api.documentApp.exception.user.NotEnoughRightsException;
import com.api.documentApp.exception.user.UserNotFoundByIdException;
import com.api.documentApp.repo.document.DocumentRepo;
import com.api.documentApp.repo.user.UserRepo;
import com.api.documentApp.service.email.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender javaMailSender;
    private final UserRepo userRepo;
    private final DocumentRepo documentRepo;
    @Override
    public EmailResponseDTO sendEmailWithAttachment(EmailRequestDTO emailRequestDTO, String userNameFromAccess) throws UserNotFoundByIdException, DocumentNotFoundByIdException, NotEnoughRightsException, MessagingException {

        var recevier = userRepo.findById(emailRequestDTO.getReceiverId()).orElseThrow(
                ()-> new UserNotFoundByIdException(String.format("Пользователь с id : %d не найден.", emailRequestDTO.getReceiverId()))
        );

        var doc = documentRepo.findById(emailRequestDTO.getDocId()).orElseThrow(
                ()-> new DocumentNotFoundByIdException(String.format("Документ с id : %s не найден.", emailRequestDTO.getDocId()))
        );

        var currentUser = userRepo.findByEmail(userNameFromAccess).get();
        var isGroupMember = recevier.getUserGroup()!=null &&
                recevier.getUserGroup().getUsers().contains(currentUser);
        if(
                currentUser.getRole()== Role.ADMIN
                || currentUser == recevier
                || isGroupMember
        ) {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message, true);
            mimeMessageHelper.setFrom("document.app.help@gmail.com");
            mimeMessageHelper.setTo(recevier.getEmail());
            mimeMessageHelper.setSubject(String.format(
                    "Письмо от %s.", currentUser.getEmail()
            ));
            mimeMessageHelper.setText(emailRequestDTO.getBody());
            ByteArrayResource resource = new ByteArrayResource(doc.getData());
            mimeMessageHelper.addAttachment(doc.getFileName(), resource);
            javaMailSender.send(message);
            return EmailResponseDTO.builder()
                    .emailReceiver(recevier.getEmail())
                    .docId(doc.getId())
                    .build();
        } else {
            throw new NotEnoughRightsException("Недостаточно прав.");
        }
    }
}
