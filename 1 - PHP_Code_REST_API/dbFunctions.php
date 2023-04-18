<?php

class DbFunctions
{
    private $conn;
    function __construct()
    {
        require_once dirname(__FILE__) . '/dbConnect.php';
        $db = new DbConnect;
        $this->conn = $db->connect();
    }

//    ALL ADMIN FUNCTIONS -----------------------------------------------------------------

    public function createAdmin($username, $password, $email, $name, $phone, $address)
    {
        if (!$this->isNewAdminEmailExist($email) && (!$this->isNewAdminUsernameExist($username))) {
            $stmt = $this->conn->prepare("INSERT INTO admins (username, password, email, name, phone, address) VALUES  (?, ?, ?, ?, ?, ?)");
            $stmt->bind_param("ssssss", $username, $password, $email, $name, $phone, $address);
            if ($stmt->execute()) {
                return USER_CREATED;
            } else {
                return USER_FAILURE;
            }
        }
        return USER_EXISTS;
    }

    public function adminLogin($username, $password)
    {
        if ($this->isNewAdminUsernameExist($username)) {
            $hashed_password = $this->authAdminByUsername($username);
            if (password_verify($password, $hashed_password)) {
                return USER_AUTH;
            } else {
                return USER_NOT_AUTH;
            }
        } else {
            return USER_NOT_FOUND;
        }
    }

    public function getAllAdmins()
    {
        $stmt = $this->conn->prepare("SELECT id, username, email, name, phone, address FROM admins");
        $stmt->execute();
        $stmt->bind_result($id, $username, $email, $name, $phone, $address);
        $admins = array();
        while ($stmt->fetch()) {
            $admin = array();
            $admin['id'] = $id;
            $admin['username'] = $username;
            $admin['email'] = $email;
            $admin['name'] = $name;
            $admin['phone'] = $phone;
            $admin['address'] = $address;
            $admins[] = $admin;
        }
        return $admins;
    }

    public function updateAdmin($email, $name, $phone, $address, $id)
    {
        if (!$this->isAdminEmailExist($email, $id)) {
            $stmt = $this->conn->prepare("UPDATE admins SET email = ?, name = ?, phone = ?, address = ? WHERE id = ?");
            $stmt->bind_param("ssssi", $email, $name, $phone, $address, $id);
            if ($stmt->execute()) {
                return ADMIN_UPDATED;
            }
            return ADMIN_FAILURE;
        }

        return EMAIL_EXISTS;
    }

    public function updateAdminPassword($currentPassword, $newPassword, $username)
    {
        $hashed_password = $this->authAdminByUsername($username);
        if (password_verify($currentPassword, $hashed_password)) {
            $hash_password = password_hash($newPassword, PASSWORD_DEFAULT);
            $stmt = $this->conn->prepare("UPDATE admins SET password = ? WHERE username = ?");
            $stmt->bind_param("ss", $hash_password, $username);
            if ($stmt->execute()) {
                return PASSWORD_UPDATED;
            }
            return PASSWORD_NOT_UPDATED;
        } else {
            return PASSWORD_INVALID;
        }
    }

    public function updateAdminUsername($currentUsername, $newUsername, $id)
    {
        $username = $this->authAdminUsername($id);
        if ($currentUsername == $username) {
            if (!$this->isAdminUsernameExist($newUsername, $id)) {
                $stmt = $this->conn->prepare("UPDATE admins SET username =? where id = ?");
                $stmt->bind_param("si", $newUsername, $id);
                if ($stmt->execute()) {
                    return USERNAME_UPDATED;
                }
                return USERNAME_NOT_UPDATED;
            } else {
                return USERNAME_EXISTS;
            }
        } else {
            return USERNAME_INVALID;
        }
    }

    public function deleteAdmin($id)
    {
        $stmt = $this->conn->prepare("DELETE FROM admins WHERE id = ?");
        $stmt->bind_param("i", $id);
        if ($stmt->execute()) {
            return true;
        } else {
            return false;
        }
    }

    public function getAdminById($id)
    {
        $stmt = $this->conn->prepare("SELECT id, username, email, name, phone, address FROM admins WHERE id = ?");
        $stmt->bind_param("i", $id);
        $stmt->execute();
        $stmt->bind_result($id, $username, $email, $name, $phone, $address);
        $stmt->fetch();
        $admin = array();
        $admin['id'] = $id;
        $admin['username'] = $username;
        $admin['email'] = $email;
        $admin['name'] = $name;
        $admin['phone'] = $phone;
        $admin['address'] = $address;
        return $admin;
    }

