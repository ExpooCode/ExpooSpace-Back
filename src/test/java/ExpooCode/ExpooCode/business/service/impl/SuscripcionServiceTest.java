package ExpooCode.ExpooCode.business.service.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Unit Tests para SuscripcionServiceImpl
 *
 * OBJETIVO: Probar la lógica de negocio del servicio de forma aislada
 * - No requiere base de datos
 * - No requiere Spring Context
 * - Usa mocks para dependencias
 * - Ejecución rápida
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("SuscripcionService - Unit Tests")
public class SuscripcionServiceTest {
    // DEPENDENCIAS MOCKEADAS
    @Mock
    private ProductDAO productDAO; //Todavía no está hecho el controlador entonces no hay Dao

    // CLASE BAJO PRUEBA (System Under Test)
    @InjectMocks
    private SuscripcionServiceImpl suscripcionService;
}
