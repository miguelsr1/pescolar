jQuery($ => {
    $('body').scroll(function () {
        var scroll = $(this).scrollTop();
        $('#dvDatos').toggleClass('divStickySombra', scroll >= 100);
        $('#tituloDatos').toggleClass('hideElem', scroll >= 100);

        $('#dvPresupuesto').toggleClass('divStickySombra', scroll >= 100);
        $('#tituloPresupuesto').toggleClass('hideElem', scroll >= 100);
    });
});