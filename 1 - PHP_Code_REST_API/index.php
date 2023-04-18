<?php
use Psr\Http\Message\ResponseInterface as Response;
use Psr\Http\Message\ServerRequestInterface as Request;
use Slim\Factory\AppFactory;

require __DIR__ . '/../vendor/autoload.php';
require __DIR__ . '/../config/dbFunctions.php';

$app = AppFactory::create();
$app->setBasePath("");
$app->addErrorMiddleware(true, true, true);

$app->add(new Tuupola\Middleware\HttpBasicAuthentication([
    "secure"=>false,
    "users" => [
        "" => "",
    ]
]));

// All ADMIN FUNCTION CALLS -----------------------------------------------------------------

$app->post('/createAdmin', function (Request $request, Response $response) {
    if (!haveEmptyParameters(array('username', 'password', 'email', 'name', 'phone', 'address'), $request, $response)) {
        $request_data = $request->getParsedBody();

        $username = $request_data['username'];
        $password = $request_data['password'];
        $email = $request_data['email'];
        $name = $request_data['name'];
        $phone = $request_data['phone'];
        $address = $request_data['address'];

        $hash_password = password_hash($password, PASSWORD_DEFAULT);
        $db = new DbFunctions;

        $result = $db->createAdmin($username, $hash_password, $email, $name, $phone, $address);
        if($result == USER_CREATED) {
            $message = array();
            $message['error'] = false;
            $message['message'] = 'Admin created successfully';

            $response->getBody()->write(json_encode($message));

            return $response
                ->withHeader('Content-type', 'application/json')
                ->withStatus(201);
        } elseif ($result == USER_FAILURE) {
            $message = array();
            $message['error'] = true;
            $message['message'] = 'An Error Has Occurred.';

            $response->getBody()->write(json_encode($message));

            return $response
                ->withHeader('Content-type', 'application/json')
                ->withStatus(411);

        } elseif ($result == USER_EXISTS) {
            $message = array();
            $message['error'] = true;
            $message['message'] = 'Admin Already Exists.';

            $response->getBody()->write(json_encode($message));

            return $response
                ->withHeader('Content-type', 'application/json')
                ->withStatus(410);
        }
    }
    return $response
        ->withHeader('Content-type', 'application/json')
        ->withStatus(412);
});

$app->post('/adminLogin', function(Request $request, Response $response) {
    if(!haveEmptyParameters(array('username', 'password'), $request, $response)) {
        $request_data = $request->getParsedBody();

        $username = $request_data['username'];
        $password = $request_data['password'];

        $db = new DbFunctions;
        $result = $db->adminLogin($username, $password);
        if ($result == USER_AUTH) {
            $admin = $db->getAdminByUsername($username);
            $response_data = array();
            $response_data['error'] = false;
            $response_data['message'] = 'Login successful';
            $response_data['admin'] = $admin;

            $response->getBody()->write(json_encode($response_data));
            return $response
                ->withHeader('Content-type', 'application/json')
                ->withStatus(200);
        } elseif ($result == USER_NOT_FOUND) {
            $response_data = array();
            $response_data['error'] = true;
            $response_data['message'] = 'Admin does not exist';

            $response->getBody()->write(json_encode($response_data));
            return $response
                ->withHeader('Content-type', 'application/json')
                ->withStatus(200);
        } elseif ($result == USER_NOT_AUTH) {
            $response_data = array();
            $response_data['error'] = true;
            $response_data['message'] = 'Invalid credentials';

            $response->getBody()->write(json_encode($response_data));
            return $response
                ->withHeader('Content-type', 'application/json')
                ->withStatus(200);
        }
    }
    return $response
        ->withHeader('Content-type', 'application/json')
        ->withStatus(200);
});

$app->get('/allAdmins', function(Request $request, Response $response){
    $db = new DbFunctions;
    $admins = $db->getAllAdmins();
    $response_data = array();
    $response_data['error'] = false;
    $response_data['admins'] = $admins;
    $response->getBody()->write(json_encode($response_data));
    return $response
        ->withHeader('Content-type', 'application/json')
        ->withStatus(201);
});

