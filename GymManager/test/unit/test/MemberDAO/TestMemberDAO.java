package unit.test.MemberDAO;

import java.util.logging.Level;
import java.util.logging.Logger;
import mx.fei.gymmanagerapp.logic.implementations.DAOException;
import mx.fei.gymmanagerapp.logic.member.Member;
import mx.fei.gymmanagerapp.logic.member.MemberDAO;
import org.junit.Assert;
import org.junit.Test;

public class TestMemberDAO {

    private static final MemberDAO TEST_MEMBER_DAO = new MemberDAO();
    private static final Member TEST_MEMBER = new Member();
    private static final Member AUX_TEST_MEMBER = new Member();

    @Test
    public void testSuccessRegisterMember() {
        int idMember = 0;
        initializeSuccesTestMember();
        try {
            idMember = TEST_MEMBER_DAO.registerMember(TEST_MEMBER);
            TEST_MEMBER_DAO.deleteMemberById(idMember);
        } catch (DAOException exception) {
            Logger.getLogger(TestMemberDAO.class.getName()).log(Level.SEVERE, null, exception);
        }
        System.out.println(idMember);
        Assert.assertTrue(idMember > 0);
    }
    
    @Test
    public void testDeleteMember() {
        int rowsAffected = 0;
        int idMember;
        initializeSuccesTestMember();
        try {
            idMember = TEST_MEMBER_DAO.registerMember(TEST_MEMBER);
            rowsAffected = TEST_MEMBER_DAO.deleteMemberById(idMember);
        } catch (DAOException exception) {
            Logger.getLogger(TestMemberDAO.class.getName()).log(Level.SEVERE, null, exception);
        }
        System.out.println(rowsAffected);
        Assert.assertTrue(rowsAffected > 0);
    }

    @Test
    public void testInvalidCharactersRegisterMember() {
        int idMember = 0;
        initializeInvalidCharactersTestMember();
        try {
            idMember = TEST_MEMBER_DAO.registerMember(TEST_MEMBER);
            TEST_MEMBER_DAO.deleteMemberById(idMember);
            TEST_MEMBER_DAO.deleteMemberById(idMember);
        } catch (DAOException exception) {
            Logger.getLogger(TestMemberDAO.class.getName()).log(Level.SEVERE, null, exception);
        }
        Assert.assertTrue(idMember > 0);
    }
    
    @Test
    public void testUpdateMember() {
        int idMember = 0;
        initializeSuccesTestMember();
        try {
            TEST_MEMBER.setIdMember(TEST_MEMBER_DAO.registerMember(TEST_MEMBER));
            TEST_MEMBER.setEmail("NewEmail@gmail.com");
            idMember = TEST_MEMBER_DAO.updateMember(TEST_MEMBER);
            TEST_MEMBER_DAO.deleteMemberById(idMember);
  
        } catch (DAOException exception) {
            Logger.getLogger(TestMemberDAO.class.getName()).log(Level.SEVERE, null, exception);
        }
        Assert.assertTrue(idMember > 0);
    }

    @Test
    public void testValidNullFieldsRegisterMember() {
        int idMember = 0;
        initializeValidNullFieldsTestMember();
        try {
            idMember = TEST_MEMBER_DAO.registerMember(TEST_MEMBER);
            TEST_MEMBER_DAO.deleteMemberById(idMember);
            TEST_MEMBER_DAO.deleteMemberById(idMember);
        } catch (DAOException exception) {
            Logger.getLogger(TestMemberDAO.class.getName()).log(Level.SEVERE, null, exception);
        }
        Assert.assertTrue(idMember > 0);
    }

