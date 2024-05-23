package mx.fei.gymmanagerapp.logic.member;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import mx.fei.gymmanagerapp.dataaccess.DatabaseManager;
import mx.fei.gymmanagerapp.logic.implementations.DAOException;
import mx.fei.gymmanagerapp.logic.implementations.Status;

/**
 *
 * @author ivanr
 */
public class MemberDAO implements IMember {

    private static final Logger LOG = Logger.getLogger(MemberDAO.class.getName());
    
    @Override
    public Member authenticateMember(String email, String password) throws DAOException {
        Member memberForAuthenticate = new Member();

        try {
            memberForAuthenticate = getMemberByEmail(email);
        } catch (DAOException exception) {
            throw new DAOException("No fue posible hacer la validacion", Status.WARNING);
        }

        if (memberForAuthenticate.getIdMember() > 0) {
            if (memberForAuthenticate.getPassword().equals(encryptPassword(password))) {
                return memberForAuthenticate;
            } else {
                throw new DAOException("La contraseña proporcinada es incorrecta", Status.WARNING);
            }
        } else {
            throw new DAOException("El correo no se encuentra registrado", Status.WARNING);
        }
        
    }

    @Override
    public boolean isThereAtLeastOneMember() throws DAOException {
        boolean result = false;
        String statement = "SELECT EXISTS(SELECT 1 FROM Miembro LIMIT 1) AS hay_registros;";
        DatabaseManager databaseManager = new DatabaseManager();

        try (Connection connection = databaseManager.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(statement); ResultSet resultSet = preparedStatement.executeQuery()) {
            if (resultSet.next()) {
                result = resultSet.getBoolean("hay_registros");
            }
        } catch (SQLException exception) {
            LOG.log(Level.SEVERE, exception.getMessage(), exception);
            throw new DAOException("No fue posible verificar que haya miembros", Status.ERROR);
        }
        return result;
    }

    @Override
    public int registerMember(Member member) throws DAOException {
        int result = 0;
        if (!checkEmailDuplication(member)) {
            member.encryptPassword(encryptPassword(member.getPassword()));
            result = insertMemberTransaction(member);
        }
        return result;

    }

