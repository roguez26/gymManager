package unit.test.EmployeeDAO;

import java.util.logging.Level;
import java.util.logging.Logger;
import mx.fei.gymmanagerapp.logic.employee.Employee;
import mx.fei.gymmanagerapp.logic.employee.EmployeeDAO;
import mx.fei.gymmanagerapp.logic.implementations.DAOException;
import org.junit.Assert;
import org.junit.Test;

public class TestEmployeeDAO {

    private static final EmployeeDAO TEST_EMPLOYEE_DAO = new EmployeeDAO();
    private static final Employee TEST_EMPLOYEE = new Employee();
    private static final Employee AUX_TEST_EMPLOYEE = new Employee();
    
    @Test
    public void testAuthenticateEmployee() {
        Employee authenticatedMember = new Employee();
        int idMember = 0;
        initializeSuccesTestEmployee();
        try {
            idMember =TEST_EMPLOYEE_DAO.registerEmployee(TEST_EMPLOYEE);
            authenticatedMember = TEST_EMPLOYEE_DAO.authenticateEmployee(TEST_EMPLOYEE.getEmail(), "Papoi123!");
            
            TEST_EMPLOYEE_DAO.deleteEmployeeById(idMember);
            
            
        } catch (DAOException exception) {
            Logger.getLogger(TestEmployeeDAO.class.getName()).log(Level.SEVERE, null, exception);
        }
        Assert.assertTrue(authenticatedMember.getIdEmployee() > 0);
    }

    @Test
    public void testSuccessRegisterEmployee() {
        int idEmployee = 0;
        initializeSuccesTestEmployee();
        try {
            idEmployee = TEST_EMPLOYEE_DAO.registerEmployee(TEST_EMPLOYEE);
            TEST_EMPLOYEE_DAO.deleteEmployeeById(idEmployee);
        } catch (DAOException exception) {
            Logger.getLogger(TestEmployeeDAO.class.getName()).log(Level.SEVERE, null, exception);
        }
        Assert.assertTrue(idEmployee > 0);
    }

    @Test
    public void testInvalidCharactersRegisterEmployee() {
        int idEmployee = 0;
        initializeInvalidCharactersTestEmployee();
        try {
            idEmployee = TEST_EMPLOYEE_DAO.registerEmployee(TEST_EMPLOYEE);
            TEST_EMPLOYEE_DAO.deleteEmployeeById(idEmployee);
        } catch (DAOException exception) {
            Logger.getLogger(TestEmployeeDAO.class.getName()).log(Level.SEVERE, null, exception);
        }
        Assert.assertTrue(idEmployee > 0);
    }

    @Test
    public void testValidNullFieldsRegisterEmployee() {
        int idEmployee = 0;
        initializeValidNullFieldsTestEmployee();
        try {
            idEmployee = TEST_EMPLOYEE_DAO.registerEmployee(TEST_EMPLOYEE);
            TEST_EMPLOYEE_DAO.deleteEmployeeById(idEmployee);
        } catch (DAOException exception) {
            Logger.getLogger(TestEmployeeDAO.class.getName()).log(Level.SEVERE, null, exception);
        }
        Assert.assertTrue(idEmployee > 0);
    }

    @Test
    public void testInvalidNullFieldsRegisterEmployee() {
        int idEmployee = 0;
        initializeInvalidNullFieldsTestEmployee();
        try {
            idEmployee = TEST_EMPLOYEE_DAO.registerEmployee(TEST_EMPLOYEE);
            TEST_EMPLOYEE_DAO.deleteEmployeeById(idEmployee);
        } catch (DAOException exception) {
            Logger.getLogger(TestEmployeeDAO.class.getName()).log(Level.SEVERE, null, exception);
        }
        Assert.assertTrue(idEmployee > 0);
    }

    @Test
    public void testEmailAlreadyRegisteredRegisterEmployee() {
        int idEmployee = 0;
        int idAuxEmployee = 0;
        initializeSuccesTestEmployee();
        initializeAuxTestEmployee();

        try {
            idEmployee = TEST_EMPLOYEE_DAO.registerEmployee(TEST_EMPLOYEE);
            idAuxEmployee = TEST_EMPLOYEE_DAO.registerEmployee(AUX_TEST_EMPLOYEE);
            TEST_EMPLOYEE_DAO.deleteEmployeeById(idEmployee);
            TEST_EMPLOYEE_DAO.deleteEmployeeById(idAuxEmployee);
        } catch (DAOException exception) {
            Logger.getLogger(TestEmployeeDAO.class.getName()).log(Level.SEVERE, null, exception);
        }
        Assert.assertTrue(idAuxEmployee > 0);
    }
    
