package unit.test.EmployeeDAO;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.ArrayList;
import mx.fei.gymmanagerapp.logic.employee.Employee;
import mx.fei.gymmanagerapp.logic.employee.EmployeeDAO;
import mx.fei.gymmanagerapp.logic.gymclass.*;
import mx.fei.gymmanagerapp.logic.implementations.DAOException;
import org.junit.Before;
import org.junit.Test;
import org.junit.After;
import static org.junit.Assert.assertTrue;

/*
 * @author d0uble_d
 */
public class TestGymClassDAO {
   
    private static final GymClassDAO GYMCLASE_DAO = new GymClassDAO();
    private static final GymClass GYMCLASS_FOR_TESTING = new GymClass();
    
    private static final GymClass AUX_GYMCLASS = new GymClass();
    
    private static final EmployeeDAO EMPLOYEE_DAO = new EmployeeDAO();
    private static final Employee AUX_EMPLOYEE = new Employee();
    
    @Before
    public void setUp() throws DAOException {
        int idEmployee = 0;
        
        initializeAuxiliarEmployee();
        initializeGymClass();
        initializeAuxiliarGymClass();
        
        try {
            idEmployee = EMPLOYEE_DAO.registerEmployee(AUX_EMPLOYEE);            
        } catch (DAOException exception) {
            Logger.getLogger(TestGymClassDAO.class.getName()).log(Level.SEVERE, null, exception);
        }
        AUX_EMPLOYEE.setIdEmployee(idEmployee);
        AUX_GYMCLASS.setIdGymClass(registerAuxiliaryGymClass());       
    }
    
    private void initializeAuxiliarEmployee() {
        AUX_EMPLOYEE.setName("Axel");
        AUX_EMPLOYEE.setPaternalSurname("Valdes");
        AUX_EMPLOYEE.setMaternalSurname("Contreras");
        AUX_EMPLOYEE.setPosition("Entrenador");
        AUX_EMPLOYEE.setEmail("axlvaldez74@gmail.com");
        AUX_EMPLOYEE.setPhoneNumber("2281273467");
    }
    
    private void initializeGymClass() {
        GYMCLASS_FOR_TESTING.setCoach(AUX_EMPLOYEE);
        GYMCLASS_FOR_TESTING.setName("Yoga");
        GYMCLASS_FOR_TESTING.setDescription("Clase de relajación y estiramientos."
        + " haremos posturas como: Tadasana, Balasana entre otras.");
        GYMCLASS_FOR_TESTING.setSchedule("13:30-15:30");
        GYMCLASS_FOR_TESTING.setDays("Lunes, Miercoles, Viernes");
        GYMCLASS_FOR_TESTING.setCapacity(30);
    }
    
    private void initializeAuxiliarGymClass() {
        AUX_GYMCLASS.setCoach(AUX_EMPLOYEE);
        AUX_GYMCLASS.setName("Boxeo");
        AUX_GYMCLASS.setDescription("Clase basica, en la que haremos sombra, "
        + " saltaremos la curda, sparring entre otras cosas");
        AUX_GYMCLASS.setSchedule("15:00-17:00");
        AUX_GYMCLASS.setDays("Lunes, Miercoles, Viernes");
        AUX_GYMCLASS.setCapacity(30);
    }
    
    private int registerAuxiliaryGymClass() {
        int idAuxGymClass = 0;
        try {
            idAuxGymClass = GYMCLASE_DAO.registerGymClass(AUX_GYMCLASS);
        } catch (DAOException exception) {
            Logger.getLogger(TestGymClassDAO.class.getName()).log(Level.SEVERE, null, exception);
        }
        return idAuxGymClass;
    }
    
    /* PRUEBAS DE INSERT */
    
    @Test
    public void testRegisterGymCassSucces() {
        int result = 0;
        
        try {
            result = GYMCLASE_DAO.registerGymClass(GYMCLASS_FOR_TESTING);
            GYMCLASS_FOR_TESTING.setIdGymClass(result);
        } catch (DAOException exception) {
            Logger.getLogger(TestGymClassDAO.class.getName()).log(Level.SEVERE, null, exception);
        } 
        assertTrue(result > 0);
    }   
    
