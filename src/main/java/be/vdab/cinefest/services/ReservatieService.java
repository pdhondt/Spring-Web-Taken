package be.vdab.cinefest.services;

import be.vdab.cinefest.dto.ReservatieMetFilmTitel;
import be.vdab.cinefest.repositories.ReservatieRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ReservatieService {
    private final ReservatieRepository reservatieRepository;

    public ReservatieService(ReservatieRepository reservatieRepository) {
        this.reservatieRepository = reservatieRepository;
    }
    public List<ReservatieMetFilmTitel> findByEmailAdres(String emailAdres) {
        return reservatieRepository.findByEmailAdres(emailAdres);
    }
}