$app->post('/updateAdmin/{id}', function(Request $request, Response $response, array $args){
    $id = $args['id'];
    if (!haveEmptyParameters(array('username', 'email', 'name', 'phone', 'address'), $request, $response)) {
        $request_data = $request->getParsedBody();
        $username = $request_data['username'];
        $email = $request_data['email'];
        $name = $request_data['name'];
        $phone = $request_data['phone'];
        $address = $request_data['address'];

        $db = new DbFunctions;
        $result = $db->updateAdmin($email, $name, $phone, $address, $id);
        if($result == ADMIN_UPDATED) {
            $response_data = array();
            $response_data['error'] = false;
            $response_data['message'] = 'Admin updated successfully';
            $admin = $db->getAdminByUsername($username);
            $response_data['admin'] = $admin;

            $response->getBody()->write(json_encode($response_data));

            return $response
                ->withHeader('Content-type', 'application/json')
                ->withStatus(201);
        }
        elseif ($result == EMAIL_EXISTS) {
            $response_data = array();
            $response_data['error'] = true;
            $response_data['message'] = 'Email already exists, please try again';
            $response->getBody()->write(json_encode($response_data));

            return $response
                ->withHeader('Content-type', 'application/json')
                ->withStatus(411);
        }
        elseif ($result == ADMIN_FAILURE) {
            $response_data = array();
            $response_data['error'] = true;
            $response_data['message'] = 'Failed to update admin, please try again';
            $admin = $db->getAdminByUsername($username);
            $response_data['admin'] = $admin;

            $response->getBody()->write(json_encode($response_data));

            return $response
                ->withHeader('Content-type', 'application/json')
                ->withStatus(410);
        }
    }
    return $response
        ->withHeader('Content-type', 'application/json')
        ->withStatus(412);
});

$app->post('/updateAdminUsername/{id}', function(Request $request, Response $response, array $args){
    $id = $args['id'];
    if (!haveEmptyParameters(array('current_username', 'new_username'), $request, $response)) {
        $request_data = $request->getParsedBody();

        $current_username = $request_data['current_username'];
        $new_username = $request_data['new_username'];

        $db = new DbFunctions;
        $result = $db->updateAdminUsername($current_username, $new_username, $id);
        if ($result == USERNAME_UPDATED) {
            $message = array();
            $message['error'] = false;
            $message['message'] = 'Username updated successfully';

            $response->getBody()->write(json_encode($message));
            return $response
                ->withHeader('Content-type', 'application/json')
                ->withStatus(201);
        } elseif ($result == USERNAME_INVALID) {
            $message = array();
            $message['error'] = true;
            $message['message'] = 'Incorrect username, please try again';
            $response->getBody()->write(json_encode($message));
            return $response
                ->withHeader('Content-type', 'application/json')
                ->withStatus(401);
        } elseif ($result == USERNAME_NOT_UPDATED) {
            $message = array();
            $message['error'] = true;
            $message['message'] = 'Unable to update username';
            $response->getBody()->write(json_encode($message));
            return $response
                ->withHeader('Content-type', 'application/json')
                ->withStatus(411);
        } elseif ($result == USERNAME_EXISTS) {
            $message = array();
            $message['error'] = true;
            $message['message'] = 'Username already exists, please try again';
            $response->getBody()->write(json_encode($message));
            return $response
                ->withHeader('Content-type', 'application/json')
                ->withStatus(410);
        }
    }
    return $response
        ->withHeader('Content-type', 'application/json')
        ->withStatus(412);
});

$app->post('/updateAdminPassword', function(Request $request, Response $response){
    if (!haveEmptyParameters(array('current_password', 'new_password', 'username'), $request, $response)) {
        $request_data = $request->getParsedBody();

        $current_password = $request_data['current_password'];
        $new_password = $request_data['new_password'];
        $username = $request_data['username'];

        $db = new DbFunctions;
        $result = $db->updateAdminPassword($current_password, $new_password, $username);
        if ($result == PASSWORD_UPDATED) {
            $response_data = array();
            $response_data['error'] = false;
            $response_data['message'] = 'Password updated successfully';
            $response->getBody()->write(json_encode($response_data));
            return $response
                ->withHeader('Content-type', 'application/json')
                ->withStatus(201);
        } elseif ($result == PASSWORD_INVALID) {
            $response_data = array();
            $response_data['error'] = true;
            $response_data['message'] = 'Incorrect password, please try again';
            $response->getBody()->write(json_encode($response_data));
            return $response
                ->withHeader('Content-type', 'application/json')
                ->withStatus(401);
        } elseif ($result == PASSWORD_NOT_UPDATED) {
            $response_data = array();
            $response_data['error'] = true;
            $response_data['message'] = 'Unable to update password';
            $response->getBody()->write(json_encode($response_data));
            return $response
                ->withHeader('Content-type', 'application/json')
                ->withStatus(410);
        }
    }
    return $response
        ->withHeader('Content-type', 'application/json')
        ->withStatus(412);
});

