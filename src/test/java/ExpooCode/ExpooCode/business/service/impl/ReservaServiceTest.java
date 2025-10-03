package ExpooCode.ExpooCode.business.service.impl;

import ExpooCode.ExpooCode.business.service.ReservaService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
/**
 * Unit Tests para ReservaServiceImpl
 *
 * OBJETIVO: Probar la lógica de negocio del servicio de forma aislada
 * - No requiere base de datos
 * - No requiere Spring Context
 * - Usa mocks para dependencias
 * - Ejecución rápida
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("ReservaService - Unit Tests")
public class ReservaServiceTest {
    // DEPENDENCIAS MOCKEADAS
    @Mock
    private ProductDAO productDAO;

    // CLASE BAJO PRUEBA (System Under Test)
    @InjectMocks
    private ReservaService reservaService;
}