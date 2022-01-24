jQuery($ => {
    /*$('body').scroll(function () {
     var scroll = $(this).scrollTop();
     $('#dvDatos').toggleClass('divStickySombra', scroll >= 100);
     $('#tituloDatos').toggleClass('hideElem', scroll >= 100);
     
     $('#dvPresupuesto').toggleClass('divStickySombra', scroll >= 100);
     $('#tituloPresupuesto').toggleClass('hideElem', scroll >= 100);
     });*/
});

function marcarEleccion(tipoNombramiento, idCandidato, tipoEstilo) {
    var cantidad = $("div[id*='" + tipoNombramiento + "']").length;
    for (var i = 0; i < cantidad; i++) {
        var nameImg = "img[id='" + tipoNombramiento + ":".concat(i).concat(":img_" + tipoNombramiento + "']");
        var nameBtn = "button[id='" + tipoNombramiento + ":".concat(i).concat(":btn_" + tipoNombramiento + "']");
        if (idCandidato != i) {
            jQuery(nameImg).addClass('imgBlackAndWhite');
            jQuery(nameBtn).removeClass('ui-button-' + tipoEstilo);
            jQuery("div[id='" + tipoNombramiento + ":" + i + ":dvNotify" + tipoNombramiento + "']").hide();
        } else {
            jQuery(nameImg).removeClass('imgBlackAndWhite');
            jQuery(nameBtn).addClass('ui-button-' + tipoEstilo);
            jQuery("div[id='" + tipoNombramiento + ":" + i + ":dvNotify" + tipoNombramiento + "']").show();
        }
    }

    //jQuery("button[id='propietarios:0:imgPropietario']").attr("disabled", true).addClass('imgBlackAndWhite');
}