$app->delete('/deleteAdmin/{id}', function(Request $request, Response $response, array $args) {
    $id = $args['id'];

    $db = new DbFunctions;
    $response_data = array();
    if ($db->deleteAdmin($id)) {
        $response_data['error'] = false;
        $response_data['message'] = 'Admin has been deleted';
    } else {
        $response_data['error'] = true;
        $response_data['message'] = 'Unable to delete admin';
    }
    $response->getBody()->write(json_encode($response_data));
    return $response
        ->withHeader('Content-type', 'application/json')
        ->withStatus(201);
});// ALL NURSE FUNCTION CALLS -----------------------------------------------------------------

$app->post('/createNurse', function (Request $request, Response $response) {
    if (!haveEmptyParameters(array('username', 'password', 'email', 'name', 'phone', 'address'), $request, $response)) {
        $request_data = $request->getParsedBody();

        $username = $request_data['username'];
        $password = $request_data['password'];
        $email = $request_data['email'];
        $name = $request_data['name'];
        $phone = $request_data['phone'];
        $address = $request_data['address'];

        $hash_password = password_hash($password, PASSWORD_DEFAULT);
        $db = new DbFunctions;

        $result = $db->createNurse($username, $hash_password, $email, $name, $phone, $address);
        if($result == USER_CREATED) {
            $message = array();
            $message['error'] = false;
            $message['message'] = 'Nurse created successfully';

            $response->getBody()->write(json_encode($message));

            return $response
                ->withHeader('Content-type', 'application/json')
                ->withStatus(201);
        } elseif ($result == USER_FAILURE) {
            $message = array();
            $message['error'] = true;
            $message['message'] = 'An Error Has Occurred.';

            $response->getBody()->write(json_encode($message));

            return $response
                ->withHeader('Content-type', 'application/json')
                ->withStatus(411);

        } elseif ($result == USER_EXISTS) {
            $message = array();
            $message['error'] = true;
            $message['message'] = 'Nurse Already Exists.';

            $response->getBody()->write(json_encode($message));

            return $response
                ->withHeader('Content-type', 'application/json')
                ->withStatus(410);
        }
    }
    return $response
        ->withHeader('Content-type', 'application/json')
        ->withStatus(412);
});

$app->post('/nurseLogin', function(Request $request, Response $response) {
    if(!haveEmptyParameters(array('username', 'password'), $request, $response)) {
        $request_data = $request->getParsedBody();

        $username = $request_data['username'];
        $password = $request_data['password'];

        $db = new DbFunctions;

        $result = $db->nurseLogin($username, $password);
        if ($result == USER_AUTH) {
            $nurse = $db->getNurseByUsername($username);
            $response_data = array();
            $response_data['error'] = false;
            $response_data['message'] = 'Login successful';
            $response_data['nurse'] = $nurse;

            $response->getBody()->write(json_encode($response_data));
            return $response
                ->withHeader('Content-type', 'application/json')
                ->withStatus(200);
        } elseif ($result == USER_NOT_FOUND) {
            $response_data = array();
            $response_data['error'] = true;
            $response_data['message'] = 'Nurse does not exist';

            $response->getBody()->write(json_encode($response_data));
            return $response
                ->withHeader('Content-type', 'application/json')
                ->withStatus(200);
        } elseif ($result == USER_NOT_AUTH) {
            $response_data = array();

            $response_data['error'] = true;
            $response_data['message'] = 'Invalid credentials';

            $response->getBody()->write(json_encode($response_data));

            return $response
                ->withHeader('Content-type', 'application/json')
                ->withStatus(200);
        }
    }
    return $response
        ->withHeader('Content-type', 'application/json')
        ->withStatus(200);
});

