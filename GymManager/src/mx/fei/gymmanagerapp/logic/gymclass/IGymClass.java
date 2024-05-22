package mx.fei.gymmanagerapp.logic.gymclass;

import java.util.ArrayList;
import mx.fei.gymmanagerapp.logic.implementations.DAOException;

/*
 * @author d0ubl3_d
 */

public interface IGymClass {
    
    public int registerGymClass(GymClass gymClass) throws DAOException;
    public int updateGymClass(GymClass gymClass) throws DAOException;    
    public GymClass getGymClassByIdGymClass(int idGymClass) throws DAOException;    
    public GymClass getGymClassByName(String name) throws DAOException;
    public ArrayList<GymClass> getGymClasses() throws DAOException;                
    public int deleteGymClassByIdGymClass(int idGymClass) throws DAOException;
    
}
