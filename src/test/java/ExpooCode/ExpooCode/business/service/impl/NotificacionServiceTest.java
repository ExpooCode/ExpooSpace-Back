package ExpooCode.ExpooCode.business.service.impl;

import ExpooCode.ExpooCode.persistence.dao.NotificacionDao;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Unit Tests para NotificacionServiceImpl
 *
 * OBJETIVO: Probar la lógica de negocio del servicio de forma aislada
 * - No requiere base de datos
 * - No requiere Spring Context
 * - Usa mocks para dependencias
 * - Ejecución rápida
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("NotificacionService - Unit Tests")
public class NotificacionServiceTest {
    // DEPENDENCIAS MOCKEADAS
    @Mock
    private NotificacionDao notificacionDao;

    // CLASE BAJO PRUEBA (System Under Test)
    @InjectMocks
    private NotificacionServiceImpl notificacionService;
}
