package mx.fei.gymmanagerapp.logic.employee;

import mx.fei.gymmanagerapp.logic.implementations.FieldValidator;

public class Employee {

    private int idEmployee = 0;
    private String name;
    private String paternalSurname;
    private String maternalSurname;
    private String position;
    private String phoneNumber;
    private String eMail;    
    
    public Employee() {
    }

    public int getIdEmployee() {
        return idEmployee;
    }

    public String getName() {
        return name;
    }

    public String getPaternalSurname() {
        return paternalSurname;
    }

    public String getMaternalSurname() {
        return maternalSurname;
    }
    
    public String getPosition() {
        return position;
    }
    
    public String getPhoneNumber() {
        return phoneNumber;
    }
    
    public String getEmail() {
        return eMail;
    }    

    public void setIdEmployee(int idEmployee) {
        this.idEmployee = idEmployee;
    }

    public void setName(String name) {
        FieldValidator fieldValidator = new FieldValidator();

        fieldValidator.checkName(name);
        this.name = name;
    }

    public void setPaternalSurname(String paternalSurname) {
        FieldValidator fieldValidator = new FieldValidator();

        fieldValidator.checkName(paternalSurname);
        this.paternalSurname = paternalSurname;
    }

    public void setMaternalSurname(String maternalSurname) {
        if (maternalSurname != null) {
            FieldValidator fieldValidator = new FieldValidator();
            fieldValidator.checkName(maternalSurname);
        }
        this.maternalSurname = maternalSurname;
    }
    
    public void setPosition(String position) {
        this.position = position;
    }

    public void setPhoneNumber(String phoneNumber) {
        FieldValidator fieldValidator = new FieldValidator();

        fieldValidator.checkPhoneNumber(phoneNumber);
        this.phoneNumber = phoneNumber;
    }

    public void setEmail(String eMail) {
        FieldValidator fieldValidator = new FieldValidator();

        fieldValidator.checkEmail(eMail);
        this.eMail = eMail;
    }
 
}