    @Test
    public void testSuccessUpdateEmployee() {
        int idTestEmployee = 0;
        int rowsAffected = 0;
        initializeSuccesTestEmployee();
        Employee employee = new Employee();
        employee.setName("Isaac");
        employee.setPaternalSurname("Avila");
        employee.setMaternalSurname("Tlaxcalteco");
        employee.setPosition("Entrenador");
        employee.setEmail("iat17@gmail.com");
        employee.setPhoneNumber("2281278263");
        employee.setPassword("Papoi123!");        
        try {
            idTestEmployee = TEST_EMPLOYEE_DAO.registerEmployee(TEST_EMPLOYEE);
            TEST_EMPLOYEE.setIdEmployee(idTestEmployee);
            employee.setIdEmployee(idTestEmployee);
            rowsAffected = TEST_EMPLOYEE_DAO.updateEmployee(employee);
            TEST_EMPLOYEE_DAO.deleteEmployeeById(idTestEmployee);
        } catch(DAOException exception) {
            Logger.getLogger(TestEmployeeDAO.class.getName()).log(Level.SEVERE, null, exception);
        }
        Assert.assertTrue(rowsAffected > 0);
        
    }
    
    @Test
    public void testEmailAlreadyRegisteredUpdateEmployee() {
        int idTestEmployee = 0;
        int rowsAffected = 0;
        initializeSuccesTestEmployee();
        Employee employee = new Employee();
        employee.setName("Isaac");
        employee.setPaternalSurname("Avila");
        employee.setMaternalSurname("Tlaxcalteco");
        employee.setPosition("Entrenador");
        employee.setEmail("iat17@gmail.com");
        employee.setPhoneNumber("2281278263");
        employee.setPassword("Papoi123!");        
        try {
            idTestEmployee = TEST_EMPLOYEE_DAO.registerEmployee(TEST_EMPLOYEE);
            TEST_EMPLOYEE.setIdEmployee(idTestEmployee);
            employee.setIdEmployee(idTestEmployee);
            rowsAffected = TEST_EMPLOYEE_DAO.updateEmployee(employee);
            TEST_EMPLOYEE_DAO.deleteEmployeeById(idTestEmployee);
        } catch(DAOException exception) {
            Logger.getLogger(TestEmployeeDAO.class.getName()).log(Level.SEVERE, null, exception);
        }
        Assert.assertTrue(rowsAffected > 0);        
    }  
    
    @Test
    public void testInvalidCharactersUpdateEmployee() {
        int idTestEmployee = 0;
        int rowsAffected = 0;
        initializeSuccesTestEmployee();
        Employee employee = new Employee();
        employee.setName("*****");
        employee.setPaternalSurname("+++++");
        employee.setMaternalSurname("@@@@@");
        employee.setPosition("Entrenador");
        employee.setEmail("!!!!!!");
        employee.setPhoneNumber("2281273467");
        employee.setPassword("Papoi123!");        
        try {
            idTestEmployee = TEST_EMPLOYEE_DAO.registerEmployee(TEST_EMPLOYEE);
            TEST_EMPLOYEE.setIdEmployee(idTestEmployee);
            employee.setIdEmployee(idTestEmployee);
            rowsAffected = TEST_EMPLOYEE_DAO.updateEmployee(employee);
            TEST_EMPLOYEE_DAO.deleteEmployeeById(idTestEmployee);
        } catch(DAOException exception) {
            Logger.getLogger(TestEmployeeDAO.class.getName()).log(Level.SEVERE, null, exception);
        }
        Assert.assertTrue(rowsAffected > 0);        
    }     
    
    @Test
    public void testValidNullFieldsUpdateEmployee() {
        int idTestEmployee = 0;
        int rowsAffected = 0;
        initializeSuccesTestEmployee();
        Employee employee = new Employee();
        employee.setName("Isaac");
        employee.setPaternalSurname("Avila");
        employee.setMaternalSurname("");
        employee.setPosition("Entrenador");
        employee.setEmail("iat17@gmail.com");
        employee.setPhoneNumber("2281278263");
        employee.setPassword("Papoi123!");        
        try {
            idTestEmployee = TEST_EMPLOYEE_DAO.registerEmployee(TEST_EMPLOYEE);
            TEST_EMPLOYEE.setIdEmployee(idTestEmployee);
            employee.setIdEmployee(idTestEmployee);
            rowsAffected = TEST_EMPLOYEE_DAO.updateEmployee(employee);
            TEST_EMPLOYEE_DAO.deleteEmployeeById(idTestEmployee);
        } catch(DAOException exception) {
            Logger.getLogger(TestEmployeeDAO.class.getName()).log(Level.SEVERE, null, exception);
        }
        Assert.assertTrue(rowsAffected > 0);        
    } 
    
