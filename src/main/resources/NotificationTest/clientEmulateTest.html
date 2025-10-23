const socket = new SockJS('/ws');
const stompClient = Stomp.over(socket);

stompClient.connect({}, () => {
  stompClient.subscribe('/topic/ambulance/45', (message) => {
    const data = JSON.parse(message.body);
    console.log("🚨 New Emergency Request:", data);
  });

  stompClient.subscribe('/topic/request/123', (update) => {
    const data = JSON.parse(update.body);
    if (data.event === "ASSIGNED") {
      alert("Request taken by ambulance " + data.assignedAmbulanceId);
    }
  });
});

// When driver clicks Accept
function acceptRequest(requestId) {
  stompClient.send("/app/ambulance/response", {}, JSON.stringify({
    requestId: requestId,
    ambulanceId: 45,
    response: "ACCEPT"
  }));
}
