
package mx.fei.gymmanagerapp.logic.employee;

import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Logger;
import java.util.logging.Level;
import mx.fei.gymmanagerapp.dataaccess.DatabaseManager;
import mx.fei.gymmanagerapp.logic.implementations.DAOException;
import mx.fei.gymmanagerapp.logic.implementations.Status;

public class EmployeeDAO implements IEmployee {
    
    private static final Logger LOG = Logger.getLogger(EmployeeDAO.class.getName());

    private boolean checkEmailDuplication(Employee employee) throws DAOException {
        Employee employeeAux = new Employee();
        int idEmployee = 0;

        try {
            employeeAux = getEmployeeByEmail(employee);
            idEmployee = employeeAux.getIdEmployee();
        } catch (DAOException exception) {
            throw new DAOException("No fue posible realizar la validacion, intente registrar mas tarde.", Status.ERROR);
        }
        if (idEmployee != employee.getIdEmployee() && idEmployee > 0) {
            throw new DAOException("El correo ya se encuentra registrado", Status.WARNING);
        }
        return false;
    }
    
    @Override
    public boolean isThereAtLeastOneEmployee() throws DAOException {
        boolean result = false;
        String statement = "SELECT EXISTS(SELECT 1 FROM empleado LIMIT 1) AS hay_registros;";
        DatabaseManager databaseManager = new DatabaseManager();
        
        try (Connection connection = databaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(statement);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            if (resultSet.next()) {
                result = resultSet.getBoolean("hay_registros");
            }
        } catch (SQLException exception) {
            LOG.log(Level.SEVERE, exception.getMessage(), exception);
            throw new DAOException("No fue posible verificar que haya empleados", Status.ERROR);
        }
        return result;
    }

    @Override
    public int registerEmployee(Employee employee) throws DAOException {
        int result = 0;
        if (!checkEmailDuplication(employee)) {
            result = insertEmployeeTransaction(employee);
        }
        return result;
    }

    @Override
    public int updateEmployee(Employee newEmployeeInformation) throws DAOException {
        int result = 0;
        if (!checkEmailDuplication(newEmployeeInformation)) {
            result = updateEmployeeTransaction(newEmployeeInformation);
        }
        return result;
    }

    @Override
    public int deleteEmployeeById(int idEmployee) throws DAOException {
        int result = -1;
        String statement = "DELETE FROM empleado WHERE idEmpleado = ?";
        DatabaseManager databaseManager = new DatabaseManager();
        
        try (Connection connection = databaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(statement)) {
            preparedStatement.setInt(1, idEmployee);
            result = preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            LOG.log(Level.SEVERE, exception.getMessage(), exception);
            throw new DAOException("No fue posible eliminar el empleado", Status.ERROR);
        }
        return result;
    }

