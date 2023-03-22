$(document).ready(
    function () {
        $("#tab1").click(
            function () { 
                $(this).css("background-color","rgb(240, 240, 240)");
                $(this).css("border-bottom","1px solid rgb(165, 165, 165)");
                $("#tab2").css("background-color","rgb(209, 209, 209)");
                $("#tab2").css("border-bottom","1px solid rgb(105, 105, 105)");
                $("#tab3").css("background-color","rgb(209, 209, 209)");
                $("#tab3").css("border-bottom","1px solid rgb(105, 105, 105)");
                $("#content").text("这是内容1");
            }
        );
        $("#tab2").click(
            function () { 
                $(this).css("background-color","rgb(240, 240, 240)");
                $(this).css("border-bottom","1px solid rgb(165, 165, 165)");
                $("#tab1").css("background-color","rgb(209, 209, 209)");
                $("#tab1").css("border-bottom","1px solid rgb(105, 105, 105)");
                $("#tab3").css("background-color","rgb(209, 209, 209)");
                $("#tab3").css("border-bottom","1px solid rgb(105, 105, 105)");
                $("#content").text("这是内容2");
            }
        );
        $("#tab3").click(
            function () { 
                $(this).css("background-color","rgb(240, 240, 240)");
                $(this).css("border-bottom","1px solid rgb(165, 165, 165)");
                $("#tab2").css("background-color","rgb(209, 209, 209)");
                $("#tab2").css("border-bottom","1px solid rgb(105, 105, 105)");
                $("#tab1").css("background-color","rgb(209, 209, 209)");
                $("#tab1").css("border-bottom","1px solid rgb(105, 105, 105)");
                $("#content").text("这是内容3");
            }
        );
        $(".btn").click(
             function () {
                var alertCard=document.createElement("div");
                alertCard.id="bg_card";
                alertCard.style.width=document.body.scrollWidth+"px";
                alertCard.style.height=window.screen.availHeight+"px";
                alertCard.style.backgroundColor="#00000088";
                alertCard.style.position="absolute";
                alertCard.style.top="0px";
                alertCard.style.left="0px";
                alertCard.style.zIndex="2";
                $(this).after(alertCard);
                var isCard=document.createElement("div");
                isCard.id="fr_card";
                isCard.style.width="450px";
                isCard.style.height="270px";
                isCard.style.backgroundColor="#eeeeeeff";
                isCard.style.position="fixed";
                isCard.style.top="180px";
                isCard.style.Right="auto";
                isCard.style.Left="auto";
                isCard.style.padding="25px";
                isCard.style.zIndex="3";
                isCard.style.border="1px solid black";
                isCard.style.borderRadius="50px 20px 50px 20px";
                $(alertCard).after(isCard);
                var part1=document.createElement("div");
                part1.id="part_1";
                var part2=document.createElement("div");
                part2.id="part_2";
                var part3=document.createElement("div");
                part3.id="part_3";
                var p1=document.createElement("p");
                var p2=document.createElement("p");
                var p3=document.createElement("p");
                var btn1=document.createElement("div");
                btn1.id="btn_1";
                var btn2=document.createElement("div");
                btn2.id="btn_2";
                var btn3=document.createElement("div");
                btn3.id="btn_3";
                part1.style.height="40px";
                part3.style.height="36px";
                part3.style.paddingTop="4px";
                part2.style.height="188px";
                part2.style.borderTop="1px solid grey";
                part2.style.borderBottom="1px solid grey";
                p1.style.cssFloat="left";
                p1.style.display="inline-block";
                p1.style.margin="0";
                p1.style.padding="auto";
                p1.style.height="40px";
                p1.style.textAlign="center";
                p1.textContent="这是标题";
                p2.textContent="楼下是内容二";
                p3.textContent="楼上是内容一";
                btn1.style.height="40px";
                btn1.style.width="40px";
                btn1.style.textAlign="center";
                btn1.textContent="╳";
                btn1.style.fontSize="32px";
                btn1.style.cssFloat="right";
                btn1.style.display="inline-block";
                btn1.style.marginTop="-11px";
                btn1.style.cursor="pointer";
                btn2.textContent="取消";
                btn2.style.marginRight="5px";
                btn2.style.cssFloat="right";
                btn2.style.cursor="pointer";
                btn3.textContent="确认";
                btn3.style.cssFloat="right";
                btn3.style.cursor="pointer";
                document.getElementById("fr_card").appendChild(part1);
                document.getElementById("part_1").appendChild(p1);
                document.getElementById("part_1").appendChild(btn1);
                document.getElementById("fr_card").appendChild(part2);
                document.getElementById("part_2").appendChild(p2);
                document.getElementById("part_2").appendChild(p3);
                document.getElementById("fr_card").appendChild(part3);
                document.getElementById("part_3").appendChild(btn3);
                document.getElementById("part_3").appendChild(btn2);
                function closeall(){
                    $("#bg_card").remove();
                    $("#fr_card").remove();
                }
                $("#bg_card").click(
                    function () {
                        closeall();
                    }
                );
                $("#btn_1").click(
                    function () {
                        closeall();
                    }
                );
                $("#btn_2").click(
                    function () {
                        closeall();
                    }
                );
                $("#btn_3").click(
                    function () {
                        closeall();
                    }
                );
            } 
        );
    }
);