package phonebook.presentation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import phonebook.presentation.errors.Errors;
import phonebook.presentation.errors.ErrorsMapper;
import phonebook.presentation.errors.InvalidRequestException;

/**
 * @author Konstantin Kosmachevskiy
 */
@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    @Autowired
    private ErrorsMapper mapper;

    @ExceptionHandler({InvalidRequestException.class})
    protected ResponseEntity<Object> handleInvalidRequest(RuntimeException e, WebRequest request) {

        InvalidRequestException ire = (InvalidRequestException) e;

        Errors errors = mapper.map(ire.getErrors().getFieldErrors());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return handleExceptionInternal(e, errors, headers, HttpStatus.UNPROCESSABLE_ENTITY, request);
    }
}