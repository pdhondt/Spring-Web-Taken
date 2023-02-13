package be.vdab.cinefest.repositories;

import be.vdab.cinefest.domain.Film;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class FilmRepository {
    private final JdbcTemplate template;
    private final RowMapper<Film> filmMapper = (rs, rowNum) ->
            new Film(rs.getLong("id"), rs.getString("titel"),
                    rs.getInt("jaar"), rs.getInt("vrijePlaatsen"),
                    rs.getBigDecimal("aankoopprijs"));

    public FilmRepository(JdbcTemplate template) {
        this.template = template;
    }

    public long findTotaalVrijePlaatsen() {
        var sql = """
                select sum(vrijePlaatsen) as totaalVrijePlaatsen
                from films
                """;
        return template.queryForObject(sql, Long.class);
    }
    public Optional<Film> findById(long id) {
        try {
            var sql = """
                    select id, titel, jaar, vrijePlaatsen, aankoopprijs
                    from films
                    where id = ?
                    """;
            return Optional.of(template.queryForObject(sql, filmMapper, id));
        } catch (IncorrectResultSizeDataAccessException ex) {
            return Optional.empty();
        }
    }
}
