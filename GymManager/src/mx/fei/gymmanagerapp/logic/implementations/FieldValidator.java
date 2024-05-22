package mx.fei.gymmanagerapp.logic.implementations;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author ivanr
 */
public class FieldValidator {

    private final String EMAIL_REGEX = "^(?=.{3,45}$)[^\\s@]+@(?:uv\\.mx|estudiantes\\.uv\\.mx|gmail\\.com|hotmail\\.com|outlook\\.com|edu\\.mx)$";
    private final String NAME_REGEX = "^(?!.*[\\!\\#\\$%\\&'\\(\\)\\*\\+\\-\\.,\\/\\:\\;<\\=\\>\\?\\@\\[\\\\\\]\\^_`\\{\\|\\}\\~])(?!.*  )(?!^ $)(?!.*\\d)^.{1,45}$";
    private final String PASSWORD_REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
    private final String SHORT_RANGE = "^[\\p{L}0-9\\s]{3,45}$";
    private final String LONG_RANGE = "^.{3,255}$";
    private final String PHONE_NUMBER_REGEX = "^\\d{10}$";
    private final String ENROLLMENT_REGEX = "^S\\d{8}$";
    private final String TEXT_REGEX = "^[\\p{L}0-9\\s,\\.\\:]+$";
    private final String DAYS_OF_WEEK_REGEX = "^(Lunes|Martes|Miércoles|Miercoles|Jueves|Viernes|Sábado|Sabado|Domingo)(,\\s*"
    + "(Lunes|Martes|Miércoles|Miercoles|Jueves|Viernes|Sábado|Sabado|Domingo))*$";
    


    public void checkEmail(String eMail) {
        Pattern pattern = Pattern.compile(EMAIL_REGEX);
        if (eMail != null) {
            Matcher matcher = pattern.matcher(eMail);
            if (matcher.matches()) {
                return;
            }
        }
        throw new IllegalArgumentException("El Email debe contener las siguientes características:\n"
                + "1.- No debe contener espacios en blanco\n"
                + "2.- Solo los siguientes dominios son permitidos: (@uv.mx, @estudiantes.uv.mx, "
                + "@gmail.com, @hotmail.com, @outlook.com, @edu.mx)\n");
    }

    public void checkName(String name) {
        Pattern pattern = Pattern.compile(NAME_REGEX);
        if (name != null) {
            Matcher matcher = pattern.matcher(name);
            if (matcher.matches()) {
                return;
            }
        }
        throw new IllegalArgumentException("El nombre debe tener las siguientes características:\n"
                + "1.- Debe contener de 3 a 45 caractéres como máximo\n"
                + "2.- No puede contener más de 2 espacios en blanco juntos\n"
                + "3.- No puede tener solo espacios en blanco\n"
                + "4.- No debe contener los siguientes símbolos: (!, \", #, $, %, &, ', (, ), *, +, "
                + ",, -, ., /, :, ;, <, =, >, ?, @, [, \\, ], ^, _, `, {, |, }, ~)\n");
    }

    public void checkPhoneNumber(String phoneNumber) {
        Pattern pattern = Pattern.compile(PHONE_NUMBER_REGEX);
        if (phoneNumber != null) {
            Matcher matcher = pattern.matcher(phoneNumber);
            if (matcher.matches()) {
                return;
            }
        }
        throw new IllegalArgumentException("El número telefónico debe tener las siguientes características:\n"
                + "1.- Debe contener un total de 10 dígitos\n"
                + "2.- Solo debe contener números\n"
                + "3.- No puede tener espacios en blanco\n"
                + "4.- No debe contener los siguientes símbolos: (!, \", #, $, %, &, ', (, ), *, +, "
                + ",, -, ., /, :, ;, <, =, >, ?, @, [, \\, ], ^, _, `, {, |, }, ~)\n");

    }

    public void checkShortRange(String stringForCheck) {
        Pattern pattern = Pattern.compile(SHORT_RANGE);
        if (stringForCheck != null) {
            Matcher matcher = pattern.matcher(stringForCheck);
            if (matcher.matches()) {
                return;
            }
        }
        throw new IllegalArgumentException("El campo debe tener las siguientes características:\n"
                + "1.- Debe contener al un rango de 3 a 45 caractéres \n");
    }

