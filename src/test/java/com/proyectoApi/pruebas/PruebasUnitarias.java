package com.proyectoApi.pruebas;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.dwes.security.controller.OfertaController;
import com.dwes.security.entities.Oferta;
import com.dwes.security.service.OfertaService;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class PruebasUnitarias {

	@InjectMocks
    private OfertaController ofertaController;

    @Mock
    private OfertaService ofertaService;

    @BeforeEach
    public void setUp() {
        // Configuración adicional si es necesaria
    }

    @Test
    public void testListarTodasLasOfertas() {
        // Configuración de datos de prueba
    	Page<Oferta> mockOfertas = new PageImpl<>(mockOfertasList, PageRequest.of(0, 10), mockOfertasList.size());
        
        when(ofertaService.listarTodasLasOfertas(any(Pageable.class))).thenReturn(mockOfertas);

        // Llamada al método del controlador
        ResponseEntity<Page<Oferta>> response = ofertaController.listarTodasLasOfertas(0, 10, null, null);

        // Verificación de resultados
        verify(ofertaService).listarTodasLasOfertas(any(Pageable.class));
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockOfertas, response.getBody());
    }

    // Puedes agregar pruebas similares para los otros casos (filtrado por usuario, filtrado por precio máximo)
}