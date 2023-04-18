<?php
class DbConnect
{
    public function connect()
    {
        include_once dirname(__FILE__) . '/dbConfig.php';
        $conn = new mysqli(DB_HOST, DB_USER, DB_PASS, DB_NAME);

        if (mysqli_connect_errno()) {
            echo "Failed to connect " . mysqli_connect_error();
            return null;
        }
        return $conn;
    }
}
