package com.api.documentApp.service.email;

import com.api.documentApp.domain.DTO.email.EmailRequestDTO;
import com.api.documentApp.domain.DTO.email.EmailResponseDTO;
import com.api.documentApp.exception.document.DocumentNotFoundByIdException;
import com.api.documentApp.exception.user.NotEnoughRightsException;
import com.api.documentApp.exception.user.UserNotFoundByIdException;
import jakarta.mail.MessagingException;

public interface EmailService {
    public EmailResponseDTO sendEmailWithAttachment(EmailRequestDTO emailRequestDTO, String userNameFromAccess)
            throws UserNotFoundByIdException,
            DocumentNotFoundByIdException,
            NotEnoughRightsException, MessagingException;
}