$app->get('/allNurses', function(Request $request, Response $response){
    $db = new DbFunctions;
    $nurses = $db->getAllNurses();
    $response_data = array();
    $response_data['error'] = false;
    $response_data['nurses'] = $nurses;
    $response->getBody()->write(json_encode($response_data));
    return $response
        ->withHeader('Content-type', 'application/json')
        ->withStatus(201);
});

$app->get('/nurseById/{id}', function(Request $request, Response $response, array $args){
    $id = $args['id'];
    $db = new DbFunctions;
    $nurse = $db->getNurseById($id);
    $response_data = array();
    $response_data['error'] = false;
    $response_data['nurse'] = $nurse;
    $response->getBody()->write(json_encode($response_data));
    return $response
        ->withHeader('Content-type', 'application/json')
        ->withStatus(201);
});

$app->post('/updateNurse/{id}', function(Request $request, Response $response, array $args){
    $id = $args['id'];
    if (!haveEmptyParameters(array('username', 'email', 'name', 'phone', 'address'), $request, $response)) {
        $request_data = $request->getParsedBody();
        $username = $request_data['username'];
        $email = $request_data['email'];
        $name = $request_data['name'];
        $phone = $request_data['phone'];
        $address = $request_data['address'];

        $db = new DbFunctions;
        $result = $db->updateNurse($email, $name, $phone, $address, $id);
        if ($result == NURSE_UPDATED) {
            $response_data = array();
            $response_data['error'] = false;
            $response_data['message'] = 'Nurse updated successfully';
            $nurse = $db->getNurseByUsername($username);
            $response_data['nurse'] = $nurse;

            $response->getBody()->write(json_encode($response_data));

            return $response
                ->withHeader('Content-type', 'application/json')
                ->withStatus(201);
        }elseif ($result == EMAIL_EXISTS) {
            $response_data = array();
            $response_data['error'] = true;
            $response_data['message'] = 'Email already exists, please try again';
            $response->getBody()->write(json_encode($response_data));

            return $response
                ->withHeader('Content-type', 'application/json')
                ->withStatus(411);
        }
        elseif ($result == NURSE_FAILURE) {
            $response_data = array();
            $response_data['error'] = true;
            $response_data['message'] = 'Failed to update nurse, please try again';
            $nurse = $db->getNurseByUsername($username);
            $response_data['nurse'] = $nurse;

            $response->getBody()->write(json_encode($response_data));

            return $response
                ->withHeader('Content-type', 'application/json')
                ->withStatus(410);
        }
    }
    return $response
        ->withHeader('Content-type', 'application/json')
        ->withStatus(412);
});

$app->post('/updateNursePassword', function(Request $request, Response $response){
    if (!haveEmptyParameters(array('current_password', 'new_password', 'username'), $request, $response)) {
        $request_data = $request->getParsedBody();

        $current_password = $request_data['current_password'];
        $new_password = $request_data['new_password'];
        $username = $request_data['username'];

        $db = new DbFunctions;
        $result = $db->updateNursePassword($current_password, $new_password, $username);
        if ($result == PASSWORD_UPDATED) {
            $response_data = array();
            $response_data['error'] = false;
            $response_data['message'] = 'Password updated successfully';
            $response->getBody()->write(json_encode($response_data));
            return $response
                ->withHeader('Content-type', 'application/json')
                ->withStatus(201);
        } elseif ($result == PASSWORD_INVALID) {
            $response_data = array();
            $response_data['error'] = true;
            $response_data['message'] = 'Incorrect password, please try again';
            $response->getBody()->write(json_encode($response_data));
            return $response
                ->withHeader('Content-type', 'application/json')
                ->withStatus(401);
        } elseif ($result == PASSWORD_NOT_UPDATED) {
            $response_data = array();
            $response_data['error'] = true;
            $response_data['message'] = 'Unable to update password';
            $response->getBody()->write(json_encode($response_data));
            return $response
                ->withHeader('Content-type', 'application/json')
                ->withStatus(411);
        }
    }
    return $response
        ->withHeader('Content-type', 'application/json')
        ->withStatus(412);
});