    @Test
    public void testAuthenticateMember() {
        Member authenticatedMember = new Member();
        int idMember = 0;
        initializeSuccesTestMember();
        try {
            idMember =TEST_MEMBER_DAO.registerMember(TEST_MEMBER);
            authenticatedMember = TEST_MEMBER_DAO.authenticateMember(TEST_MEMBER.getEmail(), "Papoi123!");
            
            TEST_MEMBER_DAO.deleteMemberById(idMember);
            
        } catch (DAOException exception) {
            Logger.getLogger(TestMemberDAO.class.getName()).log(Level.SEVERE, null, exception);
        }
        Assert.assertTrue(authenticatedMember.getIdMember() > 0);
    }

    @Test
    public void testInvalidNullFieldsRegisterMember() {
        int idMember = 0;
        initializeInvalidNullFieldsTestMember();
        try {
            idMember = TEST_MEMBER_DAO.registerMember(TEST_MEMBER);
            TEST_MEMBER_DAO.deleteMemberById(idMember);
            TEST_MEMBER_DAO.deleteMemberById(idMember);
        } catch (DAOException exception) {
            Logger.getLogger(TestMemberDAO.class.getName()).log(Level.SEVERE, null, exception);
        }
        Assert.assertTrue(idMember > 0);
    }

    @Test
    public void testEmailAlreadyRegisteredRegisterMember() {
        int idMember = 0;
        int idAuxMember = 0;
        initializeSuccesTestMember();
        initializeAuxTestMember();

        try {
            idMember = TEST_MEMBER_DAO.registerMember(TEST_MEMBER);
            idAuxMember = TEST_MEMBER_DAO.registerMember(AUX_TEST_MEMBER);
            TEST_MEMBER_DAO.deleteMemberById(idMember);
            TEST_MEMBER_DAO.deleteMemberById(idAuxMember);
        } catch (DAOException exception) {
            Logger.getLogger(TestMemberDAO.class.getName()).log(Level.SEVERE, null, exception);
        }
        Assert.assertTrue(idAuxMember > 0);
    }

    private void initializeSuccesTestMember() {
        TEST_MEMBER.setName("Edgar");
        TEST_MEMBER.setPaternalSurname("Hernandez");
        TEST_MEMBER.setMaternalSurname("Cid");
        TEST_MEMBER.setEmail("edgarhc@gmail.com");
        TEST_MEMBER.setPhoneNumber("2283748393");

        TEST_MEMBER.setPassword("Papoi123!");

    }

    private void initializeInvalidCharactersTestMember() {
        TEST_MEMBER.setName("Ricardo123");
        TEST_MEMBER.setPaternalSurname("Lópe#z ");
        TEST_MEMBER.setMaternalSurname("Domíngu.ez");
        TEST_MEMBER.setEmail("richard@gmail.com");
        TEST_MEMBER.setPhoneNumber("2283809323");
        TEST_MEMBER.setPassword("Contra!@#345");
    }

    private void initializeValidNullFieldsTestMember() {
        TEST_MEMBER.setName("Axel");
        TEST_MEMBER.setPaternalSurname("Valdes");
        TEST_MEMBER.setMaternalSurname("");
        TEST_MEMBER.setEmail("axlvaldez74@gmail.com");
        TEST_MEMBER.setPhoneNumber("2281273467");
        TEST_MEMBER.setPassword("12345678Cn????");
    }

    private void initializeInvalidNullFieldsTestMember() {
        TEST_MEMBER.setName("");
        TEST_MEMBER.setPaternalSurname("");
        TEST_MEMBER.setMaternalSurname("");
        TEST_MEMBER.setEmail("");
        TEST_MEMBER.setPhoneNumber("");
        TEST_MEMBER.setPassword("12345678Cn????");
    }

    private void initializeAuxTestMember() {
        AUX_TEST_MEMBER.setName("Axel");
        AUX_TEST_MEMBER.setPaternalSurname("Valdes");
        AUX_TEST_MEMBER.setMaternalSurname("Contreras");
        AUX_TEST_MEMBER.setEmail("axlvaldez74@gmail.com");
        AUX_TEST_MEMBER.setPhoneNumber("2281273467");
        AUX_TEST_MEMBER.setPassword("12345678Cn????");
    }

}
