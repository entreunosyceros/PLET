/**
 * Ventana flotante de paradoja temporal.
 *
 * - Se desplaza rebotando por la pantalla.
 * - Clic izquierdo en «×»: cierra y reaparece hasta MAX_REAPARICIONES veces.
 * - Clic derecho en «×»: cierre definitivo (contextmenu).
 */
(function () {
    'use strict';

    const MAX_REAPARICIONES = 5;
    const VELOCIDAD = 1.4;
    const ANCHO = 320;
    const ALTO = 200;

    const MENSAJES = [
        '🌀 Alerta de paradoja — cierre no reconocido por la línea temporal.',
        '⚠️ Este aviso ha sido enviado desde una realidad alternativa.',
        '🦋 El Efecto Mariposa recomienda no cerrar ventanas sin permiso.',
        '⏳ Error 404: el botón cerrar no existe en esta línea temporal.',
        '📋 IFCD0112: las paradojas no se desinstalan con un clic.',
        '👋 Última oportunidad… o no. Prueba el otro botón del ratón.'
    ];

    let reaparicionesRestantes = MAX_REAPARICIONES;
    let ventana = null;
    let animId = null;
    let posX = 0;
    let posY = 0;
    let velX = VELOCIDAD;
    let velY = VELOCIDAD * 0.85;
    let indiceMensaje = 0;

    const meta = document.querySelector('meta[name="ventana-paradoja-reapariciones"]');
    if (meta) {
        const parsed = parseInt(meta.content, 10);
        if (!Number.isNaN(parsed) && parsed >= 0) {
            reaparicionesRestantes = parsed;
        }
    }

    function crearVentana() {
        ventana = document.createElement('div');
        ventana.className = 'ventana-flotante';
        ventana.setAttribute('role', 'dialog');
        ventana.setAttribute('aria-labelledby', 'ventanaFlotanteTitulo');
        ventana.innerHTML = `
            <div class="ventana-flotante__barra">
                <span class="ventana-flotante__titulo" id="ventanaFlotanteTitulo">Paradoja.exe</span>
                <button type="button" class="ventana-flotante__cerrar" id="ventanaFlotanteCerrar"
                        title="Clic izquierdo: reaparece · Clic derecho: cerrar de verdad"
                        aria-label="Cerrar ventana de paradoja">×</button>
            </div>
            <div class="ventana-flotante__cuerpo">
                <p class="ventana-flotante__texto" id="ventanaFlotanteTexto"></p>
                <p class="ventana-flotante__pista">Pista: el clic <em>derecho</em> en × estabiliza la línea temporal.</p>
                <p class="ventana-flotante__contador" id="ventanaFlotanteContador"></p>
            </div>
        `;
        document.body.appendChild(ventana);

        const btn = document.getElementById('ventanaFlotanteCerrar');
        btn.addEventListener('click', onCerrarIzquierdo);
        btn.addEventListener('contextmenu', onCerrarDerecho);

        actualizarContenido();
        posicionAleatoria();
        iniciarMovimiento();
    }

    function actualizarContenido() {
        const texto = document.getElementById('ventanaFlotanteTexto');
        const contador = document.getElementById('ventanaFlotanteContador');
        if (texto) {
            texto.textContent = MENSAJES[indiceMensaje % MENSAJES.length];
        }
        if (contador) {
            if (reaparicionesRestantes > 0) {
                contador.textContent = 'Reapariciones restantes con clic izquierdo: ' + reaparicionesRestantes;
            } else {
                contador.textContent = 'Último cierre con clic izquierdo — la próxima vez se irá de verdad.';
            }
        }
    }

    function posicionAleatoria() {
        const maxX = Math.max(0, window.innerWidth - ANCHO - 16);
        const maxY = Math.max(0, window.innerHeight - ALTO - 16);
        posX = Math.random() * maxX;
        posY = Math.random() * maxY;
        velX = (Math.random() > 0.5 ? 1 : -1) * VELOCIDAD;
        velY = (Math.random() > 0.5 ? 1 : -1) * VELOCIDAD * 0.85;
        aplicarPosicion();
    }

    function aplicarPosicion() {
        if (!ventana) return;
        ventana.style.transform = 'translate(' + posX + 'px, ' + posY + 'px)';
    }

    function iniciarMovimiento() {
        if (animId) cancelAnimationFrame(animId);

        function tick() {
            if (!ventana) return;

            const maxX = window.innerWidth - ANCHO - 8;
            const maxY = window.innerHeight - ALTO - 8;

            posX += velX;
            posY += velY;

            if (posX <= 0) {
                posX = 0;
                velX = Math.abs(velX);
            } else if (posX >= maxX) {
                posX = maxX;
                velX = -Math.abs(velX);
            }

            if (posY <= 0) {
                posY = 0;
                velY = Math.abs(velY);
            } else if (posY >= maxY) {
                posY = maxY;
                velY = -Math.abs(velY);
            }

            aplicarPosicion();
            animId = requestAnimationFrame(tick);
        }

        animId = requestAnimationFrame(tick);
    }

    function onCerrarIzquierdo(event) {
        event.preventDefault();
        if (reaparicionesRestantes > 0) {
            reaparicionesRestantes -= 1;
            indiceMensaje += 1;
            reaparecer();
        } else {
            cerrarDefinitivo();
        }
    }

    function onCerrarDerecho(event) {
        event.preventDefault();
        cerrarDefinitivo();
    }

    function reaparecer() {
        if (!ventana) return;
        ventana.classList.add('ventana-flotante--salida');
        setTimeout(function () {
            if (!ventana) return;
            ventana.classList.remove('ventana-flotante--salida');
            ventana.classList.add('ventana-flotante--entrada');
            posicionAleatoria();
            actualizarContenido();
            setTimeout(function () {
                if (ventana) ventana.classList.remove('ventana-flotante--entrada');
            }, 350);
        }, 280);
    }

    function cerrarDefinitivo() {
        if (!ventana) return;
        if (animId) {
            cancelAnimationFrame(animId);
            animId = null;
        }
        ventana.classList.add('ventana-flotante--salida');
        const ref = ventana;
        ventana = null;
        setTimeout(function () {
            ref.remove();
        }, 320);
    }

    window.addEventListener('resize', function () {
        if (!ventana) return;
        const maxX = Math.max(0, window.innerWidth - ANCHO - 8);
        const maxY = Math.max(0, window.innerHeight - ALTO - 8);
        posX = Math.min(posX, maxX);
        posY = Math.min(posY, maxY);
        aplicarPosicion();
    });

    if (document.readyState === 'loading') {
        document.addEventListener('DOMContentLoaded', crearVentana);
    } else {
        crearVentana();
    }
})();
