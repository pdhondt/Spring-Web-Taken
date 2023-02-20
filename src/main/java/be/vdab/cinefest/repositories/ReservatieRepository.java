package be.vdab.cinefest.repositories;

import be.vdab.cinefest.domain.Reservatie;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;

@Repository
public class ReservatieRepository {
    private final JdbcTemplate template;

    public ReservatieRepository(JdbcTemplate template) {
        this.template = template;
    }
    public long create(Reservatie reservatie) {
        var sql = """
                insert into reservaties(filmId, emailAdres, plaatsen, besteld)
                values (?, ?, ?, ?)
                """;
        var keyHolder = new GeneratedKeyHolder();
        template.update(connection -> {
            var statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            statement.setLong(1, reservatie.getFilmId());
            statement.setString(2, reservatie.getEmailAdres());
            statement.setInt(3, reservatie.getPlaatsen());
            statement.setObject(4, reservatie.getBesteld());
            return statement;
        }, keyHolder);
        return keyHolder.getKey().longValue();
    }
}