    private int insertMemberTransaction(Member member) throws DAOException {
        int result = -1;
        String statement = "INSERT INTO Miembro (nombre, apellidopaterno,"
                + " apellidomaterno, telefono, correo, contrasenia) VALUES(?, ?, ?, ?, ?, ?);";
        DatabaseManager databaseManager = new DatabaseManager();

        try (Connection connection = databaseManager.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(statement, PreparedStatement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, member.getName());
            preparedStatement.setString(2, member.getPaternalSurname());
            preparedStatement.setString(3, member.getMaternalSurname());
            preparedStatement.setString(4, member.getPhoneNumber());
            preparedStatement.setString(5, member.getEmail());
            preparedStatement.setString(6, member.getPassword());
            preparedStatement.executeUpdate();
            try (ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    result = resultSet.getInt(1);
                }
            }
        } catch (SQLException exception) {
            LOG.log(Level.SEVERE, exception.getMessage(), exception);
            throw new DAOException("No fue posible registrar al miembro", Status.ERROR);
        }
        return result;
    }

    @Override
    public int updateMember(Member newMemberInformation) throws DAOException {
        int result = 0;
        String passwordForUpdate = newMemberInformation.getPassword();
        String encriptedPasswordForUpdate;
        Member oldMemberInformation = getMemberById(newMemberInformation.getIdMember());
        
        encriptedPasswordForUpdate = encryptPassword(passwordForUpdate);
        
        if (!oldMemberInformation.getPassword().equals(encriptedPasswordForUpdate)) {
            newMemberInformation.encryptPassword(encriptedPasswordForUpdate);
        }
        
        if (!checkEmailDuplication(newMemberInformation)) {
            result = updateMemberTransaction(newMemberInformation);
        }
        return result;
    }

    private int updateMemberTransaction(Member newMemberInformation) throws DAOException {
        int result = -1;
        String statement = "UPDATE Miembro SET nombre = ?, apellidopaterno = ?, apellidomaterno = ?, telefono = ?, correo = ?, contrasenia = ? WHERE idMiembro = ?";
        DatabaseManager databaseManager = new DatabaseManager();

        try (Connection connection = databaseManager.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(statement)) {
            preparedStatement.setString(1, newMemberInformation.getName());
            preparedStatement.setString(2, newMemberInformation.getPaternalSurname());
            preparedStatement.setString(3, newMemberInformation.getMaternalSurname());
            preparedStatement.setString(4, newMemberInformation.getPhoneNumber());
            preparedStatement.setString(5, newMemberInformation.getEmail());
            preparedStatement.setString(6, newMemberInformation.getPassword());
            preparedStatement.setInt(7, newMemberInformation.getIdMember());
            result = preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            LOG.log(Level.SEVERE, exception.getMessage(), exception);
            throw new DAOException("No fue posible actualizar al miembro", Status.ERROR);
        }
        return result;
    }

    @Override
    public int deleteMemberById(int idMember) throws DAOException {
        int result = -1;
        String statement = "DELETE FROM Miembro WHERE idMiembro = ?";
        DatabaseManager databaseManager = new DatabaseManager();

        try (Connection connection = databaseManager.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(statement)) {
            preparedStatement.setInt(1, idMember);
            result = preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            LOG.log(Level.SEVERE, exception.getMessage(), exception);
            throw new DAOException("No fue posible eliminar al miembro", Status.ERROR);
        }
        return result;

    }

    @Override
    public Member getMemberByEmail(String email) throws DAOException {
        Member memberAux = new Member();
        DatabaseManager databaseManager = new DatabaseManager();
        String statement = "SELECT * FROM miembro WHERE correo = ?";

        try (Connection connection = databaseManager.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(statement)) {
            preparedStatement.setString(1, email);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    memberAux = initializeMemberFromResultSet(resultSet);
                }
            }
        } catch (SQLException exception) {
            LOG.log(Level.SEVERE, exception.getMessage(), exception);
            throw new DAOException("No fue posible obtener el empleado", Status.ERROR);
        }
        return memberAux;
    }

    private Member initializeMemberFromResultSet(ResultSet resultSet) throws SQLException {
        Member member = new Member();
        member.setIdMember(resultSet.getInt("idMiembro"));
        member.setName(resultSet.getString("nombre"));
        member.setPaternalSurname(resultSet.getString("apellidoPaterno"));
        member.setMaternalSurname(resultSet.getString("apellidoMaterno"));
        member.setPhoneNumber(resultSet.getString("telefono"));
        member.setEmail(resultSet.getString("correo"));
        member.encryptPassword(resultSet.getString("contrasenia"));
        return member;
    }

    @Override
    public Member getMemberById(int idMember) throws DAOException {
        Member member = new Member();
        DatabaseManager databaseManager = new DatabaseManager();
        String statement = "SELECT * FROM Miembro WHERE idMiembro = ?";

        try (Connection connection = databaseManager.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(statement)) {
            preparedStatement.setInt(1, idMember);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    member = initializeMemberFromResultSet(resultSet);
                }
            }
        } catch (SQLException exception) {
            LOG.log(Level.SEVERE, exception.getMessage(), exception);
            throw new DAOException("No fue posible obtener al miembro", Status.ERROR);
        }
        return member;

    }

    @Override
    public ArrayList<Member> getMembers() throws DAOException {
        ArrayList<Member> members = new ArrayList<>();
        DatabaseManager databaseManager = new DatabaseManager();
        String statement = "SELECT * FROM Miembro";

        try (Connection connection = databaseManager.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(statement); ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                Member member = initializeMemberFromResultSet(resultSet);
                members.add(member);
            }
        } catch (SQLException exception) {
            LOG.log(Level.SEVERE, exception.getMessage(), exception);
            throw new DAOException("No fue posible obtener los miembros", Status.ERROR);
        }
        return members;
    }

    private boolean checkEmailDuplication(Member member) throws DAOException {
        Member memberAux = new Member();
        int idMember = 0;

        try {
            memberAux = getMemberByEmail(member.getEmail());
            idMember = memberAux.getIdMember();
        } catch (DAOException exception) {
            throw new DAOException("No fue posible realizar la validacion, intente registrar mas tarde.", Status.ERROR);
        }
        if (member.getIdMember() != idMember && idMember > 0) {
            throw new DAOException("El correo ya se encuentra registrado", Status.WARNING);
        }
        return false;
    }
    
    public String encryptPassword(String password) throws DAOException {
        String encryptedPassword;

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = digest.digest(password.getBytes());

            StringBuilder hexadecimalString = new StringBuilder();

            for (byte byteIntHashedBytes : hashedBytes) {
                hexadecimalString.append(String.format("%02x", byteIntHashedBytes));
            }

            encryptedPassword = hexadecimalString.toString();
        } catch (NoSuchAlgorithmException exception) {
            LOG.log(Level.SEVERE, exception.getMessage(), exception);
            throw new DAOException("No fue posible asignar la contraseña al usuario", Status.ERROR);
        }
        return encryptedPassword;
    }

    
}