    public function getAdminByUsername($username)
    {
        $stmt = $this->conn->prepare("SELECT id, username, email, name, phone, address FROM admins WHERE username = ?");
        $stmt->bind_param("s", $username);
        $stmt->execute();
        $stmt->bind_result($id, $username, $email, $name, $phone, $address);
        $stmt->fetch();
        $admin = array();
        $admin['id'] = $id;
        $admin['username'] = $username;
        $admin['email'] = $email;
        $admin['name'] = $name;
        $admin['phone'] = $phone;
        $admin['address'] = $address;
        return $admin;
    }

    public function authAdminUsername($id)
    {
        $stmt = $this->conn->prepare("SELECT username FROM admins WHERE id = ?");
        $stmt->bind_param("i", $id);
        $stmt->execute();
        $stmt->bind_result($username);
        $stmt->fetch();
        return $username;
    }

    private function authAdminByUsername($username)
    {
        $stmt = $this->conn->prepare("SELECT password FROM admins WHERE username = ?");
        $stmt->bind_param("s", $username);
        $stmt->execute();
        $stmt->bind_result($password);
        $stmt->fetch();
        return $password;
    }

//    NURSE FUNCTIONS-------------------------------------------------------

    public function createNurse($username, $password, $email, $name, $phone, $address)
    {
        if (!$this->isNewNurseEmailExist($email) && (!$this->isNewNurseUsernameExist($username))) {
            $stmt = $this->conn->prepare("INSERT INTO nurses (username, password, email, name, phone, address) VALUES (?, ?, ?, ?, ?, ?)");
            $stmt->bind_param("ssssss", $username, $password, $email, $name, $phone, $address);
            if ($stmt->execute()) {
                return USER_CREATED;
            } else {
                return USER_FAILURE;
            }
        }
        return USER_EXISTS;
    }

    public function nurseLogin($username, $password)
    {
        if ($this->isNewNurseUsernameExist($username)) {
            $hashed_password = $this->authNurseByUsername($username);
            if (password_verify($password, $hashed_password)) {
                return USER_AUTH;
            } else {
                return USER_NOT_AUTH;
            }
        } else {
            return USER_NOT_FOUND;
        }
    }

    public function getAllNurses()
    {
        $stmt = $this->conn->prepare("SELECT id, username, email, name, phone, address FROM nurses");
        $stmt->execute();
        $stmt->bind_result($id, $username, $email, $name, $phone, $address);
        $nurses = array();
        while ($stmt->fetch()) {
            $nurse = array();
            $nurse['id'] = $id;
            $nurse['username'] = $username;
            $nurse['email'] = $email;
            $nurse['name'] = $name;
            $nurse['phone'] = $phone;
            $nurse['address'] = $address;
            $nurses[] = $nurse;
        }
        return $nurses;
    }

    public function updateNurse($email, $name, $phone, $address, $id)
    {
        if (!$this->isNurseEmailExist($email, $id)) {
            $stmt = $this->conn->prepare("UPDATE nurses SET email = ?, name = ?, phone = ?, address = ? WHERE id = ?");
            $stmt->bind_param("ssssi", $email, $name, $phone, $address, $id);
            if ($stmt->execute()) {
                return NURSE_UPDATED;
            }
            return NURSE_FAILURE;
        } else {
            return EMAIL_EXISTS;
        }
    }

    public function updateNursePassword($currentPassword, $newPassword, $username)
    {
        $hashed_password = $this->authNurseByUsername($username);
        if (password_verify($currentPassword, $hashed_password)) {
            $hash_password = password_hash($newPassword, PASSWORD_DEFAULT);
            $stmt = $this->conn->prepare("UPDATE nurses SET password = ? where username = ?");
            $stmt->bind_param("ss", $hash_password, $username);
            if ($stmt->execute()) {
                return PASSWORD_UPDATED;
            }
            return PASSWORD_NOT_UPDATED;
        } else {
            return PASSWORD_INVALID;
        }
    }

    public function updateNurseUsername($currentUsername, $newUsername, $id)
    {
        $username = $this->authNurseUsername($id);
        if ($currentUsername == $username) {
            if (!$this->isNurseUsernameExist($newUsername, $id)) {
                $stmt = $this->conn->prepare("UPDATE nurses SET username =? where id = ?");
                $stmt->bind_param("si", $newUsername, $id);
                if ($stmt->execute()) {
                    return USERNAME_UPDATED;
                }
                return USERNAME_NOT_UPDATED;
            } else {
                return USERNAME_EXISTS;
            }
        } else {
            return USERNAME_INVALID;
        }
    }

