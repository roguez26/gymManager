package mx.fei.gymmanagerapp.logic.member;

import java.util.ArrayList;
import mx.fei.gymmanagerapp.logic.implementations.DAOException;

/**
 *
 * @author ivanr
 */
public interface IMember {
    
    public boolean isThereAtLeastOneMember() throws DAOException;
    public int registerMember(Member member) throws DAOException;
    public int updateMember(Member member) throws DAOException;
    public int deleteMemberById(int idMember) throws DAOException;
    public Member getMemberByEmail(String email) throws DAOException;
    public Member getMemberById(int idMember) throws DAOException;
    public ArrayList<Member> getMembers() throws DAOException; 
    public Member authenticateMember(String email, String password) throws DAOException;
    
}
