package mx.fei.gymmanagerapp.logic.employee;

import java.util.Objects;
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
        if (!maternalSurname.isEmpty()) {
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
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return idEmployee == employee.idEmployee &&
                Objects.equals(name, employee.name) &&
                Objects.equals(paternalSurname, employee.paternalSurname) &&
                Objects.equals(maternalSurname, employee.maternalSurname) &&
                Objects.equals(position, employee.position) &&
                Objects.equals(phoneNumber, employee.phoneNumber) &&
                Objects.equals(eMail, employee.eMail);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idEmployee, name, paternalSurname, maternalSurname, position, phoneNumber, eMail);
    }    
    
    @Override
    public String toString() {
        return "Employee{" +
                "idEmployee=" + idEmployee +
                ", name='" + name + '\'' +
                ", paternalSurname='" + paternalSurname + '\'' +
                ", maternalSurname='" + maternalSurname + '\'' +
                ", position='" + position + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", eMail='" + eMail + '\'' +
                '}';
    }    
 
}
