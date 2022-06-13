<?php
    header("Access-Control-Allow-Origin:*");
    $json_string = file_get_contents("php://input");
    $json_datas = file_get_contents('test.json');
    $data_before = json_decode($json_datas, true);
    $data_before["comments"][] = json_decode($json_string, true);
    $a =count($data_before["comments"]);
    if($a>10){
        $array_comment=Array();
        $array_comment[]=$data_before["comments"][0];
        $b=$a-9;
        for($i=$b;$i<$a;$i++){
            $array_comment[]=$data_before["comments"][$i];
        }
        $data_now=Array("comments" => $array_comment);
        $json_write = json_encode($data_now);
        if(file_put_contents("test.json",$json_write) == false){
            echo false;
        }
        else{
            echo true;
        }
    }
    else{
        $json_write = json_encode($data_before);
        if(file_put_contents("test.json",$json_write) == false){
            echo false;
        }
        else{
            echo true;
        }
    }
?>