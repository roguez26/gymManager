package mx.fei.gymmanagerapp.gui.views;

import mx.fei.gymmanagerapp.logic.implementations.Status;

public class AlertMessage {
 
    private String content = "La operación se ha realizado exitosamente";
    private Status alertType = Status.SUCCESS;

    public AlertMessage(String content, Status alertType) {
        if (content.isEmpty() || alertType == null) {
            throw new IllegalArgumentException("Debes ingresar un mensaje y un estádo en el mensaje de alerta");
        }
        this.content = content;
        this.alertType = alertType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Status getAlertType() {
        return alertType;
    }

    public void setAlertType(Status alertType) {
        this.alertType = alertType;
    }
}
