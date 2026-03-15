public class AuthController {

    POST /auth/register
    POST /auth/login
    GET /users
    GET /users/{id}
    GET /users/search
    GET /users/me
    PATCH /users/profile
}

public class ChatController {

    POST /messages
    GET /conversations
    GET /conversations/{userId}
    PUT /messages/{id}/read
    DELETE /messages/{id}
}
