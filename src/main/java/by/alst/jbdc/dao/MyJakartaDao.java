package by.alst.jbdc.dao;

import by.alst.jbdc.entity.MyJakarta;
import by.alst.jbdc.entity.Technology;
import by.alst.jbdc.exception.DaoException;
import by.alst.jbdc.utils.ConnectionManager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class MyJakartaDao {

    private static final MyJakartaDao INSTANCE = new MyJakartaDao();

    private MyJakartaDao() {
    }

    public static MyJakartaDao getInstance() {
        return INSTANCE;
    }

    private final TechnologyDao technologyDao = TechnologyDao.getInstance();

    private final static String UPDATE_SQL = """
            UPDATE my_jakarta
            SET description = ?
            WHERE version = ?
            """;

    private final static String FIND_ALL_SQL = """
            SELECT version, description
            FROM my_jakarta
            """;


    private final static String SAVE_SQL = """
            INSERT INTO my_jakarta(version, description)
            VALUES (?, ?)
            """;

    private final static String DELETE_SQL = """
            DELETE FROM my_jakarta
            """;

    public MyJakarta writeMyJakarta(MyJakarta myJakarta) {

        if (myJakarta != null) {
            var myJakartaVersion = Optional.ofNullable(myJakarta.getVersion()).isPresent()
                    ? myJakarta.getVersion().trim() : "";
            var myJakartaDescription = Optional.ofNullable(myJakarta.getDescription()).isPresent()
                    ? myJakarta.getDescription().trim() : "";
            var myJakartaTechnologyList = Optional.ofNullable(myJakarta.getTechnologyList()).isPresent()
                    ? myJakarta.getTechnologyList() : new ArrayList<>();
            if (!myJakartaVersion.isEmpty() & !myJakartaDescription.isEmpty()
                & !myJakartaTechnologyList.isEmpty()) {
                try (var connection = ConnectionManager.get();
                     var statement = connection.prepareStatement(SAVE_SQL)) {
                    if (myJakartaTechnologyList.stream().noneMatch(Objects::nonNull) |
                        myJakartaTechnologyList.stream().noneMatch(s -> s.equals(new Technology()))) {
                        deleteMyJakarta();
                        statement.setString(1, myJakartaVersion);
                        statement.setString(2, myJakartaDescription);
                        statement.executeUpdate();
                        myJakartaTechnologyList.forEach(t -> technologyDao.save((Technology) t));
                        myJakarta = readMyJakarta();
                    } else {
                        myJakarta = new MyJakarta();
                    }

                } catch (SQLException e) {
                    throw new DaoException(e);
                }
            } else {
                myJakarta = new MyJakarta();
            }
        } else {
            myJakarta = new MyJakarta();
        }
        return myJakarta;
    }

    public MyJakarta readMyJakarta() {
        try (var connection = ConnectionManager.get();
             var statement = connection.prepareStatement(FIND_ALL_SQL)) {
            MyJakarta myJakarta = null;

            var result = statement.executeQuery();
            while (result.next()) {
                myJakarta = new MyJakarta(result.getString("version"),
                        result.getString("description"),
                        technologyDao.findTechnologyList(connection));
            }
            myJakarta = Optional.ofNullable(myJakarta).isPresent() ? myJakarta : new MyJakarta();
            return myJakarta;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    public boolean updateTechnology(Technology technology) {
        boolean updateResult = false;
        if (technology != null) {
            List<String> technologyNameList = technologyDao.findAll().stream()
                    .map(Technology::getTechnologyName).toList();
            if (technologyNameList.contains(technology.getTechnologyName())) {
                updateResult = technologyDao.update(technology);
            } else {
                updateResult = !technologyDao.save(technology).equals(new Technology());
            }
        }
        return updateResult;
    }

    public void deleteMyJakarta() {
        try (var connection = ConnectionManager.get();
             var statement = connection.prepareStatement(DELETE_SQL)) {
            technologyDao.deleteTechnologyList(connection);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }
}