    @Test
    public void testRegisterGymClassFailByDuplicateName() {
        int result = 0;
        GYMCLASS_FOR_TESTING.setName(AUX_GYMCLASS.getName());
        try {
            result = GYMCLASE_DAO.registerGymClass(GYMCLASS_FOR_TESTING);
        } catch (DAOException exception) {
            Logger.getLogger(TestGymClassDAO.class.getName()).log(Level.SEVERE, null, exception);            
        }
        assertTrue(result > 0);
    }
    
    @Test
    public void testRegisterGymClassFailByInvalidName() {
        int result = 0;
        GYMCLASS_FOR_TESTING.setName("Y0ga_");
        
        try {
            result = GYMCLASE_DAO.registerGymClass(GYMCLASS_FOR_TESTING);
            GYMCLASS_FOR_TESTING.setIdGymClass(result);
        } catch (DAOException exception) {
            Logger.getLogger(TestGymClassDAO.class.getName()).log(Level.SEVERE, null, exception);             
        }         
        assertTrue(result > 0);
    }
    
    @Test
    public void testRegisterGymClassFailByInvalidDays() {
        int result = 0;        
        GYMCLASS_FOR_TESTING.setDays("Luunes, Miércol, Viernes");
                
        try {
            result = GYMCLASE_DAO.registerGymClass(GYMCLASS_FOR_TESTING);
            GYMCLASS_FOR_TESTING.setIdGymClass(result);
        } catch (DAOException exception) {
            Logger.getLogger(TestGymClassDAO.class.getName()).log(Level.SEVERE, null, exception);
        }         
        assertTrue(result > 0);
    }
    
    @Test
    public void testRegisterGymClassFailByDaysRepeated() {
        int result = 0;        
        GYMCLASS_FOR_TESTING.setDays("Lunes, Lunes, Sábado, Sábado");
                
        try {
            result = GYMCLASE_DAO.registerGymClass(GYMCLASS_FOR_TESTING);
            GYMCLASS_FOR_TESTING.setIdGymClass(result);
        } catch (DAOException exception) {
            Logger.getLogger(TestGymClassDAO.class.getName()).log(Level.SEVERE, null, exception);
        }         
        assertTrue(result > 0);
    }
    
    @Test
    public void testRegisterGymClassFailByInvalidSchedule() {
        int result = 0;
        GYMCLASS_FOR_TESTING.setSchedule("13:30-13:30");
        
        try {
            result = GYMCLASE_DAO.registerGymClass(GYMCLASS_FOR_TESTING);
            GYMCLASS_FOR_TESTING.setIdGymClass(result);
        } catch (DAOException exception) {
            Logger.getLogger(TestGymClassDAO.class.getName()).log(Level.SEVERE, null, exception);
        }         
        assertTrue(result > 0);
    }
    
    /* PRUEBAS DE UPDATE */
    
    @Test
    public void testUpdateGymClassSucces() {
        int result = 0;
        GYMCLASS_FOR_TESTING.setSchedule("12:00-14:00");
        GYMCLASS_FOR_TESTING.setDays("Lunes, Jueves");
        GYMCLASS_FOR_TESTING.setCapacity(40);
        GYMCLASS_FOR_TESTING.setIdGymClass(AUX_GYMCLASS.getIdGymClass());
        
        try {
            result = GYMCLASE_DAO.updateGymClass(GYMCLASS_FOR_TESTING);
            GYMCLASS_FOR_TESTING.setIdGymClass(result);
        } catch (DAOException exception) {
            Logger.getLogger(TestGymClassDAO.class.getName()).log(Level.SEVERE, null, exception);
        } 
        assertTrue(result > 0);                
    }
    
    @Test
    public void testUpdateGymClassFailByDuplicateName() {
        int result = 0;
        GYMCLASS_FOR_TESTING.setName(AUX_GYMCLASS.getName());        
        
        try {
            result = GYMCLASE_DAO.updateGymClass(GYMCLASS_FOR_TESTING);
            GYMCLASS_FOR_TESTING.setIdGymClass(result);
        } catch (DAOException exception) {
            Logger.getLogger(TestGymClassDAO.class.getName()).log(Level.SEVERE, null, exception);
        } 
        assertTrue(result > 0);        
    }
    
    @Test
    public void testUpdateGymClassFailByInvalidName() {
        int result = 0;
        GYMCLASS_FOR_TESTING.setName("*Ca1istecn1a?");  
        
        try {
            result = GYMCLASE_DAO.updateGymClass(GYMCLASS_FOR_TESTING);
            GYMCLASS_FOR_TESTING.setIdGymClass(result);
        } catch (DAOException exception) {
            Logger.getLogger(TestGymClassDAO.class.getName()).log(Level.SEVERE, null, exception);
        } 
        assertTrue(result > 0);
    }
    