$app->post('/updateNurseUsername/{id}', function(Request $request, Response $response, array $args){
    $id = $args['id'];
    if (!haveEmptyParameters(array('current_username', 'new_username'), $request, $response)) {
        $request_data = $request->getParsedBody();

        $current_username = $request_data['current_username'];
        $new_username = $request_data['new_username'];

        $db = new DbFunctions;
        $result = $db->updateNurseUsername($current_username, $new_username, $id);
        if ($result == USERNAME_UPDATED) {
            $message = array();
            $message['error'] = false;
            $message['message'] = 'Username updated successfully';

            $response->getBody()->write(json_encode($message));
            return $response
                ->withHeader('Content-type', 'application/json')
                ->withStatus(201);
        } elseif ($result == USERNAME_INVALID) {
            $message = array();
            $message['error'] = true;
            $message['message'] = 'Incorrect username, please try again';
            $response->getBody()->write(json_encode($message));
            return $response
                ->withHeader('Content-type', 'application/json')
                ->withStatus(401);
        } elseif ($result == USERNAME_NOT_UPDATED) {
            $message = array();
            $message['error'] = true;
            $message['message'] = 'Unable to update username';
            $response->getBody()->write(json_encode($message));
            return $response
                ->withHeader('Content-type', 'application/json')
                ->withStatus(411);
        } elseif ($result == USERNAME_EXISTS) {
            $message = array();
            $message['error'] = true;
            $message['message'] = 'Username already exists, please try again';
            $response->getBody()->write(json_encode($message));
            return $response
                ->withHeader('Content-type', 'application/json')
                ->withStatus(410);
        }
    }
    return $response
        ->withHeader('Content-type', 'application/json')
        ->withStatus(412);
});
$app->delete('/deleteNurse/{id}', function(Request $request, Response $response, array $args) {
    $id = $args['id'];

    $db = new DbFunctions;
    $response_data = array();
    if ($db->deleteNurse($id)) {
        $response_data['error'] = false;
        $response_data['message'] = 'Nurse has been deleted';
    } else {
        $response_data['error'] = true;
        $response_data['message'] = 'Unable to delete nurse';
    }
    $response->getBody()->write(json_encode($response_data));
    return $response
        ->withHeader('Content-type', 'application/json')
        ->withStatus(201);
});

// NURSE SHIFT FUNCTION CALLS -----------------------------------------------------------------

$app->post('/createShift', function (Request $request, Response $response) {
    if (!haveEmptyParameters(array('shift_date', 'start_time', 'end_time', 'unit_id', 'nurse_id'), $request, $response)) {
        $request_data = $request->getParsedBody();

        $shift_date = $request_data['shift_date'];
        $start_time = $request_data['start_time'];
        $end_time = $request_data['end_time'];
        $unit_id = $request_data['unit_id'];
        $nurse_id = $request_data['nurse_id'];

        $db = new DbFunctions;

        $result = $db->createShift($shift_date, $start_time, $end_time, $unit_id, $nurse_id);
        if($result == SHIFT_CREATED) {
            $message = array();
            $message['error'] = false;
            $message['message'] = 'Shift created successfully';

            $response->getBody()->write(json_encode($message));

            return $response
                ->withHeader('Content-type', 'application/json')
                ->withStatus(201);
        } elseif ($result == SHIFT_FAILURE) {
            $message = array();
            $message['error'] = true;
            $message['message'] = 'An Error Has occurred.';

            $response->getBody()->write(json_encode($message));

            return $response
                ->withHeader('Content-type', 'application/json')
                ->withStatus(411);

        }
    }
    return $response
        ->withHeader('Content-type', 'application/json')
        ->withStatus(412);
});

$app->get('/shiftsByNurse/{nurse_id}', function(Request $request, Response $response, array $args) {
    $nurse_id = $args['nurse_id'];
    $db = new DbFunctions;
    $shifts = $db->getNurseShifts($nurse_id);
    $response_data = array();
    $response_data['error'] = false;
    $response_data['shifts'] = $shifts;
    $response->getBody()->write(json_encode($response_data));
    return $response
        ->withHeader('Content-type', 'application/json')
        ->withStatus(201);
});

