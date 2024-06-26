package mx.fei.gymmanagerapp.logic.gymclass;

import mx.fei.gymmanagerapp.logic.implementations.DAOException;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import mx.fei.gymmanagerapp.dataaccess.DatabaseManager;
import mx.fei.gymmanagerapp.logic.employee.EmployeeDAO;
import mx.fei.gymmanagerapp.logic.implementations.Status;

/**
 * @author d0ubl3_d
 */
public class GymClassDAO implements IGymClass {
    
    public GymClassDAO() {
    }
    
    @Override
    public int registerGymClass(GymClass gymClass) throws DAOException {
        int result = -1;
        
        if (!checkGymClassDuplicate(gymClass)) {
            if (!checkCoachGymClassSchedules(gymClass)) {
                result = insertGymClass(gymClass);
            }            
        }
        return result;
    }
    
    private boolean checkGymClassDuplicate(GymClass gymClass) throws DAOException {
        GymClass auxGymClass = new GymClass();
        
        try {
            auxGymClass = getGymClassByName(gymClass.getName());
        } catch (DAOException exception) {
            throw new DAOException("No fue posible hacer la validación, intente más tarde", Status.WARNING);
        }
        if (auxGymClass.getIdGymClass() != gymClass.getIdGymClass() &&
        auxGymClass.getIdGymClass() > 0) {
            throw new DAOException("Ya hay una clase registrada con ese nombre", Status.WARNING);
        }
        return false;
    }
    
    private boolean checkCoachGymClassSchedules(GymClass gymClass) throws DAOException {
        ArrayList<GymClass> gymClasses = new ArrayList<>();
        String[] days = gymClass.getDays().split(", ");
        int idEmployee;
        
        try {
            idEmployee = gymClass.getCoach().getIdEmployee();
            for (String day: days) {
                gymClasses.addAll(getCoachScheduleGymClassesByDays(idEmployee, day));
            }                        
        } catch (DAOException exception) {
            throw new DAOException("No fue posible hacer la validación, intente más tarde", Status.WARNING);
        }
        for (GymClass auxGymClass : gymClasses) {
            if (isScheduleConflict(auxGymClass.getSchedule(), gymClass.getSchedule()) &&
            auxGymClass.getIdGymClass() != gymClass.getIdGymClass() &&                    
            auxGymClass.getIdGymClass() > 0) {
                throw new DAOException("El entrenador tiene una clase que causa conflicto con el horario", Status.WARNING);
            }
        }        
        return false;
    }
    
    private boolean isScheduleConflict(String schedule, String newSchedule) {
        boolean check;
        check = false;
        String[] gymClassSchedule = schedule.split("-");
        String[] newGymClassSchedule = newSchedule.split("-");
                
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        try {
            Date startGymClassSchedule = sdf.parse(gymClassSchedule[0].trim());
            Date endGymClassSchedule = sdf.parse(gymClassSchedule[1].trim());
            Date startNewGymClassSchedule = sdf.parse(newGymClassSchedule[0].trim());
            Date endNewGymClassSchedule = sdf.parse(newGymClassSchedule[1].trim());                        
            
            check = (startNewGymClassSchedule.compareTo(startGymClassSchedule) >= 0 &&
            startNewGymClassSchedule.compareTo(endGymClassSchedule) <= 0) ||
            (startGymClassSchedule.compareTo(startNewGymClassSchedule) >= 0 &&
            startGymClassSchedule.compareTo(endNewGymClassSchedule) <= 0);
        } catch (ParseException exception) {
            Logger.getLogger(GymClassDAO.class.getName()).log(Level.SEVERE, null, exception);
        }
        return check;
    }
    
