var person_num = 0;
$(document).ready(
    function () {
        $("#add").click(
            function () {
                if (person_num < 4) {
                    addoneok();
                    person_num++;
                }
                else if (person_num == 4) {
                    addonenot();
                    $("#add").hide();
                }
            }
        );
    }
);

function addoneok() {
    var aperson = $("#haveoked").clone();
    aperson.css("display","inline-block");
    $("#teamlist").append(aperson);
}
function addonenot() { 
    var aperson = $("#notoked").clone();
    aperson.css("display","inline-block");
    $("#teamlist").append(aperson);
 }