    public function deleteNurse($id)
    {
        $stmt = $this->conn->prepare("DELETE FROM nurses WHERE id =?");
        $stmt->bind_param("i", $id);
        if ($stmt->execute()) {
            return true;
        } else {
            return false;
        }
    }

    public function getNurseById($id)
    {
        $stmt = $this->conn->prepare("SELECT id, username, email, name, phone, address FROM nurses WHERE id = ?");
        $stmt->bind_param("i", $id);
        $stmt->execute();
        $stmt->bind_result($id, $username, $email, $name, $phone, $address);
        $stmt->fetch();
        $nurse = array();
        $nurse['id'] = $id;
        $nurse['username'] = $username;
        $nurse['email'] = $email;
        $nurse['name'] = $name;
        $nurse['phone'] = $phone;
        $nurse['address'] = $address;
        return $nurse;
    }

    public function getNurseByUsername($username)
    {
        $stmt = $this->conn->prepare("SELECT id, username, email, name, phone, address FROM nurses WHERE username = ?");
        $stmt->bind_param("s", $username);
        $stmt->execute();
        $stmt->bind_result($id, $username, $email, $name, $phone, $address);
        $stmt->fetch();
        $nurse = array();
        $nurse['id'] = $id;
        $nurse['username'] = $username;
        $nurse['email'] = $email;
        $nurse['name'] = $name;
        $nurse['phone'] = $phone;
        $nurse['address'] = $address;
        return $nurse;
    }

    public function authNurseUsername($id)
    {
        $stmt = $this->conn->prepare("SELECT username FROM nurses WHERE id = ?");
        $stmt->bind_param("i", $id);
        $stmt->execute();
        $stmt->bind_result($username);
        $stmt->fetch();
        return $username;
    }

    public function authNurseByUsername($username)
    {
        $stmt = $this->conn->prepare("SELECT password FROM nurses WHERE username = ?");
        $stmt->bind_param("s", $username);
        $stmt->execute();
        $stmt->bind_result($password);
        $stmt->fetch();
        return $password;
    }

//    ALL SHIFT FUNCTIONS -----------------------------------------------------------------

    public function createShift($shift_date, $start_time, $end_time, $unit_id, $nurse_id)
    {
        $stmt = $this->conn->prepare("INSERT INTO shifts (shift_date, start_time, end_time, unit_id, nurse_id) VALUES  (?, ?, ?, ?, ?)");
        $stmt->bind_param("sssii", $shift_date, $start_time, $end_time, $unit_id, $nurse_id);
        if ($stmt->execute()) {
            return SHIFT_CREATED;
        } else {
            return SHIFT_FAILURE;
        }
    }

    public function getNurseShifts($nurse_id)
    {
        $stmt = $this->conn->prepare("SELECT id, shift_date, nurse_id, unit_id, start_time, end_time FROM shifts WHERE nurse_id = ? AND shift_date > NOW() ORDER BY shift_date");
        $stmt->bind_param("i", $nurse_id);
        $stmt->execute();
        $stmt->bind_result($id, $shift_date, $nurse_id, $unit_id, $start_time, $end_time);
        $shifts = array();
        while ($stmt->fetch()) {
            $shift = array();
            $shift['id'] = $id;
            $shift['shift_date'] = $shift_date;
            $shift['nurse_id'] = $nurse_id;
            $shift['unit_id'] = $unit_id;
            $shift['start_time'] = $start_time;
            $shift['end_time'] = $end_time;
            $shifts[] = $shift;
        }
        return $shifts;
    }

    public function getUpcomingNurseShifts($nurse_id)
    {
        $stmt = $this->conn->prepare("SELECT id, shift_date, nurse_id, unit_id, start_time, end_time FROM shifts WHERE nurse_id = ? AND shift_date BETWEEN NOW() AND CURRENT_DATE + interval 7 day ORDER BY shift_date");
        $stmt->bind_param("i", $nurse_id);
        $stmt->execute();
        $stmt->bind_result($id, $shift_date, $nurse_id, $unit_id, $start_time, $end_time);
        $shifts = array();
        while ($stmt->fetch()) {
            $shift = array();
            $shift['id'] = $id;
            $shift['shift_date'] = $shift_date;
            $shift['nurse_id'] = $nurse_id;
            $shift['unit_id'] = $unit_id;
            $shift['start_time'] = $start_time;
            $shift['end_time'] = $end_time;
            $shifts[] = $shift;
        }
        return $shifts;
    }


