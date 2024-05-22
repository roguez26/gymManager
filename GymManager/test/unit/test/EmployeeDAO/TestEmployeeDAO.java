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
    public void testSuccessRegisterEmployee() {
        int idEmployee = 0;
        initializeSuccesTestEmployee();        
        try {
            idEmployee = TEST_EMPLOYEE_DAO.registerEmployee(TEST_EMPLOYEE);
            TEST_EMPLOYEE_DAO.deleteEmployeeById(idEmployee);
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
    
    private void initializeSuccesTestEmployee() {
        TEST_EMPLOYEE.setName("Axel");
        TEST_EMPLOYEE.setPaternalSurname("Valdes");
        TEST_EMPLOYEE.setMaternalSurname("Contreras");
        TEST_EMPLOYEE.setPosition("Entrenador");
        TEST_EMPLOYEE.setEmail("axlvaldez74@gmail.com");
        TEST_EMPLOYEE.setPhoneNumber("2281273467");
        TEST_EMPLOYEE.setPassword("papoi123");
    }
    
    private void initializeInvalidCharactersTestEmployee() {
        TEST_EMPLOYEE.setName("*****");
        TEST_EMPLOYEE.setPaternalSurname("+++++");
        TEST_EMPLOYEE.setMaternalSurname("@@@@@");
        TEST_EMPLOYEE.setPosition("Entrenador");
        TEST_EMPLOYEE.setEmail("!!!!!!");
        TEST_EMPLOYEE.setPhoneNumber("2281273467"); 
        TEST_EMPLOYEE.setPassword("------");
    }    
    
    private void initializeValidNullFieldsTestEmployee() {
        TEST_EMPLOYEE.setName("Axel");
        TEST_EMPLOYEE.setPaternalSurname("Valdes");
        TEST_EMPLOYEE.setMaternalSurname("");
        TEST_EMPLOYEE.setPosition("Entrenador");
        TEST_EMPLOYEE.setEmail("axlvaldez74@gmail.com");
        TEST_EMPLOYEE.setPhoneNumber("2281273467");   
        TEST_EMPLOYEE.setPassword("papoi123");
    }    

    private void initializeInvalidNullFieldsTestEmployee() {
        TEST_EMPLOYEE.setName("");
        TEST_EMPLOYEE.setPaternalSurname("");
        TEST_EMPLOYEE.setMaternalSurname("");
        TEST_EMPLOYEE.setPosition("Entrenador");
        TEST_EMPLOYEE.setEmail("axlvaldez74@gmail.com");
        TEST_EMPLOYEE.setPhoneNumber("");    
        TEST_EMPLOYEE.setPassword("");
    }  
    
    private void initializeAuxTestEmployee() {
        AUX_TEST_EMPLOYEE.setName("Axel");
        AUX_TEST_EMPLOYEE.setPaternalSurname("Valdes");
        AUX_TEST_EMPLOYEE.setMaternalSurname("Contreras");
        AUX_TEST_EMPLOYEE.setPosition("Entrenador");
        AUX_TEST_EMPLOYEE.setEmail("axlvaldez74@gmail.com");
        AUX_TEST_EMPLOYEE.setPhoneNumber("2281273467");     
        AUX_TEST_EMPLOYEE.setPassword("papoi123");
    }       
        
}