    @Override
    public Employee getEmployeeByEmail(Employee employee) throws DAOException {
        Employee employeeAux = new Employee();
        DatabaseManager databaseManager = new DatabaseManager();
        String statement = "SELECT * FROM empleado WHERE correo = ?";
        
        try (Connection connection = databaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(statement)) {
            preparedStatement.setString(1, employee.getEmail());
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    employeeAux = initializeEmployeeFromResultSet(resultSet);
                }
            }
        } catch (SQLException exception) {
            LOG.log(Level.SEVERE, exception.getMessage(), exception);
            throw new DAOException("No fue posible obtener el empleado", Status.ERROR);
        }
        return employeeAux;
    }    
    
    @Override
    public Employee getEmployeeById(int idEmployee) throws DAOException {
        Employee employee = new Employee();
        DatabaseManager databaseManager = new DatabaseManager();
        String statement = "SELECT * FROM empleado WHERE idEmpleado = ?";
        
        try (Connection connection = databaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(statement)) {
            preparedStatement.setInt(1, idEmployee);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    employee = initializeEmployeeFromResultSet(resultSet);
                }
            }
        } catch (SQLException exception) {
            LOG.log(Level.SEVERE, exception.getMessage(), exception);
            throw new DAOException("No fue posible obtener el empleado", Status.ERROR);
        }
        return employee;
    }

    @Override
    public ArrayList<Employee> getEmployees() throws DAOException {
        ArrayList<Employee> employees = new ArrayList<>();
        DatabaseManager databaseManager = new DatabaseManager();
        String statement = "SELECT * FROM empleado";
        
        try (Connection connection = databaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(statement);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                Employee employee = initializeEmployeeFromResultSet(resultSet);
                employees.add(employee);
            }
        } catch (SQLException exception) {
            LOG.log(Level.SEVERE, exception.getMessage(), exception);
            throw new DAOException("No fue posible obtener los empleados", Status.ERROR);
        }
        return employees;
    }

    private int insertEmployeeTransaction(Employee employee) throws DAOException {
        int result = -1;
        String statement = "INSERT INTO empleado(nombre, apellidopaterno,"
                + " apellidomaterno, puesto, telefono, correo, contrasenia) VALUES(?, ?, ?, ?, ?, ?, ?);";
        DatabaseManager databaseManager = new DatabaseManager();
        
        try (Connection connection = databaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(statement, PreparedStatement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, employee.getName());
            preparedStatement.setString(2, employee.getPaternalSurname());
            preparedStatement.setString(3, employee.getMaternalSurname());
            preparedStatement.setString(4, employee.getPosition());
            preparedStatement.setString(5, employee.getPhoneNumber());
            preparedStatement.setString(6, employee.getEmail());
            preparedStatement.setString(7, employee.getPassword());
            preparedStatement.executeUpdate();            
            try (ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    result = resultSet.getInt(1);
                }
            }
        } catch (SQLException exception) {
            LOG.log(Level.SEVERE, exception.getMessage(), exception);
            throw new DAOException("No fue posible registrar el empleado", Status.ERROR);
        }
        return result;
    }

    private int updateEmployeeTransaction(Employee newEmployeeInformation) throws DAOException {
        int result = -1;
        String statement = "UPDATE empleado SET nombre = ?, apellidopaterno = ?,"
                + " apellidomaterno = ?, puesto = ?, telefono = ?, correo = ?, contrasenia = ? WHERE idEmpleado = ?";
        DatabaseManager databaseManager = new DatabaseManager();
        
        try (Connection connection = databaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(statement)) {
            preparedStatement.setString(1, newEmployeeInformation.getName());
            preparedStatement.setString(2, newEmployeeInformation.getPaternalSurname());
            preparedStatement.setString(3, newEmployeeInformation.getMaternalSurname());
            preparedStatement.setString(4, newEmployeeInformation.getPosition());
            preparedStatement.setString(5, newEmployeeInformation.getPhoneNumber());
            preparedStatement.setString(6, newEmployeeInformation.getEmail());
            preparedStatement.setString(7, newEmployeeInformation.getPassword());
            preparedStatement.setInt(8, newEmployeeInformation.getIdEmployee());
            result = preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            LOG.log(Level.SEVERE, exception.getMessage(), exception);
            throw new DAOException("No fue posible actualizar el empleado", Status.ERROR);
        }
        return result;
    }    
    
    
    private Employee initializeEmployeeFromResultSet(ResultSet resultSet) throws SQLException {
        Employee employee = new Employee();
        employee.setIdEmployee(resultSet.getInt("idEmpleado"));
        employee.setName(resultSet.getString("nombre"));
        employee.setPaternalSurname(resultSet.getString("apellidoPaterno"));
        employee.setMaternalSurname(resultSet.getString("apellidoMaterno"));
        employee.setPosition(resultSet.getString("puesto"));
        employee.setPhoneNumber(resultSet.getString("telefono"));
        employee.setEmail(resultSet.getString("correo"));
        employee.setPassword(resultSet.getString("contrasenia"));
        return employee;
    }    
    
}