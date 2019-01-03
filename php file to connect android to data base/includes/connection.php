<?php

class DBconnect{

	private $conn;

	function __construct()
	{

	}

	function connect()
	{
		include_once dirname(__FILE__).'/constants.php';
		$this->conn = new mysqli(server_name,user_name,password ,db_name); //connecting the database

		if(mysqli_connect_errno()){
			echo "failed to connect to database".mysqli_connect_errno();
		}

		return $this->conn;


	}
}

