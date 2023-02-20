package be.vdab.cinefest.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class OnvoldoendeVrijePlaatsenException extends RuntimeException {
    public OnvoldoendeVrijePlaatsenException(int vrijePlaatsen) {
        super("Er zijn maar " + vrijePlaatsen + " plaatsen beschikbaar");
    }
}
