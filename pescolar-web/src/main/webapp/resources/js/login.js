$(document).ready(function () {
    $("#signIn").on('click', function () {
        $("#container").removeClass("right-panel-active");
    });
    $("#signUp").on('click', function () {
        $("#container").addClass("right-panel-active");
    });
});