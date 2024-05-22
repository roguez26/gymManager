package mx.fei.gymmanagerapp.logic.member;

import java.util.Objects;
import mx.fei.gymmanagerapp.logic.implementations.FieldValidator;

/**
 *
 * @author ivanr
 */
public class Member {

    private int idMember;
    private String name;
    private String paternalSurname;
    private String maternalSurname;
    private String email;
    private String phoneNumber;
    private String registrationDate;
    private String password;

    public Member() {

    }

    public int getIdMember() {
        return idMember;
    }

    public void setIdMember(int idMember) {
        this.idMember = idMember;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        FieldValidator fieldValidar = new FieldValidator();
        fieldValidar.checkName(name);
        this.name = name;
    }

    public String getPaternalSurname() {
        return paternalSurname;
    }

    public void setPaternalSurname(String paternalSurname) {
        FieldValidator fieldValidar = new FieldValidator();
        fieldValidar.checkName(paternalSurname);
        this.paternalSurname = paternalSurname;
    }

    public String getMaternalSurname() {
        return maternalSurname;
    }

    public void setMaternalSurname(String maternalSurname) {
        FieldValidator fieldValidar = new FieldValidator();
        fieldValidar.checkName(maternalSurname);
        this.maternalSurname = maternalSurname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        FieldValidator fieldValidar = new FieldValidator();
        fieldValidar.checkEmail(email);
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        FieldValidator fieldValidar = new FieldValidator();
        fieldValidar.checkPhoneNumber(phoneNumber);
        this.phoneNumber = phoneNumber;
    }

    public String getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(String registrationDate) {
        this.registrationDate = registrationDate;
    }
    
    public void setPassword(String password) {
        FieldValidator fieldValidar = new FieldValidator();
        fieldValidar.checkPassword(password);
        this.password = password;
    }
    
    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return name + " " + paternalSurname + " " + maternalSurname;
    }
    
    public void encryptPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object object) {
        boolean isEqual;

        if (this == object) {
            isEqual = true;
        } else if (object == null || getClass() != object.getClass()) {
            isEqual = false;
        } else {
            Member otherMember = (Member) object;
            isEqual = idMember == otherMember.idMember
                    && Objects.equals(name, otherMember.name)
                    && Objects.equals(paternalSurname, otherMember.paternalSurname)
                    && Objects.equals(maternalSurname, otherMember.maternalSurname)
                    && Objects.equals(email, otherMember.email)
                    && Objects.equals(phoneNumber, otherMember.phoneNumber)
                    && Objects.equals(registrationDate, otherMember.registrationDate)
                    && Objects.equals(password, otherMember.password);
        }

        return isEqual;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idMember, name, paternalSurname, maternalSurname, email, phoneNumber, registrationDate, password);
    }

}