    public function getUnitShifts($unit_id)
    {
        $stmt = $this->conn->prepare("SELECT id, shift_date, nurse_id, unit_id, start_time, end_time FROM shifts WHERE unit_id = ? AND shift_date > NOW() ORDER BY shift_date");
        $stmt->bind_param("i", $unit_id);
        $stmt->execute();
        $stmt->bind_result($id, $shift_date, $nurse_id, $unit_id, $start_time, $end_time);
        $shifts = array();
        while ($stmt->fetch()) {
            $shift = array();
            $shift['id'] = $id;
            $shift['shift_date'] = $shift_date;
            $shift['nurse_id'] = $nurse_id;
            $shift['unit_id'] = $unit_id;
            $shift['start_time'] = $start_time;
            $shift['end_time'] = $end_time;
            $shifts[] = $shift;
        }
        return $shifts;
    }

    public function deleteShift($id)
    {
        $stmt = $this->conn->prepare("DELETE FROM shifts WHERE id =?");
        $stmt->bind_param("i", $id);
        if ($stmt->execute()) {
            return true;
        } else {
            return false;
        }
    }

//    ALL REQUEST FUNCTIONS -----------------------------------------------------------------

    public function createNurseRequest($start_date, $end_date, $status, $type, $notes, $nurse_id)
    {
        $stmt = $this->conn->prepare("INSERT INTO requests (start_date, end_date, status, type, notes, nurse_id) VALUES  (?, ?, ?, ?, ?, ?)");
        $stmt->bind_param("sssssi", $start_date, $end_date, $status, $type, $notes, $nurse_id);
        if ($stmt->execute()) {
            return REQUEST_CREATED;
        } else {
            return REQUEST_FAILURE;
        }
    }

    public function updateNurseRequest($start_date, $end_date, $status, $type, $notes, $id)
    {
        $stmt = $this->conn->prepare("UPDATE requests SET start_date = ?, end_date = ?, status = ?, type = ?, notes = ? WHERE id = ?");
        $stmt->bind_param("sssssi", $start_date, $end_date, $status, $type, $notes, $id);
        if ($stmt->execute()) {
            return true;
        }
        return false;
    }

    public function getAllNurseRequests()
    {
        $stmt = $this->conn->prepare("SELECT id, nurse_id, start_date, end_date, type, status, notes FROM requests WHERE start_date > NOW() ORDER BY start_date");
        $stmt->execute();
        $stmt->bind_result($id, $nurse_id, $start_date, $end_date, $type, $status, $notes);
        $nurse_requests = array();
        while ($stmt->fetch()) {
            $nurse_request = array();
            $nurse_request['id'] = $id;
            $nurse_request['nurse_id'] = $nurse_id;
            $nurse_request['start_date'] = $start_date;
            $nurse_request['end_date'] = $end_date;
            $nurse_request['type'] = $type;
            $nurse_request['status'] = $status;
            $nurse_request['notes'] = $notes;
            $nurse_requests[] = $nurse_request;
        }
        return $nurse_requests;
    }

    public function getRequestsByNurse($nurse_id)
    {
        $stmt = $this->conn->prepare("SELECT id, nurse_id, start_date, end_date, type, status, notes FROM requests WHERE nurse_id = ? ORDER BY start_date");
        $stmt->bind_param("i", $nurse_id);
        $stmt->execute();
        $stmt->bind_result($id, $nurse_id, $start_date, $end_date, $type, $status, $notes);
        $nurse_requests = array();
        while ($stmt->fetch()) {
            $nurse_request = array();
            $nurse_request['id'] = $id;
            $nurse_request['nurse_id'] = $nurse_id;
            $nurse_request['start_date'] = $start_date;
            $nurse_request['end_date'] = $end_date;
            $nurse_request['type'] = $type;
            $nurse_request['status'] = $status;
            $nurse_request['notes'] = $notes;
            $nurse_requests[] = $nurse_request;
        }
        return $nurse_requests;
    }

    public function getPendingRequestsByNurse($nurse_id)
    {
        $stmt = $this->conn->prepare("SELECT id, nurse_id, start_date, end_date, type, status, notes FROM requests WHERE nurse_id = ? AND status = 'Pending' ORDER BY start_date");
        $stmt->bind_param("i", $nurse_id);
        $stmt->execute();
        $stmt->bind_result($id, $nurse_id, $start_date, $end_date, $type, $status, $notes);
        $nurse_requests = array();
        while ($stmt->fetch()) {
            $nurse_request = array();
            $nurse_request['id'] = $id;
            $nurse_request['nurse_id'] = $nurse_id;
            $nurse_request['start_date'] = $start_date;
            $nurse_request['end_date'] = $end_date;
            $nurse_request['type'] = $type;
            $nurse_request['status'] = $status;
            $nurse_request['notes'] = $notes;
            $nurse_requests[] = $nurse_request;
        }
        return $nurse_requests;
    }