    @Test
    public void testInvalidNullFieldsUpdateEmployee() {
        int idTestEmployee = 0;
        int rowsAffected = 0;
        initializeSuccesTestEmployee();
        Employee employee = new Employee();
        employee.setName("");
        employee.setPaternalSurname("");
        employee.setMaternalSurname("");
        employee.setPosition("Entrenador");
        employee.setEmail("iat17@gmail.com");
        employee.setPhoneNumber("2281278263");
        employee.setPassword("Papoi123!");        
        try {
            idTestEmployee = TEST_EMPLOYEE_DAO.registerEmployee(TEST_EMPLOYEE);
            TEST_EMPLOYEE.setIdEmployee(idTestEmployee);
            employee.setIdEmployee(idTestEmployee);
            rowsAffected = TEST_EMPLOYEE_DAO.updateEmployee(employee);
            TEST_EMPLOYEE_DAO.deleteEmployeeById(idTestEmployee);
        } catch(DAOException exception) {
            Logger.getLogger(TestEmployeeDAO.class.getName()).log(Level.SEVERE, null, exception);
        }
        Assert.assertTrue(rowsAffected > 0);        
    }     

    private void initializeSuccesTestEmployee() {
        TEST_EMPLOYEE.setName("Axel");
        TEST_EMPLOYEE.setPaternalSurname("Valdes");
        TEST_EMPLOYEE.setMaternalSurname("Contreras");
        TEST_EMPLOYEE.setPosition("Entrenador");
        TEST_EMPLOYEE.setEmail("axlvaldez74@gmail.com");
        TEST_EMPLOYEE.setPhoneNumber("2281273467");
        TEST_EMPLOYEE.setPassword("Papoi123!");
    }

    private void initializeInvalidCharactersTestEmployee() {
        TEST_EMPLOYEE.setName("*****");
        TEST_EMPLOYEE.setPaternalSurname("+++++");
        TEST_EMPLOYEE.setMaternalSurname("@@@@@");
        TEST_EMPLOYEE.setPosition("Entrenador");
        TEST_EMPLOYEE.setEmail("!!!!!!");
        TEST_EMPLOYEE.setPhoneNumber("2281273467");
        TEST_EMPLOYEE.setPassword("Papoi123!");
    }

    private void initializeValidNullFieldsTestEmployee() {
        TEST_EMPLOYEE.setName("Axel");
        TEST_EMPLOYEE.setPaternalSurname("Valdes");
        TEST_EMPLOYEE.setMaternalSurname("");
        TEST_EMPLOYEE.setPosition("Entrenador");
        TEST_EMPLOYEE.setEmail("axlvaldez74@gmail.com");
        TEST_EMPLOYEE.setPhoneNumber("2281273467");
        TEST_EMPLOYEE.setPassword("Papoi123!");
    }

    private void initializeInvalidNullFieldsTestEmployee() {
        TEST_EMPLOYEE.setName("");
        TEST_EMPLOYEE.setPaternalSurname("");
        TEST_EMPLOYEE.setMaternalSurname("");
        TEST_EMPLOYEE.setPosition("Entrenador");
        TEST_EMPLOYEE.setEmail("axlvaldez74@gmail.com");
        TEST_EMPLOYEE.setPhoneNumber("");
        TEST_EMPLOYEE.setPassword("Papoi123!");
    }

    private void initializeAuxTestEmployee() {
        AUX_TEST_EMPLOYEE.setName("Axel");
        AUX_TEST_EMPLOYEE.setPaternalSurname("Valdes");
        AUX_TEST_EMPLOYEE.setMaternalSurname("Contreras");
        AUX_TEST_EMPLOYEE.setPosition("Entrenador");
        AUX_TEST_EMPLOYEE.setEmail("axlvaldez74@gmail.com");
        AUX_TEST_EMPLOYEE.setPhoneNumber("2281273467");
        AUX_TEST_EMPLOYEE.setPassword("Papoi123!");
    }

}

