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
                                    +50 GT y te guardas el ronchón como souvenir científico.
                                    """.trim(),
                                    new EfectosOpcion(
                                            50,
                                            false,
                                            Optional.empty(),
                                            Optional.of(CatalogoObjetos.RONCHON_COSMICO.id()),
                                            0,
                                            false,
                                            Optional.empty()
                                    )
                            ),
                            new OpcionAventura(
                                    "aplastar",
                                    "Aplastarlo de un manotazo",
                                    """
                                    ¡PARADOJA TRÁGICA! Al volver al presente, descubres que los humanos ahora \
                                    tienen tres ojos y el café no existe. El sistema te cobra una multa de \
                                    -100 GT por el seguro del Efecto Mariposa.
                                    """.trim(),
                                    new EfectosOpcion(
                                            -100,
                                            true,
                                            Optional.empty(),
                                            Optional.empty(),
                                            0,
                                            false,
                                            Optional.of(TipoParadoja.EFECTO_MARIPOSA)
                                    )
                            )
                    )
            ),

            new EscenaAventura(
                    "dino-spa-volcan",
                    Era.DINOSAURIOS,
                    "Balneario jurásico premium",
                    "Un brontosaurio te invita a su spa de lodo termal. La recepción es una roca.",
                    """
                    Encuentras un manantial humeante donde tres herbívoros de tamaño industrial disfrutan \
                    de un «circuito termal» de barro. Un brontosaurio te mira con ojos de quien lleva \
                    esperando masajista desde el Jurásico Medio. Hay toallas de hojas de helecho.
                    """.trim(),
                    List.of(
                            new OpcionAventura(
                                    "bano-de-lodo",
                                    "Aceptar el circuito de lodo (es gratis, no hay caja)",
                                    """
                                    Sales relajado, oliendo a spa prehistórico. Te regalan un tarro de \
                                    lodo termal como producto promocional y +35 GT por reseña \
                                    positiva en la tablilla de la cueva.
                                    """.trim(),
                                    new EfectosOpcion(
                                            35,
                                            false,
                                            Optional.empty(),
                                            Optional.of(CatalogoObjetos.LODO_VOLCANICO.id()),
                                            0,
                                            false,
                                            Optional.empty()
                                    )
                            ),
                            new OpcionAventura(
                                    "gritar-parejo",
                                    "Gritar «¡PAREJO, QUE ME QUEMO!» y echarles agua fría",
                                    """
                                    El shock térmico provoca una estampida que rediseña el relieve \
                                    continental. Vuelves al presente y el GPS ya no reconoce Portugal. \
                                    Multa de -90 GT y paradoja activada.
                                    """.trim(),
                                    new EfectosOpcion(
                                            -90,
                                            true,
                                            Optional.empty(),
                                            Optional.empty(),
                                            0,
                                            false,
                                            Optional.of(TipoParadoja.EFECTO_MARIPOSA)
                                    )
                            )
                    )
            ),

            new EscenaAventura(
                    "dino-pterodactil-taxi",
                    Era.DINOSAURIOS,
                    "Taxi volador sin licencia",
                    "Un pterodáctilo te ofrece transporte aéreo. No tiene taxímetro. Sí tiene actitud.",
                    """
                    Un pterodáctilo aterriza frente a ti, abre un ala como puerta de coche y gruñe \
                    un precio en chirridos. Parece un Uber del Cretácico: sin cinturón, sin casco y \
                    con olor a pescado caducado hace setenta millones de años.
                    """.trim(),
                    List.of(
                            new OpcionAventura(
                                    "subir-al-taxi",
                                    "Subir y agarrarte fuerte",
                                    """
                                    El vuelo es terrorífico pero eficaz. Aterrizas cerca de un nido \
                                    vacío con una pluma suelta y +45 GT que el pterodáctilo \
                                    «no declarará a Hacienda Cretácica».
                                    """.trim(),
                                    new EfectosOpcion(
                                            45,
                                            false,
                                            Optional.empty(),
                                            Optional.of(CatalogoObjetos.PLUMA_PTERODACTILO.id()),
                                            0,
                                            false,
                                            Optional.empty()
                                    )
                            ),
                            new OpcionAventura(
                                    "pedir-factura",
                                    "Exigir factura y cinturón de seguridad homologado",
                                    """
                                    El pterodáctilo se siente acosado por la burocracia futura, despega \
                                    en vertical y choca contra un meteorito… que no debía desviarse \
                                    tres centímetros. Paradoja temporal: -110 GT.
                                    """.trim(),
                                    new EfectosOpcion(
                                            -110,
                                            true,
                                            Optional.empty(),
                                            Optional.empty(),
                                            0,
                                            false,
                                            Optional.of(TipoParadoja.EFECTO_MARIPOSA)
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
                                    Ganas +200 GT al vender los denarios en el presente, pero recibes \
                                    un aviso de la agencia por introducir tecnología del siglo XXI en el \
                                    Imperio Romano. Te llevas un denario como souvenir.
                                    """.trim(),
                                    new EfectosOpcion(
                                            200,
                                            false,
                                            Optional.empty(),
                                            Optional.of(CatalogoObjetos.DENARIO_ROMANO.id()),
                                            0,
                                            false,
                                            Optional.empty()
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
                                            true,
                                            Optional.empty()
                                    )
                            ),
                            new OpcionAventura(
                                    "mostrar-youtube",
                                    "Enseñarle un vídeo de gladiadores en YouTube",
                                    """
                                    El centurión ve un anuncio de seguros antes del combate y decide \
                                    que los dioses son publicidad. Roma inventa el marketing quince \
                                    siglos antes de tiempo. Paradoja histórica: -95 GT.
                                    """.trim(),
                                    new EfectosOpcion(
                                            -95,
                                            true,
                                            Optional.empty(),
                                            Optional.empty(),
                                            0,
                                            false,
                                            Optional.of(TipoParadoja.PARADOJA_HISTORICA)
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
                                            false,
                                            Optional.empty()
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
                            ),
                            new OpcionAventura(
                                    "profetizar-futuro",
                                    "Escribir en el mapa «Roma caerá en 476 d.C.» como broma",
                                    """
                                    Un augur lee tu grafiti, entra en pánico y quema el Senado por \
                                    accidente… el martes. Has adelantado tres crisis políticas y \
                                    activado una paradoja histórica. -85 GT de arreglos.
                                    """.trim(),
                                    new EfectosOpcion(
                                            -85,
                                            true,
                                            Optional.empty(),
                                            Optional.of(CatalogoObjetos.PERGAMINO_FALSO.id()),
                                            0,
                                            false,
                                            Optional.of(TipoParadoja.PARADOJA_HISTORICA)
                                    )
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
                                    Te escabulles con +300 GT en metálico temporal. La agencia \
                                    archiva el incidente como «turismo cultural intensivo».
                                    """.trim(),
                                    EfectosOpcion.creditos(300)
                            ),
                            new OpcionAventura(
                                    "dejar-todo",
                                    "Dejarlo intacto por respeto histórico",
                                    """
                                    Cierras la losa sin tocar nada. Un centurión fantasma asiente desde \
                                    las sombras. La agencia te bonifica con +75 GT por ética excepcional.
                                    """.trim(),
                                    EfectosOpcion.creditos(75)
                            ),
                            new OpcionAventura(
                                    "selfie-con-estatua",
                                    "Hacerte un selfie tocando la estatua del César",
                                    """
                                    La estatua cae, aplasta una urna votiva y cambia el resultado de \
                                    una elección municipal del 98 a.C. Te escabulles con una ánfora \
                                    rota y una paradoja histórica de manual: -100 GT.
                                    """.trim(),
                                    new EfectosOpcion(
                                            -100,
                                            true,
                                            Optional.empty(),
                                            Optional.of(CatalogoObjetos.ANFORA_ROTA.id()),
                                            0,
                                            false,
                                            Optional.of(TipoParadoja.PARADOJA_HISTORICA)
                                    )
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
                                    "Prestarle 50 GT",
                                    """
                                    Tu saldo baja en -50 GT, pero el chaval te da las gracias y te \
                                    regala un almanaque deportivo del futuro. Puedes consumirlo en el carrito \
                                    para un 20 % de descuento en tu próximo viaje.
                                    """.trim(),
                                    new EfectosOpcion(
                                            -50,
                                            false,
                                            Optional.empty(),
                                            Optional.of(CatalogoObjetos.ALMANAQUE_3000.id()),
                                            0,
                                            false,
                                            Optional.empty()
                                    )
                            ),
                            new OpcionAventura(
                                    "negar-parentesco",
                                    "Decirle que no le conoces y negarlo todo",
                                    """
                                    El chaval se enfada, te borra de su árbol genealógico digital y empiezas \
                                    a notar que tus manos se vuelven transparentes estilo Marty McFly. Tienes \
                                    que pagar -80 GT médicos de urgencia para estabilizar tu línea genética.
                                    """.trim(),
                                    new EfectosOpcion(
                                            -80,
                                            true,
                                            Optional.empty(),
                                            Optional.empty(),
                                            0,
                                            false,
                                            Optional.of(TipoParadoja.PARADOJA_GENETICA)
                                    )
                            )
                    )
            ),

            new EscenaAventura(
                    "3000-cafe-prohibido",
                    Era.ANO_3000,
                    "El café clandestino",
                    "En el año 3000 el café es ilegal. Huele a contrabando delicioso.",
                    """
                    Un dron susurra que hay «la buena stuff» en un callejón sin cámaras. Dentro, un \
                    barista cyborg prepara espresso con granos rescatados del siglo XXI. La ley dice \
                    que la cafeína altera la línea temporal emocional de los ciudadanos.
                    """.trim(),
                    List.of(
                            new OpcionAventura(
                                    "comprar-cafe",
                                    "Comprar un café doble sin preguntar",
                                    """
                                    El sabor te hace llorar de nostalgia. Te venden una bebida neural \
                                    de recuerdo por -30 GT. Puedes consumirla en el carrito \
                                    para un 15 % de descuento.
                                    """.trim(),
                                    new EfectosOpcion(
                                            -30,
                                            false,
                                            Optional.empty(),
                                            Optional.of(CatalogoObjetos.BEBIDA_NEURAL.id()),
                                            0,
                                            false,
                                            Optional.empty()
                                    )
                            ),
                            new OpcionAventura(
                                    "denunciar-bar",
                                    "Denunciar el bar a la Policía Temporal del Bienestar",
                                    """
                                    Te denuncian a ti por exceso de hipocresía. Te multan, borran \
                                    tu historial de pedidos de café y activan paradoja genética \
                                    porque «alguien así no puede tener abuelos». -70 GT.
                                    """.trim(),
                                    new EfectosOpcion(
                                            -70,
                                            true,
                                            Optional.empty(),
                                            Optional.empty(),
                                            0,
                                            false,
                                            Optional.of(TipoParadoja.PARADOJA_GENETICA)
                                    )
                            )
                    )
            ),

            new EscenaAventura(
                    "3000-examen-hover",
                    Era.ANO_3000,
                    "Examen teórico de hoverboard",
                    "Para circular necesitas aprobar un test. Las preguntas son absurdas.",
                    """
                    En la Oficina de Movilidad Antigravitatoria te obligan a un examen teórico. \
                    Pregunta 1: «Si tu hoverboard se queja, ¿le pones un sticker o terapia?». \
                    Hay un chófer dormido y un holograma de un inspector muy serio.
                    """.trim(),
                    List.of(
                            new OpcionAventura(
                                    "estudiar-manual",
                                    "Estudiar el manual de 4.000 páginas holográficas",
                                    """
                                    Apruebas raspando y recibes +40 GT de devolución de tasas. \
                                    Te regalan una pegatina oficial que nadie entiende pero todos \
                                    miran en las pasarelas.
                                    """.trim(),
                                    new EfectosOpcion(
                                            40,
                                            false,
                                            Optional.empty(),
                                            Optional.of(CatalogoObjetos.STICKER_HOVER.id()),
                                            0,
                                            false,
                                            Optional.empty()
                                    )
                            ),
                            new OpcionAventura(
                                    "copiar-con-telepatia",
                                    "Intentar copiar telepáticamente al chófer dormido",
                                    """
                                    Copias sus respuestas… y también su sueño de ser un pato. \
                                    Colapsas el sistema de exámenes del distrito 7. Paradoja \
                                    genética leve pero documentada: -90 GT.
                                    """.trim(),
                                    new EfectosOpcion(
                                            -90,
                                            true,
                                            Optional.empty(),
                                            Optional.empty(),
                                            0,
                                            false,
                                            Optional.of(TipoParadoja.PARADOJA_GENETICA)
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
