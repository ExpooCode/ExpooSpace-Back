package ExpooCode.ExpooCode.business.service;

import ExpooCode.ExpooCode.persistence.entity.Usuario;
import java.util.List;

public interface UsuarioService {

    /**
     * Listar todos los usuarios
     *
     * @return Lista completa de usuarios
     */
    List<Usuario> getAllUsuarios();

    /**
     * Crear un nuevo usuario
     *
     * @param nombre   Nombre del usuario
     * @param email    Correo electrónico del usuario
     * @param password Contraseña del usuario
     * @return Usuario creado con ID generado
     * @throws IllegalArgumentException Si los datos no son válidos
     */
    Usuario createUsuario(String nombre, String email, String password);

    /**
     * Buscar un usuario por ID
     *
     * @param id ID del usuario
     * @return Usuario encontrado
     * @throws RuntimeException Si el usuario no existe
     */
    Usuario getUsuarioById(Long id);

    /**
     * Actualizar un usuario existente
     *
     * @param id       ID del usuario a actualizar
     * @param nombre   Nuevo nombre
     * @param email    Nuevo correo electrónico
     * @param password Nueva contraseña
     * @return Usuario actualizado
     * @throws RuntimeException Si el usuario no existe
     */
    Usuario updateUsuario(Long id, String nombre, String email, String password);

    /**
     * Eliminar un usuario
     *
     * @param id ID del usuario a eliminar
     * @throws RuntimeException Si el usuario no existe
     */
    void deleteUsuario(Long id);

    /**
     * Iniciar sesión de un usuario
     *
     * @param email    Correo electrónico
     * @param password Contraseña
     * @return true si las credenciales son correctas, false en caso contrario
     */
    boolean login(String email, String password);

    /**
     * Cerrar sesión del usuario actual
     */
    void logout();

    /**
     * Obtener todas las reservas asociadas a un usuario
     *
     * @param id ID del usuario
     * @return Lista de identificadores de reservas del usuario
     */
    List<String> getReservasDeUsuario(Long id);

    /**
     * Obtener todas las suscripciones asociadas a un usuario
     *
     * @param id ID del usuario
     * @return Lista de identificadores de suscripciones del usuario
     */
    List<String> getSuscripcionesDeUsuario(Long id);

    /**
     * Cambiar el estado de un usuario (activo/inactivo, bloqueado, etc.)
     *
     * @param id ID del usuario
     * @return Usuario con el estado actualizado
     * @throws RuntimeException Si el usuario no existe
     */
    Usuario cambiarEstadoUsuario(Long id);
}
