/**
 * Animación de salto temporal en la página de confirmación.
 *
 * Flujo:
 * 1. Lee destinos desde el template Thymeleaf (#destinosDatos)
 * 2. Anima el contador de años desde el presente hasta cada destino
 * 3. Muestra llegada con emoji, lema y advertencia de la era
 * 4. Oculta el overlay y revela la confirmación de compra
 */
(function () {
    /** Duraciones en ms — ajustar aquí el ritmo global de la animación */
    const TIEMPOS = {
        despegueInicial: 1200,
        saltoCorto: 5000,
        saltoLargo: 7500,
        umbralSaltoLargo: 1_000_000,
        fraccionAntesAdvertencia: 0.35,
        pantallaLlegada: 4000,
        entreSaltos: 1000,
        reintegracion: 1500,
        salidaOverlay: 900
    };

    const contenedor = document.getElementById('saltoTemporal');
    if (!contenedor) return;

    const presente = parseInt(contenedor.dataset.presente, 10);
    const destinos = leerDestinos();
    const confirmacion = document.getElementById('confirmacionContenido');

    if (destinos.length === 0) {
        document.body.classList.remove('salto-temporal-activo');
        if (confirmacion) {
            confirmacion.classList.remove('confirmacion--oculta');
            confirmacion.classList.add('confirmacion--visible');
        }
        contenedor.remove();
        return;
    }

    const elAno = document.getElementById('saltoAno');
    const elUnidad = document.getElementById('saltoUnidad');
    const elFase = document.getElementById('saltoFase');
    const elProgreso = document.getElementById('saltoProgreso');
    const elLlegada = document.getElementById('saltoLlegada');
    const elEmoji = document.getElementById('saltoEmoji');
    const elNombre = document.getElementById('saltoNombre');
    const elEtiqueta = document.getElementById('saltoEtiqueta');
    const elLema = document.getElementById('saltoLema');
    const elAdvertencia = document.getElementById('saltoAdvertencia');

    function leerDestinos() {
        const plantilla = document.getElementById('destinosDatos');
        const raiz = plantilla ? plantilla.content : document;
        const nodos = raiz.querySelectorAll('[data-valor]');
        return Array.from(nodos).map(n => ({
            valor: parseInt(n.dataset.valor, 10),
            etiqueta: n.dataset.etiqueta,
            nombre: n.dataset.nombre,
            emoji: n.dataset.emoji,
            color: n.dataset.color || '#00d4ff',
            lema: n.dataset.lema || '',
            claseCss: n.dataset.claseCss || '',
            advertencia: n.dataset.advertencia || '',
            advertencias: n.dataset.advertencias
                ? n.dataset.advertencias.split('|').filter(Boolean)
                : []
        }));
    }

    function formatearAno(valor) {
        if (valor < 0) {
            return Math.abs(valor).toLocaleString('es-ES') + ' a.C.';
        }
        return valor.toLocaleString('es-ES') + ' d.C.';
    }

    function separarAnoUnidad(etiqueta) {
        if (etiqueta.includes('a.C.')) {
            return { numero: etiqueta.replace(' a.C.', ''), unidad: 'a.C.' };
        }
        return { numero: etiqueta.replace(' d.C.', ''), unidad: 'd.C.' };
    }

    function esperar(ms) {
        return new Promise(resolve => setTimeout(resolve, ms));
    }

    function mostrarTexto(el, texto) {
        if (!el) return;
        if (texto) {
            el.textContent = texto;
            el.hidden = false;
        } else {
            el.textContent = '';
            el.hidden = true;
        }
    }

    function duracionSalto(desde, hasta) {
        const distancia = Math.abs(hasta - desde);
        return distancia >= TIEMPOS.umbralSaltoLargo
            ? TIEMPOS.saltoLargo
            : TIEMPOS.saltoCorto;
    }

    function animarAno(desde, hasta, duracion, dest) {
        return new Promise(resolve => {
            const inicio = performance.now();
            const delta = hasta - desde;
            let advertenciaMostrada = false;

            function frame(ahora) {
                const t = Math.min((ahora - inicio) / duracion, 1);
                const ease = t < 0.5 ? 4 * t * t * t : 1 - Math.pow(-2 * t + 2, 3) / 2;
                const actual = Math.round(desde + delta * ease);
                const partes = separarAnoUnidad(formatearAno(actual));
                elAno.textContent = partes.numero;
                elUnidad.textContent = partes.unidad;
                elProgreso.style.width = (t * 100) + '%';

                if (!advertenciaMostrada && t > TIEMPOS.fraccionAntesAdvertencia && dest.advertencia) {
                    elFase.textContent = '⚠️ ' + dest.advertencia;
                    advertenciaMostrada = true;
                }

                if (t < 1) {
                    requestAnimationFrame(frame);
                } else {
                    resolve();
                }
            }
            requestAnimationFrame(frame);
        });
    }

    async function ejecutar() {
        document.body.classList.add('salto-temporal-activo');

        for (let i = 0; i < destinos.length; i++) {
            const dest = destinos[i];
            const origen = i === 0 ? presente : destinos[i - 1].valor;

            elFase.textContent = i === 0
                ? 'Despegue desde el presente…'
                : 'Salto encadenado ' + (i + 1) + '…';
            elLlegada.hidden = true;
            contenedor.className = 'salto-temporal';
            if (dest.claseCss) {
                contenedor.classList.add('salto-temporal--' + dest.claseCss);
            }
            contenedor.style.setProperty('--era-color', dest.color);

            await esperar(TIEMPOS.despegueInicial);
            await animarAno(origen, dest.valor, duracionSalto(origen, dest.valor), dest);

            elFase.textContent = '¡Llegada detectada!';
            elEmoji.textContent = dest.emoji;
            elNombre.textContent = dest.nombre;
            elEtiqueta.textContent = dest.etiqueta;
            mostrarTexto(elLema, dest.lema ? '«' + dest.lema + '»' : '');
            mostrarTexto(elAdvertencia, dest.advertencia ? '⚠️ ' + dest.advertencia : '');
            elLlegada.hidden = false;
            contenedor.classList.add('salto-temporal--flash');

            await esperar(TIEMPOS.pantallaLlegada);
            contenedor.classList.remove('salto-temporal--flash');

            if (i < destinos.length - 1) {
                await esperar(TIEMPOS.entreSaltos);
            }
        }

        elFase.textContent = 'Reintegrando en la línea temporal…';
        await esperar(TIEMPOS.reintegracion);

        contenedor.classList.add('salto-temporal--salir');
        await esperar(TIEMPOS.salidaOverlay);

        contenedor.remove();
        document.body.classList.remove('salto-temporal-activo');
        if (confirmacion) {
            confirmacion.classList.remove('confirmacion--oculta');
            confirmacion.classList.add('confirmacion--visible');
        }
    }

    ejecutar();
})();
