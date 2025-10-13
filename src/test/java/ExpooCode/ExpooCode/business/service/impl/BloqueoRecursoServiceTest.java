package ExpooCode.ExpooCode.business.service.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Unit Tests para BloqueoRecursoServiceImpl
 *
 * OBJETIVO: Probar la lógica de negocio del servicio de forma aislada
 * - No requiere base de datos
 * - No requiere Spring Context
 * - Usa mocks para dependencias
 * - Ejecución rápida
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("BloqueoRecursoService - Unit Tests")
public class BloqueoRecursoServiceTest {
    // DEPENDENCIAS MOCKEADAS
    @Mock
    private BloqueoDao bloqueoDao; //No se ha hecho el controlador

    // CLASE BAJO PRUEBA (System Under Test)
    @InjectMocks
    private BloqueoRecursoServiceImpl bloqueoRecursoServiceImpl;
}
