function makeRequest(request, callback) {
    request.onreadystatechange = function () {
        if (request.readyState === XMLHttpRequest.DONE && request.status === 200) {
            callback(request.response);
        }
    }
}

function redirect(localUrl) {
    window.location.replace(localUrl)
}