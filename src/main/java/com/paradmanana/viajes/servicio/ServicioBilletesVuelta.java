package com.paradmanana.viajes.servicio;

import com.paradmanana.viajes.dominio.Billete;
import com.paradmanana.viajes.dominio.Era;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;

/**
 * Billetes de vuelta ya pagados pero aún no utilizados.
 * <p>
 * Si ida y vuelta se compran juntas, la ida se ejecuta al confirmar y la vuelta
 * queda aquí hasta que el viajero pulse «Usar billete de vuelta».
 * </p>
 */
@Service
@SessionScope
public class ServicioBilletesVuelta {

    private final Map<Era, Billete> billetes = new EnumMap<>(Era.class);

    public void guardar(Billete billete) {
        billetes.put(billete.era(), billete);
    }

    public boolean tiene(Era era) {
        return billetes.containsKey(era);
    }

    public Optional<Billete> obtener(Era era) {
        return Optional.ofNullable(billetes.get(era));
    }

    public Map<Era, Billete> obtenerTodos() {
        return Collections.unmodifiableMap(billetes);
    }

    /** Consume el billete y lo elimina del almacén. */
    public Optional<Billete> consumir(Era era) {
        return Optional.ofNullable(billetes.remove(era));
    }

    public void vaciar() {
        billetes.clear();
    }
}
