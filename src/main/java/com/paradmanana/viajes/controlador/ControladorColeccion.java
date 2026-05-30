package com.paradmanana.viajes.controlador;

import com.paradmanana.viajes.dominio.aventura.CatalogoObjetos;
import com.paradmanana.viajes.dominio.aventura.ObjetoTemporal;
import com.paradmanana.viajes.servicio.ServicioAventuras;
import com.paradmanana.viajes.servicio.ServicioPosicionViajero;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Mi Colección Temporal y casa de empeños (reventa en el presente).
 */
@Controller
public class ControladorColeccion {

    private final ServicioAventuras servicioAventuras;
    private final ServicioPosicionViajero servicioPosicion;

    public ControladorColeccion(ServicioAventuras servicioAventuras,
                                ServicioPosicionViajero servicioPosicion) {
        this.servicioAventuras = servicioAventuras;
        this.servicioPosicion = servicioPosicion;
    }

    @GetMapping("/coleccion")
    public String coleccion(Model modelo) {
        modelo.addAttribute("piezasMuseo", CatalogoObjetos.piezasDelMuseo());
        modelo.addAttribute("inventarioDetalle", servicioAventuras.obtenerInventario());
        modelo.addAttribute("vendibles", servicioAventuras.listarVendiblesEnInventario());
        modelo.addAttribute("enPresente", servicioPosicion.estaEnPresente());
        return "coleccion";
    }

    @PostMapping("/coleccion/vender")
    public String vender(@RequestParam String objetoId, RedirectAttributes redireccion) {
        var precio = servicioAventuras.venderObjeto(objetoId);
        if (precio.isEmpty()) {
            redireccion.addFlashAttribute("mensajeError",
                    "No puedes vender ese objeto ahora (solo en el presente y si lo tienes en el inventario).");
        } else {
            String nombre = CatalogoObjetos.buscarPorId(objetoId)
                    .map(ObjetoTemporal::nombre)
                    .orElse(objetoId);
            redireccion.addFlashAttribute("mensajeExito",
                    "💰 Has vendido «" + nombre + "» por " + precio.get() + " créditos en la casa de empeños.");
        }
        return "redirect:/coleccion";
    }
}
