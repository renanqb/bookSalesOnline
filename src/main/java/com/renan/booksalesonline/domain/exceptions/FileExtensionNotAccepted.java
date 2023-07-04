package com.renan.booksalesonline.domain.exceptions;

import com.renan.booksalesonline.domain.enums.ImageExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.NOT_ACCEPTABLE, reason="File extension not accepeted")
public class FileExtensionNotAccepted extends Exception {

    public FileExtensionNotAccepted() {

        super(String.format(
                "Extension is not accepted as valid. Values are %s.",
                ImageExtension.getExtensionAcceptedList()
        ));
    }
}
