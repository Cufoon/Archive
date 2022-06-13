$(document).ready(
    function () {
        var ssss=$(".foot .rightpart table tr td");
        ssss.css("cursor","pointer");
        ssss.click(
            function(){
                window.location.href = "coming.html";
            }
        );
        ssss.eq(0).click(
            function(){
                window.location.href = "team.html";
            }
        );
    }
);