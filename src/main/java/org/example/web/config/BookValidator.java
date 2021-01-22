package org.example.web.config;

import org.example.web.dto.BookPattern;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class BookValidator implements Validator {
    @Override
    public boolean supports(Class clazz) {
        return BookPattern.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        BookPattern bookIPattern = (BookPattern) target;

        if (bookIPattern.getAuthorPattern().isEmpty() &&
                bookIPattern.getTitlePattern().isEmpty() &&
                bookIPattern.getSizePattern().isEmpty()) {
            errors.rejectValue("authorPattern", "authorPattern[emptyMessage]");
            errors.rejectValue("titlePattern", "titlePattern[emptyMessage]");
            errors.rejectValue("sizePattern", "sizePattern[emptyMessage]");
        }

    }
}
