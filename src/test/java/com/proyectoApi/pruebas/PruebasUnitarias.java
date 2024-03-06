package com.proyectoApi.pruebas;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.dwes.security.SecurityApplication;
import com.dwes.security.entities.Oferta;
import com.dwes.security.entities.Usuario;
import com.dwes.security.error.exception.OfertaNotFoundException;
import com.dwes.security.repository.OfertaRepository;
import com.dwes.security.repository.UserRepository;
import com.dwes.security.service.impl.OfertaServiceImpl;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = SecurityApplication.class)
class PruebasUnitarias {

	@InjectMocks
	private OfertaServiceImpl ofertaService;

	@Mock
	private OfertaRepository ofertaRepository;

	@Mock
	private UserRepository userRepository;


	@Test
	public void testGuardarOferta() {
		// Configuración del repositorio y del usuario mock
		Oferta ofertaMock = mock(Oferta.class);
		Usuario usuarioMock = mock(Usuario.class);
		when(userRepository.findByEmail(anyString())).thenReturn(java.util.Optional.of(usuarioMock));
		when(ofertaRepository.save(any())).thenReturn(ofertaMock);

		// Ejecutar el método del servicio
		ofertaService.guardarOferta(new Oferta(), "username");

		// Verificar que se llama al repositorio y al usuarioRepository
		verify(ofertaRepository).save(any());
		verify(userRepository).findByEmail("username");
	}
	
	@Test
    public void testListarTodasLasOfertas() {
        // Configuración del repositorio mock
        Page<Oferta> ofertasMock = mock(Page.class);
        when(ofertaRepository.findAll(any(Pageable.class))).thenReturn(ofertasMock);

        // Ejecutar el método del servicio
        Page<Oferta> result = ofertaService.listarTodasLasOfertas(Pageable.unpaged());

        // Verificar que se llama al repositorio y se devuelve la lista de ofertas
        verify(ofertaRepository).findAll(any(Pageable.class));
        assertSame(ofertasMock, result);
    }
	
	@Test
    public void testObtenerOfertaPorIdExistente() {
        // Configuración del repositorio mock
        Oferta ofertaMock = mock(Oferta.class);
        when(ofertaRepository.findById(1L)).thenReturn(java.util.Optional.of(ofertaMock));

        // Ejecutar el método del servicio
        Oferta result = ofertaService.obtenerOfertaPorId(1L);

        // Verificar que se llama al repositorio y se devuelve la oferta
        verify(ofertaRepository).findById(1L);
        assertSame(ofertaMock, result);
    }
	
	@Test
    public void testObtenerOfertaPorIdNoExistente() {
        // Configuración del repositorio mock
        when(ofertaRepository.findById(1L)).thenReturn(java.util.Optional.empty());

        // Ejecutar el método del servicio y verificar la excepción esperada
        assertThrows(OfertaNotFoundException.class, () -> ofertaService.obtenerOfertaPorId(1L));

        // Verificar que se llama al repositorio
        verify(ofertaRepository).findById(1L);
    }
	

	
}