    public function getRequestsByStatus($status)
    {
        $stmt = $this->conn->prepare("SELECT id, nurse_id, start_date, end_date, type, status, notes FROM requests WHERE status = ? AND start_date > NOW() ORDER BY start_date");
        $stmt->bind_param("s", $status);
        $stmt->execute();
        $stmt->bind_result($id, $nurse_id, $start_date, $end_date, $type, $status, $notes);
        $nurse_requests = array();
        while ($stmt->fetch()) {
            $nurse_request = array();
            $nurse_request['id'] = $id;
            $nurse_request['nurse_id'] = $nurse_id;
            $nurse_request['start_date'] = $start_date;
            $nurse_request['end_date'] = $end_date;
            $nurse_request['type'] = $type;
            $nurse_request['status'] = $status;
            $nurse_request['notes'] = $notes;
            $nurse_requests[] = $nurse_request;
        }
        return $nurse_requests;
    }

    public function deleteRequest($id)
    {
        $stmt = $this->conn->prepare("DELETE FROM requests WHERE id =?");
        $stmt->bind_param("i", $id);
        if ($stmt->execute()) {
            return true;
        } else {
            return false;
        }
    }

//ALL UNIT FUNCTIONS -----------------------------------------------------------------

    public function getAllUnits()
    {
        $stmt = $this->conn->prepare("SELECT id, unit_name, location FROM units");
        $stmt->execute();
        $stmt->bind_result($id, $unit_name, $location);
        $units = array();
        while ($stmt->fetch()) {
            $unit = array();
            $unit['id'] = $id;
            $unit['unit_name'] = $unit_name;
            $unit['location'] = $location;
            $units[] = $unit;
        }
        return $units;
    }

    public function getUnitById($id)
    {
        $stmt = $this->conn->prepare("SELECT id, unit_name, location FROM units WHERE id = ?");
        $stmt->bind_param("i", $id);
        $stmt->execute();
        $stmt->bind_result($id, $unit_name, $location);
        $stmt->fetch();
        $unit = array();
        $unit['id'] = $id;
        $unit['unit_name'] = $unit_name;
        $unit['location'] = $location;
        return $unit;
    }

//    AUTHORIZATION FUNCTIONS---------------------------------
    public function isNurseUsernameExist($username, $id)
    {
        $stmt = $this->conn->prepare("SELECT id FROM nurses WHERE username = ? AND id != ?");
        $stmt->bind_param("si", $email, $id);
        $stmt->execute();
        $stmt->store_result();
        return $stmt->num_rows > 0;
    }

    public function isNewNurseUsernameExist($username)
    {
        $stmt = $this->conn->prepare("SELECT id FROM nurses WHERE username = ?");
        $stmt->bind_param("s", $username);
        $stmt->execute();
        $stmt->store_result();
        return $stmt->num_rows > 0;
    }

    public function isAdminUsernameExist($username, $id)
    {
        $stmt = $this->conn->prepare("SELECT id FROM admins WHERE username = ? AND id != ?");
        $stmt->bind_param("si", $email, $id);
        $stmt->execute();
        $stmt->store_result();
        return $stmt->num_rows > 0;
    }

    public function isNewAdminUsernameExist($username)
    {
        $stmt = $this->conn->prepare("SELECT id FROM admins WHERE username = ?");
        $stmt->bind_param("s", $username);
        $stmt->execute();
        $stmt->store_result();
        return $stmt->num_rows > 0;
    }

    private function isNewNurseEmailExist($email)
    {
        $stmt = $this->conn->prepare("SELECT id FROM nurses WHERE email = ?");
        $stmt->bind_param("s", $email);
        $stmt->execute();
        $stmt->store_result();
        return $stmt->num_rows > 0;
    }

    private function isNurseEmailExist($email, $id)
    {
        $stmt = $this->conn->prepare("SELECT id FROM nurses WHERE email = ? AND id != ?");
        $stmt->bind_param("si", $email, $id);
        $stmt->execute();
        $stmt->store_result();
        return $stmt->num_rows > 0;
    }

    private function isNewAdminEmailExist($email)
    {
        $stmt = $this->conn->prepare("SELECT id FROM admins WHERE email = ?");
        $stmt->bind_param("s", $email);
        $stmt->execute();
        $stmt->store_result();
        return $stmt->num_rows > 0;
    }

    private function isAdminEmailExist($email, $id)
    {
        $stmt = $this->conn->prepare("SELECT id FROM admins WHERE email = ? AND id != ?");
        $stmt->bind_param("si", $email, $id);
        $stmt->execute();
        $stmt->store_result();
        return $stmt->num_rows > 0;
    }
}