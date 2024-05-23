package mx.fei.gymmanagerapp.logic.employee;

import java.util.ArrayList;
import mx.fei.gymmanagerapp.logic.implementations.DAOException;

public interface IEmployee {
    
    public boolean isThereAtLeastOneEmployee() throws DAOException;
    public int registerEmployee(Employee employee) throws DAOException;
    public int updateEmployee(Employee employee) throws DAOException;
    public int deleteEmployeeById(int idEmployee) throws DAOException;
    public Employee getEmployeeByEmail(String email) throws DAOException;
    public Employee getEmployeeById(int idEmployee) throws DAOException;
    public ArrayList<Employee> getEmployees() throws DAOException;     
    
}
