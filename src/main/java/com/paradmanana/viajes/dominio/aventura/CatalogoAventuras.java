package com.paradmanana.viajes.dominio.aventura;

import com.paradmanana.viajes.dominio.Era;

import java.util.List;
import java.util.Optional;

/**
 * Registro central de todas las escenas jugables.
 */
public final class CatalogoAventuras {

    private static final List<EscenaAventura> TODAS = List.of(

            new EscenaAventura(
                    "dino-mosquito",
                    Era.DINOSAURIOS,
                    "El dilema del Mosquito",
                    "Un mosquito prehistórico del tamaño de un puño se posa en tu brazo.",
                    """
                    Apareces entre helechos gigantes. El calor es sofocante. De repente, un mosquito \
                    prehistórico del tamaño de un puño se posa en tu brazo dispuesto a chuparte la sangre. \
                    Sabes que si lo matas, podrías extinguir a la humanidad en el futuro.
                    """.trim(),
                    List.of(
                            new OpcionAventura(
                                    "dejar-picar",
                                    "Dejar que te pique",
                                    """
                                    Te chupa medio litro de sangre y te deja un ronchón cósmico, pero la \
                                    línea temporal sigue intacta. La agencia te premia con un bonus de \
                                    +50 créditos y te guardas el ronchón como souvenir científico.
                                    """.trim(),
                                    new EfectosOpcion(
                                            50,
                                            false,
                                            Optional.empty(),
                                            Optional.of(CatalogoObjetos.RONCHON_COSMICO.id()),
                                            0,
                                            false
                                    )
                            ),
                            new OpcionAventura(
                                    "aplastar",
                                    "Aplastarlo de un manotazo",
                                    """
                                    ¡PARADOJA TRÁGICA! Al volver al presente, descubres que los humanos ahora \
                                    tienen tres ojos y el café no existe. El sistema te cobra una multa de \
                                    -100 créditos por el seguro del Efecto Mariposa.
                                    """.trim(),
                                    new EfectosOpcion(
                                            -100,
                                            true,
                                            Optional.empty(),
                                            Optional.empty(),
                                            0,
                                            false
                                    )
                            )
                    )
            ),

            new EscenaAventura(
                    "roma-taberna",
                    Era.ROMA,
                    "La Taberna del Viajero",
                    "Un centurión borracho confunde tu reloj digital con un artefacto mágico.",
                    """
                    Estás en una ruidosa taberna de la Subura. Un centurión borracho confunde tu reloj \
                    digital con un artefacto mágico de los dioses y te ofrece una bolsa de denarios si \
                    se lo cambias por su gladius (espada).
                    """.trim(),
                    List.of(
                            new OpcionAventura(
                                    "aceptar-trato",
                                    "Aceptar el trato",
                                    """
                                    Ganas +200 créditos al vender los denarios en el presente, pero recibes \
                                    un aviso de la agencia por introducir tecnología del siglo XXI en el \
                                    Imperio Romano. Te llevas un denario como souvenir.
                                    """.trim(),
                                    new EfectosOpcion(
                                            200,
                                            false,
                                            Optional.empty(),
                                            Optional.of(CatalogoObjetos.DENARIO_ROMANO.id()),
                                            0,
                                            false
                                    )
                            ),
                            new OpcionAventura(
                                    "salir-corriendo",
                                    "Explicarle qué es la tecnología y salir corriendo",
                                    """
                                    El centurión piensa que lo estás embrujando y te persigue la guardia \
                                    pretoriana. Consigues activar el botón de pánico de tu billete de vuelta \
                                    justo a tiempo, pero pierdes una bota. Sin penalizaciones económicas.
                                    """.trim(),
                                    new EfectosOpcion(
                                            0,
                                            false,
                                            Optional.empty(),
                                            Optional.of(CatalogoObjetos.BOTA_PERDIDA.id()),
                                            0,
                                            true
                                    )
                            )
                    )
            ),

            new EscenaAventura(
                    "roma-mapas",
                    Era.ROMA,
                    "El Cartógrafo de la Subura",
                    "Un anciano te vende mapas… más o menos fiables del subsuelo romano.",
                    """
                    En un callejón húmedo, un cartógrafo te susurra que conoce túneles bajo el Foro que \
                    ni el Senado ha mapeado. Por unas monedas te ofrece un mapa de cuero con marcas \
                    misteriosas hacia «el tesoro de alguien muy importante».
                    """.trim(),
                    List.of(
                            new OpcionAventura(
                                    "comprar-mapa",
                                    "Comprar el mapa (gratis — ya pagaste suficiente impuestos temporales)",
                                    """
                                    El anciano te entrega el mapa de cuero. Huele a aceite de oliva y \
                                    ambición. Quizá algún día te sirva para algo más que secar mesas.
                                    """.trim(),
                                    new EfectosOpcion(
                                            0,
                                            false,
                                            Optional.empty(),
                                            Optional.of(CatalogoObjetos.MAPA_CUERO.id()),
                                            0,
                                            false
                                    )
                            ),
                            new OpcionAventura(
                                    "ignorar-mapa",
                                    "Ignorar al cartógrafo",
                                    """
                                    Prefieres no fiarte de mapas vendidos en callejones. El anciano se \
                                    encoge de hombros y desaparece entre la niebla de tabernas. \
                                    Nunca sabrás qué había bajo el Foro.
                                    """.trim(),
                                    EfectosOpcion.ninguno()
                            )
                    )
            ),

            new EscenaAventura(
                    "roma-tesoro-cesar",
                    Era.ROMA,
                    "El Tesoro del César",
                    "Una cámara oculta bajo el Foro… si el mapa no miente.",
                    """
                    Siguiendo el mapa de cuero llegas a una losa suelta bajo el Foro. Tras un esfuerzo \
                    digno de gladiador, descubres una cámara con monedas, pergaminos y una placa que dice: \
                    «Propiedad de alguien con mucho ego y pocas facturas pagadas».
                    """.trim(),
                    List.of(
                            new OpcionAventura(
                                    "coger-monedas",
                                    "Coger monedas y salir discretamente",
                                    """
                                    Te escabulles con +300 créditos en metálico temporal. La agencia \
                                    archiva el incidente como «turismo cultural intensivo».
                                    """.trim(),
                                    EfectosOpcion.creditos(300)
                            ),
                            new OpcionAventura(
                                    "dejar-todo",
                                    "Dejarlo intacto por respeto histórico",
                                    """
                                    Cierras la losa sin tocar nada. Un centurión fantasma asiente desde \
                                    las sombras. La agencia te bonifica con +75 créditos por ética excepcional.
                                    """.trim(),
                                    EfectosOpcion.creditos(75)
                            )
                    ),
                    Optional.of(CatalogoObjetos.MAPA_CUERO.id())
            ),

            new EscenaAventura(
                    "3000-tataranieto",
                    Era.ANO_3000,
                    "La paradoja de tu tataranieto",
                    "Un chaval clavado a ti afirma ser tu tataranieto y pide dinero para el parking.",
                    """
                    Caminas por las pasarelas flotantes de Neo-Vigo cuando un chaval clavado a ti se te \
                    acerca corriendo. Dice que es tu tataranieto, que ha reconocido tu cara por las fotos \
                    holográficas de la familia y que necesita dinero para pagar el parking de su hoverboard.
                    """.trim(),
                    List.of(
                            new OpcionAventura(
                                    "prestar-creditos",
                                    "Prestarle 50 créditos",
                                    """
                                    Tu saldo baja en -50 créditos, pero el chaval te da las gracias y te \
                                    regala un almanaque deportivo del futuro. Puedes consumirlo en el carrito \
                                    para un 20 % de descuento en tu próximo viaje.
                                    """.trim(),
                                    new EfectosOpcion(
                                            -50,
                                            false,
                                            Optional.empty(),
                                            Optional.of(CatalogoObjetos.ALMANAQUE_3000.id()),
                                            0,
                                            false
                                    )
                            ),
                            new OpcionAventura(
                                    "negar-parentesco",
                                    "Decirle que no le conoces y negarlo todo",
                                    """
                                    El chaval se enfada, te borra de su árbol genealógico digital y empiezas \
                                    a notar que tus manos se vuelven transparentes estilo Marty McFly. Tienes \
                                    que pagar -80 créditos médicos de urgencia para estabilizar tu línea genética.
                                    """.trim(),
                                    new EfectosOpcion(
                                            -80,
                                            true,
                                            Optional.empty(),
                                            Optional.empty(),
                                            0,
                                            false
                                    )
                            )
                    )
            )
    );

    private CatalogoAventuras() {
    }

    public static List<EscenaAventura> todas() {
        return TODAS;
    }

    public static List<EscenaAventura> porEra(Era era) {
        return TODAS.stream()
                .filter(e -> e.era() == era)
                .toList();
    }

    public static Optional<EscenaAventura> buscarPorId(String id) {
        return TODAS.stream()
                .filter(e -> e.id().equals(id))
                .findFirst();
    }
}