    @Test
    public void testUpdateGymClassFailByInvalidSchedule() {
        int result = 0;
        GYMCLASS_FOR_TESTING.setSchedule("16:00-16:00");  
        
        try {
            result = GYMCLASE_DAO.updateGymClass(GYMCLASS_FOR_TESTING);
            GYMCLASS_FOR_TESTING.setIdGymClass(result);
        } catch (DAOException exception) {
            Logger.getLogger(TestGymClassDAO.class.getName()).log(Level.SEVERE, null, exception);
        } 
        assertTrue(result > 0);
    }
    
    /* PRUEBAS DE GETTERS */
    
    @Test
    public void testgetGymClassByIdGymClass() {
        int result = 0; 
        GymClass gymClass = new GymClass();
        int idGymClass = 0;     
        idGymClass = AUX_GYMCLASS.getIdGymClass();
        
        try {
            gymClass = GYMCLASE_DAO.getGymClassByIdGymClass(idGymClass);
            result = gymClass.getIdGymClass();
        } catch (DAOException exception) {
            Logger.getLogger(TestGymClassDAO.class.getName()).log(Level.SEVERE, null, exception);
        } 
        assertTrue(result > 0);
    }
    
    @Test
    public void testgetGymClassByName() {
        int result = 0; 
        GymClass gymClass = new GymClass();
        String name;     
        name = AUX_GYMCLASS.getName();        
        try {
            gymClass = GYMCLASE_DAO.getGymClassByName(name);
            result = gymClass.getIdGymClass();
        } catch (DAOException exception) {
            Logger.getLogger(TestGymClassDAO.class.getName()).log(Level.SEVERE, null, exception);
        } 
        assertTrue(result > 0);
    }
    
    @Test
    public void testgetGymClasses() {
        int result = 0;
        ArrayList<GymClass> gymClasses = new ArrayList<>();
        try {
            gymClasses = GYMCLASE_DAO.getGymClasses();
        } catch (DAOException exception) {
            Logger.getLogger(TestGymClassDAO.class.getName()).log(Level.SEVERE, null, exception);
        }
        result = gymClasses.size();
        assertTrue(result > 0);
    }
                   
    /* PRUEBAS DE DELETE */
    
    @Test
    public void testDeleteGymCassSucces() {
        int result = 0;
        
        try {
            result = GYMCLASE_DAO.deleteGymClassByIdGymClass(AUX_GYMCLASS.getIdGymClass());
        } catch (DAOException exception) {
            Logger.getLogger(TestGymClassDAO.class.getName()).log(Level.SEVERE, null, exception);
        }
        AUX_GYMCLASS.setIdGymClass(registerAuxiliaryGymClass());
        assertTrue(result > 0);
    }
    
    @Test
    public void testDeleteGymCassFailByNonExistenceGymClass() {
        int result = 0;
        int idAuxGymClass = 0;
        
        idAuxGymClass = AUX_GYMCLASS.getIdGymClass();
        AUX_GYMCLASS.setIdGymClass(0);        
        try {
            result = GYMCLASE_DAO.deleteGymClassByIdGymClass(AUX_GYMCLASS.getIdGymClass());
        } catch (DAOException exception) {
            Logger.getLogger(TestGymClassDAO.class.getName()).log(Level.SEVERE, null, exception);
        }
        AUX_GYMCLASS.setIdGymClass(idAuxGymClass);
        assertTrue(result > 0);
    }
    
    @After
    public void tearDown() {
        try {
            if (GYMCLASS_FOR_TESTING.getIdGymClass() > 0) {
                GYMCLASE_DAO.deleteGymClassByIdGymClass(GYMCLASS_FOR_TESTING.getIdGymClass());
            }         
            GYMCLASE_DAO.deleteGymClassByIdGymClass(AUX_GYMCLASS.getIdGymClass());
            EMPLOYEE_DAO.deleteEmployeeById(AUX_EMPLOYEE.getIdEmployee());
        } catch (DAOException exception) {
            Logger.getLogger(TestGymClassDAO.class.getName()).log(Level.SEVERE, null, exception);
        }
    }
    
    
}