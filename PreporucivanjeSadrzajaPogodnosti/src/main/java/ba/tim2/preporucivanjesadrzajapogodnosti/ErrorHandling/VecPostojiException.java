package ba.tim2.preporucivanjesadrzajapogodnosti.ErrorHandling;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class VecPostojiException extends RuntimeException {
    public VecPostojiException(String exception) {
        super(exception);
    }
}