$app->get('/upcomingShiftsByNurse/{nurse_id}', function(Request $request, Response $response, array $args) {
    $nurse_id = $args['nurse_id'];
    $db = new DbFunctions;
    $shifts = $db->getUpcomingNurseShifts($nurse_id);
    $response_data = array();
    $response_data['error'] = false;
    $response_data['shifts'] = $shifts;
    $response->getBody()->write(json_encode($response_data));
    return $response
        ->withHeader('Content-type', 'application/json')
        ->withStatus(201);
});


$app->get('/shiftsByUnit/{unit_id}', function(Request $request, Response $response, array $args) {
    $unit_id = $args['unit_id'];
    $db = new DbFunctions;
    $shifts = $db->getUnitShifts($unit_id);
    $response_data = array();
    $response_data['error'] = false;
    $response_data['shifts'] = $shifts;
    $response->getBody()->write(json_encode($response_data));
    return $response
        ->withHeader('Content-type', 'application/json')
        ->withStatus(201);
});

$app->delete('/deleteShift/{id}', function(Request $request, Response $response, array $args) {
    $id = $args['id'];

    $db = new DbFunctions;
    $response_data = array();
    if ($db->deleteShift($id)) {
        $response_data['error'] = false;
        $response_data['message'] = 'Shift has been deleted';
    } else {
        $response_data['error'] = true;
        $response_data['message'] = 'Unable to delete shift';
    }
    $response->getBody()->write(json_encode($response_data));
    return $response
        ->withHeader('Content-type', 'application/json')
        ->withStatus(201);
});

// NURSE REQUESTS FUNCTION CALLS -----------------------------------------------------------------

$app->post('/createNurseRequest', function (Request $request, Response $response) {
    if (!haveEmptyParameters(array('start_date', 'end_date', 'status', 'type', 'notes', 'nurse_id'), $request, $response)) {
        $request_data = $request->getParsedBody();

        $start_date = $request_data['start_date'];
        $end_date = $request_data['end_date'];
        $status = $request_data['status'];
        $type = $request_data['type'];
        $notes = $request_data['notes'];
        $nurse_id = $request_data['nurse_id'];

        $db = new DbFunctions;

        $result = $db->createNurseRequest($start_date, $end_date, $status, $type, $notes, $nurse_id);
        if($result == REQUEST_CREATED) {
            $message = array();
            $message['error'] = false;
            $message['message'] = 'Request created successfully';

            $response->getBody()->write(json_encode($message));

            return $response
                ->withHeader('Content-type', 'application/json')
                ->withStatus(201);
        } elseif ($result == REQUEST_FAILURE) {
            $message = array();
            $message['error'] = true;
            $message['message'] = 'An error has occurred.';

            $response->getBody()->write(json_encode($message));

            return $response
                ->withHeader('Content-type', 'application/json')
                ->withStatus(411);

        }
    }
    return $response
        ->withHeader('Content-type', 'application/json')
        ->withStatus(412);
});

$app->post('/updateNurseRequest/{id}', function(Request $request, Response $response, array $args){
    $id = $args['id'];
    if (!haveEmptyParameters(array('start_date', 'end_date', 'status', 'type', 'notes'), $request, $response)) {
        $request_data = $request->getParsedBody();
        $start_date = $request_data['start_date'];
        $end_date = $request_data['end_date'];
        $status = $request_data['status'];
        $type = $request_data['type'];
        $notes = $request_data['notes'];

        $db = new DbFunctions;
        if ($db->updateNurseRequest($start_date, $end_date, $status, $type, $notes, $id)) {
            $response_data = array();
            $response_data['error'] = false;
            $response_data['message'] = 'Request updated successfully';
            $response->getBody()->write(json_encode($response_data));

            return $response
                ->withHeader('Content-type', 'application/json')
                ->withStatus(201);
        } else {
            $response_data = array();
            $response_data['error'] = true;
            $response_data['message'] = 'Failed to update request, please try again';
            $response->getBody()->write(json_encode($response_data));

            return $response
                ->withHeader('Content-type', 'application/json')
                ->withStatus(410);
        }
    }
    return $response
        ->withHeader('Content-type', 'application/json')
        ->withStatus(412);
});

