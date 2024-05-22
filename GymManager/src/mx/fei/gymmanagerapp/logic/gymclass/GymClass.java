package mx.fei.gymmanagerapp.logic.gymclass;

import mx.fei.gymmanagerapp.logic.employee.Employee;
import mx.fei.gymmanagerapp.logic.implementations.FieldValidator;

/*
 * @author d0ubl3_d
 */

public class GymClass {
    
    private Employee coach;
    
    private int idGymClass = 0;
    private String name;
    private String description;
    private String schedule;
    private String days;
    private int capacity;
    
    public GymClass() {
    }
    
    public Employee getCoach() {
        return coach;
    }
    
    public void setCoach(Employee coach) {
        this.coach = coach;
    }
    
    public int getIdGymClass() {
        return idGymClass;
    }
    
    public void setIdGymClass(int idGymClass) {
        this.idGymClass = idGymClass;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        FieldValidator fieldValidator = new FieldValidator();

        fieldValidator.checkName(name);
        this.name = name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        FieldValidator fieldValidator = new FieldValidator();

        fieldValidator.checkText(description);
        this.description = description;
    }
    
    public String getSchedule() {
        return schedule;
    }
    
    public void setSchedule(String schedule) {
        FieldValidator fieldValidator = new FieldValidator();

        fieldValidator.checkScheduleFormat(schedule);
        this.schedule = schedule;        
    }
    
    public String getDays() {
        return days;
    }
    
    public void setDays(String days) {
        FieldValidator fieldValidator = new FieldValidator();

        fieldValidator.checkDaysOfWeek(days);
        this.days = days;                                
    }
    
    public int getCapacity() {
        return capacity;
    }
    
    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
    
}
