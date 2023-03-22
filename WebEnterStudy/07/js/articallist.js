$(document).ready(
    function () {
        toload();
        $("#loadmore").click(
            function () {
                toload();
            });
    }
);

function toload() {
    for (var i = 0; i < 20; i++) {
        var one = $("<div></div>");
        one.css({ "width": "24%", "height": "12em", "background": "#FFFFFF", "display": "inline-block", "margin": "0", "margin-left": "0.8%","cursor": "pointer" });
        one.click(
            function(){
                window.location.href = "ArticalDetial.html";
            }
        );
        $("#list").append(one);
    }
}