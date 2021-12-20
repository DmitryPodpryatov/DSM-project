function login() {
    function callback(response) {
        localStorage.setItem("playerId", response)

        redirect("/games")
    }

    const body = location.host;
    const request = new XMLHttpRequest();
    makeRequest(request, callback);

    request.open("POST", "http://0.0.0.0:8082/api/login", true);
    request.send(body);
}