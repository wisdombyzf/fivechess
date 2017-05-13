<?php
$link=mysql_connect("","root","z753951","web")or die ("无法打开数据库1");
mysql_select_db("web")or die("无法打开数据库2");
$result = mysql_query("SELECT * FROM fivechess");
while($row = mysql_fetch_array($result))
{
	echo $row['zuobiao'];
}
$id=$_GET["id"];
$aa=$_GET["zuobiao"];
$result = mysql_query("SELECT * FROM fivechess");
while($row = mysql_fetch_array($result))
{
	if($id!=$row["id"])
	{
		mysql_query("UPDATE `web`.`fivechess` SET `id` = '".$id."', `zuobiao` = '".$aa."' WHERE `fivechess`.`id` = ".$row['id']."");
		//echo"修改完成";
	}
}

?>