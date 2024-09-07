package by.alst.jbdc.dao;

import by.alst.jbdc.entity.Technology;
import by.alst.jbdc.exception.DaoException;
import by.alst.jbdc.utils.ConnectionManager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TechnologyDao implements Dao<String, Technology> {

    private static final TechnologyDao INSTANCE = new TechnologyDao();

    private TechnologyDao() {
    }

    public static TechnologyDao getInstance() {
        return INSTANCE;
    }

    private final static String UPDATE_SQL = """
            UPDATE technology
            SET technology_description = ?
            WHERE technology_name = ?
            """;

    private final static String FIND_ALL_SQL = """
            SELECT technology_name, technology_description
            FROM technology
            """;

    private final static String FIND_BY_NAME_SQL = FIND_ALL_SQL + """
            WHERE technology_name = ?
            """;


    private final static String SAVE_SQL = """
            INSERT INTO technology(technology_name, technology_description)
            VALUES (?, ?)
            """;

    private final static String DELETE_SQL = """
            DELETE FROM technology
            WHERE technology_name = ?
            """;

    private final static String DELETE_SQL_LIST = """
            DELETE FROM technology
            """;

    @Override
    public boolean update(Technology technology) {
        boolean updateResult = false;
        if (technology != null) {
            var technologyName = Optional.ofNullable(technology.getTechnologyName()).isPresent()
                    ? technology.getTechnologyName().trim() : "";
            var technologyDescription = Optional.ofNullable(technology.getTechnologyDescription()).isPresent()
                    ? technology.getTechnologyDescription().trim() : "";
            if (!technologyName.isEmpty() & !technologyDescription.isEmpty()) {
                try (var connection = ConnectionManager.get();
                     var statement = connection.prepareStatement(UPDATE_SQL)) {
                    statement.setString(1, technology.getTechnologyDescription());
                    statement.setString(2, technology.getTechnologyName());

                    updateResult = statement.executeUpdate() > 0;
                } catch (SQLException e) {
                    throw new DaoException(e);
                }
            }
        }
        return updateResult;
    }

    @Override
    public Optional<Technology> findByName(String technologyName) {
        try (var connection = ConnectionManager.get();
             var statement = connection.prepareStatement(FIND_BY_NAME_SQL)) {
            statement.setString(1, technologyName);
            Technology technology = null;

            var result = statement.executeQuery();
            while (result.next()) {
                technology = buildTechnology(result);
            }
            return Optional.ofNullable(technology);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public List<Technology> findAll() {
        try (var connection = ConnectionManager.get()) {
            return findTechnologyList(connection);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    public List<Technology> findTechnologyList(Connection connection) {
        try (var statement = connection.prepareStatement(FIND_ALL_SQL)) {
            List<Technology> technologyList = new ArrayList<>();

            var result = statement.executeQuery();
            while (result.next()) {
                technologyList.add(
                        buildTechnology(result)
                );
            }
            return technologyList;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    private Technology buildTechnology(ResultSet result) throws SQLException {
        return new Technology(result.getString("technology_name"),
                result.getString("technology_description"));
    }

    @Override
    public Technology save(Technology technology) {
        if (technology != null) {
            var technologyName = Optional.ofNullable(technology.getTechnologyName()).isPresent()
                    ? technology.getTechnologyName().trim() : "";
            var technologyDescription = Optional.ofNullable(technology.getTechnologyDescription()).isPresent()
                    ? technology.getTechnologyDescription().trim() : "";
            if (!technologyName.isEmpty() & !technologyDescription.isEmpty()
                & !findAll().stream().map(Technology::getTechnologyName).toList().contains(technologyName)) {
                try (var connection = ConnectionManager.get();
                     var statement = connection.prepareStatement(SAVE_SQL)) {
                    statement.setString(1, technology.getTechnologyName());
                    statement.setString(2, technology.getTechnologyDescription());

                    statement.executeUpdate();
                    technology = findByName(technology.getTechnologyName()).orElse(new Technology());
                } catch (SQLException e) {
                    throw new DaoException(e);
                }
            } else {
                technology = new Technology();
            }
        } else {
            technology = new Technology();
        }
        return technology;
    }

    @Override
    public boolean delete(String technologyName) {
        try (var connection = ConnectionManager.get();
             var statement = connection.prepareStatement(DELETE_SQL)) {
            statement.setString(1, technologyName);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    public void deleteTechnologyList(Connection connection) {
        try (var statement = connection.prepareStatement(DELETE_SQL_LIST)) {
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }
}
