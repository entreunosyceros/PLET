/**
 * Easter eggs del Salón VIP del Tiempo.
 */
(function () {
    'use strict';

    const respuesta = document.getElementById('salonVipRespuesta');
    const btnCola = document.getElementById('btnColaBigBang');
    const btnCafe = document.getElementById('btnCafeLegal');

    function mostrar(texto) {
        if (!respuesta) return;
        respuesta.textContent = texto;
        respuesta.hidden = false;
        respuesta.classList.add('salon-vip-respuesta--visible');
    }

    if (btnCola) {
        btnCola.addEventListener('click', function () {
            mostrar('Has saltado la cola del Big Bang. Lamentablemente ya ocurrió. Vuelve en −13.800 millones de años.');
        });
    }

    if (btnCafe) {
        btnCafe.addEventListener('click', function () {
            mostrar('Un dron trae café caliente. La Interpol Temporal abre expediente en 3… 2… 1… (Es broma. Por ahora.)');
        });
    }

    const btnUniverso = document.getElementById('btnComprarUniverso');
    if (btnUniverso) {
        btnUniverso.addEventListener('click', function () {
            mostrar('Has intentado comprar el universo, pero el servidor no tiene cambio de 500. Inténtalo en el próximo Big Bang.');
        });
    }
})();