$app->get('/allNurseRequests', function(Request $request, Response $response) {
    $db = new DbFunctions;
    $nurse_requests = $db->getAllNurseRequests();
    $response_data = array();
    $response_data['error'] = false;
    $response_data['nurse_requests'] = $nurse_requests;
    $response->getBody()->write(json_encode($response_data));
    return $response
        ->withHeader('Content-type', 'application/json')
        ->withStatus(201);
});

$app->get('/requestsByNurse/{nurse_id}', function(Request $request, Response $response, array $args) {
    $nurse_id = $args['nurse_id'];
    $db = new DbFunctions;
    $nurse_requests = $db->getRequestsByNurse($nurse_id);
    $response_data = array();
    $response_data['error'] = false;
    $response_data['nurse_requests'] = $nurse_requests;
    $response->getBody()->write(json_encode($response_data));
    return $response
        ->withHeader('Content-type', 'application/json')
        ->withStatus(201);
});

$app->get('/pendingRequestsByNurse/{nurse_id}', function(Request $request, Response $response, array $args) {
    $nurse_id = $args['nurse_id'];
    $db = new DbFunctions;
    $nurse_requests = $db->getPendingRequestsByNurse($nurse_id);
    $response_data = array();
    $response_data['error'] = false;
    $response_data['nurse_requests'] = $nurse_requests;
    $response->getBody()->write(json_encode($response_data));
    return $response
        ->withHeader('Content-type', 'application/json')
        ->withStatus(201);
});


$app->get('/requestsByStatus/{status}', function(Request $request, Response $response, array $args) {
    $status = $args['status'];
    $db = new DbFunctions;
    $nurse_requests = $db->getRequestsByStatus($status);
    $response_data = array();
    $response_data['error'] = false;
    $response_data['nurse_requests'] = $nurse_requests;
    $response->getBody()->write(json_encode($response_data));
    return $response
        ->withHeader('Content-type', 'application/json')
        ->withStatus(201);
});

$app->delete('/deleteRequest/{id}', function(Request $request, Response $response, array $args) {
    $id = $args['id'];

    $db = new DbFunctions;
    $response_data = array();
    if ($db->deleteRequest($id)) {
        $response_data['error'] = false;
        $response_data['message'] = 'Request has been deleted';
    } else {
        $response_data['error'] = true;
        $response_data['message'] = 'Unable to delete request';
    }
    $response->getBody()->write(json_encode($response_data));
    return $response
        ->withHeader('Content-type', 'application/json')
        ->withStatus(201);
});

// UNIT FUNCTION CALLS -----------------------------------------------------------------
$app->get('/allUnits', function(Request $request, Response $response) {
    $db = new DbFunctions;
    $units = $db->getAllUnits();
    $response_data = array();
    $response_data['error'] = false;
    $response_data['units'] = $units;
    $response->getBody()->write(json_encode($response_data));
    return $response
        ->withHeader('Content-type', 'application/json')
        ->withStatus(201);
});

$app->get('/unitById/{id}', function(Request $request, Response $response, array $args) {
    $id = $args['id'];
    $db = new DbFunctions;
    $unit = $db->getUnitById($id);
    $response_data = array();
    $response_data['error'] = false;
    $response_data['unit'] = $unit;
    $response->getBody()->write(json_encode($response_data));
    return $response
        ->withHeader('Content-type', 'application/json')
        ->withStatus(201);
});


function haveEmptyParameters($required_params, $request, $response) {
    $error = false;
    $error_params = '';
    $request_params = $request->getParsedBody();

    foreach ($required_params as $param) {
        if (!isset($request_params[$param]) || strlen($request_params[$param]) <= 0) {
            $error = true;
            $error_params .= $param . ', ';
        }
    }

    if($error) {
        $error_detail = array();
        $error_detail['error'] = true;
        $error_detail['message'] = 'Required Parameters ' .
            substr($error_params, 0, -2) . ' Are Either Missing Or Empty';

        $response->getBody()->write(json_encode($error_detail));
    }

    return $error;
}


$app->run();
