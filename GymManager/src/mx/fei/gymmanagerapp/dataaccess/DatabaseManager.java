package mx.fei.gymmanagerapp.dataaccess;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import mx.fei.gymmanagerapp.logic.implementations.DAOException;
import mx.fei.gymmanagerapp.logic.implementations.Status;

public class DatabaseManager {

    private Connection connection;
    private static final String DATABASE_URL = "jdbc:mysql://127.0.0.1/gimnasiodb";
    private static final String DATABASE_USER = "root";
    private static final String DATABASE_PASSWORD = "MontielAcosta03";

    public DatabaseManager() {

    }

    public Connection getConnection() throws DAOException {
        try {
            if (connection == null || connection.isClosed()) {
                connection = connect();
            }
        } catch (SQLException exception) {
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, exception);
            throw new DAOException("No se pudo realizar la conexion a la base de datos", Status.FATAL);
        }
        return connection;
    }

    public Connection connect() throws SQLException {
        Connection newConnection = DriverManager.getConnection(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD);
        return newConnection;
    }

    public boolean closeConnection() {
        boolean isClosed = false;

        try {
            if (connection != null) {
                connection.close();
            }
            isClosed = true;
        } catch (SQLException exception) {
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, exception);
        }
        return isClosed;
    }
}
