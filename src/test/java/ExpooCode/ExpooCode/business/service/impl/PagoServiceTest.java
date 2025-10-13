package ExpooCode.ExpooCode.business.service.impl;

import ExpooCode.ExpooCode.persistence.dao.PagoDao;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Unit Tests para PagoServiceImpl
 *
 * OBJETIVO: Probar la lógica de negocio del servicio de forma aislada
 * - No requiere base de datos
 * - No requiere Spring Context
 * - Usa mocks para dependencias
 * - Ejecución rápida
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("PagoService - Unit Tests")
public class PagoServiceTest {
    // DEPENDENCIAS MOCKEADAS
    @Mock
    private PagoDao pagoDao;

    // CLASE BAJO PRUEBA (System Under Test)
    @InjectMocks
    private PagoServiceImpl pagoService;
}
