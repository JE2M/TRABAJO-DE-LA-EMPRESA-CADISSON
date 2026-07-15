package com.cadisson.cadisson.DTO;

public class ContactoClienteDTO {

    private String whatsappUrl;
    private String correoUrl;

    public ContactoClienteDTO() {
    }

    public String getWhatsappUrl() {
        return whatsappUrl;
    }

    public void setWhatsappUrl(String whatsappUrl) {
        this.whatsappUrl = whatsappUrl;
    }

    public String getCorreoUrl() {
        return correoUrl;
    }

    public void setCorreoUrl(String correoUrl) {
        this.correoUrl = correoUrl;
    }
}