    public void checkLongRange(String stringForCheck) {
        Pattern pattern = Pattern.compile(LONG_RANGE);
        if (stringForCheck != null) {
            Matcher matcher = pattern.matcher(stringForCheck);
            if (matcher.matches()) {
                return;
            }
        }
        throw new IllegalArgumentException("El campo debe tener las siguientes "
                + "características:\n1.- Debe contener al un rango de 3 a 255 caractéres \n");
    }

    public void checkEnrollment(String enrollment) {
        Pattern pattern = Pattern.compile(ENROLLMENT_REGEX);
        if (enrollment != null) {
            Matcher matcher = pattern.matcher(enrollment);
            if (matcher.matches()) {
                return;
            }
        }
        throw new IllegalArgumentException("La matricula debe comenzar por S "
                        + "mayuscula y estar seguida de 8 numeros");
    }

    public void checkPassword(String password) {
        Pattern pattern = Pattern.compile(PASSWORD_REGEX);
        if (password != null) {
            Matcher matcher = pattern.matcher(password);
            if (matcher.matches()) {
                return;
            }
        }
        throw new IllegalArgumentException("La contraseña debe tener las siguientes características:\n"
                + "1.- Debe contener al menos 8 caracteres.\n"
                + "2.- Debe contener al menos una letra minúscula.\n"
                + "3.- Debe contener al menos una letra mayúscula.\n"
                + "4.- Debe contener al menos un número.\n"
                + "5.- Debe contener  al menos un carácter especial.");
    }
    
    public void checkText(String text) {
        Pattern pattern = Pattern.compile(TEXT_REGEX);
        if (text != null) {
            Matcher matcher = pattern.matcher(text);
            if (matcher.matches()) {                
                checkLongRange(text);
                return;
            }
        }
        throw new IllegalArgumentException("EL texto debe tener las siguientes características:\n"
    + "1.- Puede contener letras, números, espacios, comas, puntos y dos puntos.\n"
    + "2.- No puede contener más de 2 espacios en blanco juntos\n"
    + "3.- No puede tener solo espacios en blanco\n"
    + "4.- No debe contener los siguientes símbolos: (!, \", #, $, %, &, ', (, ), *,"
    + " +, -, /, ;, <, =, >, ?, @, [, \\, ], ^, _, `, {, |, }, ~)\n");

    }
    
    public void checkDaysOfWeek(String days) {
        Pattern pattern = Pattern.compile(DAYS_OF_WEEK_REGEX);
        if (days != null) {
            Matcher matcher = pattern.matcher(days);
            if (matcher.matches()) {                
                String[] daysArray = days.split("\\s*,\\s*");
                Set<String> daysSet = new HashSet<>();
                for (String day : daysArray) {
                    if (!daysSet.add(day)) {
                        throw new IllegalArgumentException("No se permiten días repetidos: " + day);
                    }
                }
                return;
            }
        }
        throw new IllegalArgumentException("Los días deben ser uno o más de los siguientes separados por comas: Lunes, Martes, Miércoles, Jueves, Viernes, Sábado, Domingo.");
    }
    
    public void checkScheduleFormat(String time) {
        if (time != null) {
            String[] timeParts = time.split("-");
            if (timeParts.length == 2) {
                String startTime = timeParts[0];
                String endTime = timeParts[1];
                if (startTime.matches("^\\d{2}:\\d{2}$") && endTime.matches("^\\d{2}:\\d{2}$")) {                
                    String[] start = startTime.split(":");
                    String[] end = endTime.split(":");
                    int startHour = Integer.parseInt(start[0]);
                    int startMinute = Integer.parseInt(start[1]);
                    int endHour = Integer.parseInt(end[0]);
                    int endMinute = Integer.parseInt(end[1]);                               
                    if (startHour < endHour || (startHour == endHour && startMinute < endMinute)) {
                        return;
                    }
                }
            }
        }
        throw new IllegalArgumentException("El formato de hora debe ser '00:00-00:00' y la hora de inicio debe ser anterior a la hora de finalización.");
    }
    
}
