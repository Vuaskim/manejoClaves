package com.example.tareakoko;

public class Contraseñas {
    private String nombreUsuario;
    private String contraseña;

    public Contraseñas(String nombreUsuario, String contraseña) {
        this.nombreUsuario = nombreUsuario;
        this.contraseña = contraseña;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public String getContraseña() {
        return contraseña;
    }
}