    private int insertGymClass(GymClass gymClass) throws DAOException {
        DatabaseManager databaseManager = new DatabaseManager();        
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String statement = "INSERT INTO Clase (Empleado_idEmpleado,"
        + " nombre, descripcion, horario, dias, cupo)"
        + " VALUES (?,?,?,?,?,?)";
        int result = -1;
        
        try {
            connection = databaseManager.getConnection();
            preparedStatement = connection.prepareCall(statement);
            
            preparedStatement.setInt(1, gymClass.getCoach().getIdEmployee());
            preparedStatement.setString(2, gymClass.getName());
            preparedStatement.setString(3, gymClass.getDescription());
            preparedStatement.setString(4, gymClass.getSchedule());
            preparedStatement.setString(5, gymClass.getDays());                        
            preparedStatement.setInt(6, gymClass.getCapacity());
            
            preparedStatement.executeUpdate();
            resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                result = resultSet.getInt(1);
            }            
        } catch(SQLException exception) {
            Logger.getLogger(GymClassDAO.class.getName()).log(Level.SEVERE, null, exception);
            throw new DAOException("No fue posible registrar la clase", Status.ERROR);
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException exception){
                Logger.getLogger(GymClassDAO.class.getName()).log(Level.SEVERE, null, exception);
            }
        }
        return result;
    }
    
    @Override
    public GymClass getGymClassByIdGymClass(int idGymClass) throws DAOException {
        GymClass gymClass = new GymClass();
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String statement = "SELECT * FROM Clase"
        + " WHERE idClase = ?";
        
        try {
            connection = databaseManager.getConnection();
            preparedStatement = connection.prepareStatement(statement);
            
            preparedStatement.setInt(1,idGymClass);
            
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                gymClass = initializeGymClass(resultSet);
            }
        } catch(SQLException exception) {
            Logger.getLogger(GymClassDAO.class.getName()).log(Level.SEVERE, null, exception);
            throw new DAOException("No fue posible recuperar la clase", Status.ERROR);
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException exception){
                Logger.getLogger(GymClassDAO.class.getName()).log(Level.SEVERE, null, exception);
            }
        }
        return gymClass;
    }
    
    @Override
    public GymClass getGymClassByName(String name) throws DAOException {
        GymClass gymClass = new GymClass();
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String statement = "SELECT * FROM Clase"
        + " WHERE nombre = ?";
        
        try {
            connection = databaseManager.getConnection();
            preparedStatement = connection.prepareStatement(statement);
            
            preparedStatement.setString(1,name);
            
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                gymClass = initializeGymClass(resultSet);
            }
        } catch(SQLException exception) {
            Logger.getLogger(GymClassDAO.class.getName()).log(Level.SEVERE, null, exception);
            throw new DAOException("No fue posible recuperar la clase", Status.ERROR);
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException exception){
                Logger.getLogger(GymClassDAO.class.getName()).log(Level.SEVERE, null, exception);
            }
        }
        return gymClass;
    }
    
    @Override
    public ArrayList<GymClass> getGymClasses() throws DAOException {
        ArrayList<GymClass> gymClasses = new ArrayList<>();
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String statement = "SELECT * FROM Clase";
        
        try {
            connection = databaseManager.getConnection();
            preparedStatement = connection.prepareStatement(statement);
             
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                gymClasses.add(initializeGymClass(resultSet));
            }
        } catch(SQLException exception) {
            Logger.getLogger(GymClassDAO.class.getName()).log(Level.SEVERE, null, exception);
            throw new DAOException("No fue posible recuperar las clases", Status.ERROR);
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException exception){
                Logger.getLogger(GymClassDAO.class.getName()).log(Level.SEVERE, null, exception);
            }
        }
        return gymClasses;
    }    
    
    @Override
    public int updateGymClass(GymClass gymClass) throws DAOException {
        int result = -1;
        
        if (!checkGymClassDuplicate(gymClass)) {
            if (!checkCoachGymClassSchedules(gymClass)) {
                result = updateGymClassPrivate(gymClass);
            }            
        }
        return result;
    }
    
    private int updateGymClassPrivate(GymClass gymClass) throws DAOException {
        DatabaseManager databaseManager = new DatabaseManager();        
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String statement = "UPDATE Clase SET Empleado_idEmpleado = ?,"
        + " nombre = ?, descripcion = ?, horario = ?, dias = ?,"
        + " cupo = ? WHERE idClase = ?";
        int rowsAffected = -1;
        
        try {
            connection = databaseManager.getConnection();
            preparedStatement = connection.prepareCall(statement);
            
            preparedStatement.setInt(1, gymClass.getCoach().getIdEmployee());
            preparedStatement.setString(2, gymClass.getName());
            preparedStatement.setString(3, gymClass.getDescription());
            preparedStatement.setString(4, gymClass.getSchedule());
            preparedStatement.setString(5, gymClass.getDays());                        
            preparedStatement.setInt(6, gymClass.getCapacity());
            preparedStatement.setInt(7, gymClass.getIdGymClass());
            
            rowsAffected = preparedStatement.executeUpdate();
        } catch(SQLException exception) {
            Logger.getLogger(GymClassDAO.class.getName()).log(Level.SEVERE, null, exception);
            throw new DAOException("No fue posible actualizar la clase", Status.ERROR);
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException exception){
                Logger.getLogger(GymClassDAO.class.getName()).log(Level.SEVERE, null, exception);
            }
        }
        return rowsAffected;
    }
    
    @Override
    public int deleteGymClassByIdGymClass(int idGymClass) throws DAOException {
        int result = -1;
        
        if (getGymClassByIdGymClass(idGymClass).getIdGymClass() > 0) {
            result = deleteGymClassByIdGymClassPrivate(idGymClass);
        } else {
            throw new DAOException("La clase no existe", Status.WARNING);
        }
        return result;
    }
    
    private int deleteGymClassByIdGymClassPrivate(int idGymClass) throws DAOException {
        DatabaseManager databaseManager = new DatabaseManager();        
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String statement = "DELETE FROM Clase WHERE idClase = ?";
        int rowsAffected = -1;
        
        try {
            connection = databaseManager.getConnection();
            preparedStatement = connection.prepareCall(statement);
            
            preparedStatement.setInt(1, idGymClass);            
            
            rowsAffected = preparedStatement.executeUpdate();
        } catch(SQLException exception) {
            Logger.getLogger(GymClassDAO.class.getName()).log(Level.SEVERE, null, exception);
            throw new DAOException("No fue posible eliminar la clase", Status.ERROR);
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException exception){
                Logger.getLogger(GymClassDAO.class.getName()).log(Level.SEVERE, null, exception);
            }
        }
        return rowsAffected;
    }
    
    private GymClass initializeGymClass(ResultSet resultSet) throws DAOException {
        GymClass gymClass = new GymClass();
        EmployeeDAO employeeDAO = new EmployeeDAO();
        
        try {
            gymClass.setIdGymClass(resultSet.getInt("idClase"));
            gymClass.setName(resultSet.getString("nombre"));
            gymClass.setDescription(resultSet.getString("descripcion"));
            gymClass.setSchedule(resultSet.getString("horario"));
            gymClass.setDays(resultSet.getString("dias"));
            gymClass.setCapacity(resultSet.getInt("cupo"));
            gymClass.setCoach(employeeDAO.getEmployeeById(resultSet.getInt("Empleado_idEmpleado")));           
        } catch (SQLException exception) {
            Logger.getLogger(GymClassDAO.class.getName()).log(Level.SEVERE, null, exception);
        }                
        return gymClass;
    }
    
    private ArrayList<GymClass> getCoachScheduleGymClassesByDays(int idEmployee, String day) throws DAOException {
        ArrayList<GymClass> gymClasses = new ArrayList<>();
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String statement = "SELECT * FROM Clase"
        + " WHERE Empleado_idEmpleado = ? AND dias LIKE ?";
        
        try {
            connection = databaseManager.getConnection();
            preparedStatement = connection.prepareStatement(statement);
            
            
            preparedStatement.setInt(1,idEmployee);
            preparedStatement.setString(2, "%" + day + "%");
            
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                gymClasses.add(initializeGymClass(resultSet));
            }
        } catch(SQLException exception) {
            Logger.getLogger(GymClassDAO.class.getName()).log(Level.SEVERE, null, exception);
            throw new DAOException("No fue posible recuperar los horarios", Status.ERROR);
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException exception){
                Logger.getLogger(GymClassDAO.class.getName()).log(Level.SEVERE, null, exception);
            }
        }
        return gymClasses;
    }
}