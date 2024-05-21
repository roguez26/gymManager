package unit.test.EmployeeDAO;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.ArrayList;
import mx.fei.gymmanagerapp.logic.employee.Employee;
import mx.fei.gymmanagerapp.logic.employee.EmployeeDAO;
import mx.fei.gymmanagerapp.logic.implementations.DAOException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.After;

public class TestEmployeeDAO {
    
    private static final EmployeeDAO TEST_EMPLOYEE_DAO = new EmployeeDAO();
    private static final Employee TEST_EMPLOYEE = new Employee();
    private static final Employee AUX_TEST_EMPLOYEE = new Employee();
    
    @Before
    public void setUp() {
        try {
            initializeTestEmployee();
            int idTestEmployee = TEST_EMPLOYEE_DAO.registerEmployee(TEST_EMPLOYEE);
            TEST_EMPLOYEE.setIdEmployee(idTestEmployee);    
        } catch (DAOException exception) {
            Logger.getLogger(TestEmployeeDAO.class.getName()).log(Level.SEVERE, null, exception);
        }
    }
    
    @After
    public void tearDown() {
        try {
            TEST_EMPLOYEE_DAO.deleteEmployeeById(TEST_EMPLOYEE.getIdEmployee());
        } catch (DAOException exception) {
            Logger.getLogger(TestEmployeeDAO.class.getName()).log(Level.SEVERE, null, exception);
        }
    }
    
    @Test
    public void testSuccessRegisterEmployee() {
        int idEmployee = 0;
        initializeAuxTestEmployee();
        
        try {
            idEmployee = TEST_EMPLOYEE_DAO.registerEmployee(AUX_TEST_EMPLOYEE);
            TEST_EMPLOYEE_DAO.deleteEmployeeById(idEmployee);
        } catch (DAOException exception) {
            Logger.getLogger(TestEmployeeDAO.class.getName()).log(Level.SEVERE, null, exception);
        }
        Assert.assertTrue(idEmployee > 0);
    }
    
    private void initializeTestEmployee() {
        TEST_EMPLOYEE.setName("Axel");
        TEST_EMPLOYEE.setPaternalSurname("Valdes");
        TEST_EMPLOYEE.setMaternalSurname("Contreras");
        TEST_EMPLOYEE.setPosition("Entrenador");
        TEST_EMPLOYEE.setEmail("axlvaldez74@gmail.com");
        TEST_EMPLOYEE.setPhoneNumber("2281273467");        
    }
    
    private void initializeAuxTestEmployee() {
        AUX_TEST_EMPLOYEE.setName("Ivan");
        AUX_TEST_EMPLOYEE.setPaternalSurname("Rodriguez");
        AUX_TEST_EMPLOYEE.setMaternalSurname("Franco");
        AUX_TEST_EMPLOYEE.setPosition("Entrenador");
        AUX_TEST_EMPLOYEE.setEmail("roguez@gmail.com");
        AUX_TEST_EMPLOYEE.setPhoneNumber("2281245386");        
    }
    
}
