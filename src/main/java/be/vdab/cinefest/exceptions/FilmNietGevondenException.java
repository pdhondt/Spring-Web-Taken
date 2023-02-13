package be.vdab.cinefest.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class FilmNietGevondenException extends RuntimeException {
    public FilmNietGevondenException(long id) {
        super("Film niet gevonden. Id: " + id);
    }
}
