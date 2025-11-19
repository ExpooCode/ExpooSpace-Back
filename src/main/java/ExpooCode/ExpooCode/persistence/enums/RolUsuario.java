package ExpooCode.ExpooCode.persistence.enums;

public enum RolUsuario {
    Administrador,
    Afiliado,
    Visitante;

    public String getAuthority() {
        return "ROLE_" + this.name().toUpperCase();
    }
}
