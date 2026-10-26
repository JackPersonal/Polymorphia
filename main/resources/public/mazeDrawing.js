const roomRadius = "80";

// window.addEventListener('resize', function() {
//     // Update SVG size based on new window dimensions
//     const svgElement = document.getElementById("maze");
//     svgElement.setAttribute("width", window.innerWidth.toString());
//     svgElement.setAttribute("height", window.innerHeight.toString());
//
// });

function drawGame(game) {
    const svgElement = document.getElementById("maze");
    svgElement.innerHTML = "";

    const svgWidth = svgElement.width.baseVal.value;
    const roomLocations = calculateRoomLocations(game, svgWidth, roomRadius);
    for (let room of game.rooms) {
        room.x = roomLocations[room.name].x;
        room.y = roomLocations[room.name].y;
        drawRoom(room, svgElement);
    }
    drawRoomConnections(game.rooms, roomLocations, roomRadius, svgElement, "blue");
}

function drawRoomShape(room, roomRadius, svgElement) {
    const rect = document.createElementNS("http://www.w3.org/2000/svg", "circle");
    rect.setAttribute("cx", room.x);
    rect.setAttribute("cy", room.y);
    rect.setAttribute("r", roomRadius);
    rect.setAttribute("fill", "yellow");
    rect.setAttribute("stroke", "green");
    rect.setAttribute("stroke-width", "4");
    svgElement.appendChild(rect);
}

function drawRoom(room, svgElement) {
    const roomNameX = room.x - roomRadius / 2 - 20;
    const roomNameY = room.y - roomRadius / 2 - 20;
    drawRoomShape(room, roomRadius, svgElement);
    drawRoomName(room.name, roomNameX + 20, roomNameY, svgElement);
    drawContentsOfRoom(room.contents, roomNameX + 20, roomNameY + 30, svgElement);
}

function drawRoomName(text, x, y, svgElement) {
    drawText(text, x, y, "bold", "black", svgElement);
}

function drawText(text, x, y, weight, color, svgElement) {
    const textElement = document.createElementNS("http://www.w3.org/2000/svg", "text");
    textElement.setAttribute("x", x);
    textElement.setAttribute("y", y);
    textElement.setAttribute("fill", color);
    textElement.setAttribute("font-weight", weight);
    textElement.textContent = text;
    svgElement.appendChild(textElement);
}

function drawContentsOfRoom(texts, x, y, svgElement) {
    for (let i = 0; i < texts.length; i++) {
        drawText(texts[i], x, y + i * 20, "normal", "red", svgElement);
    }
}

function drawLine(x1, y1, x2, y2, svgElement, color = "black") {
    // const path = document.createElementNS("http://www.w3.org/2000/svg", "path");
    // path.setAttribute("d", `M${x1},${y1} L${x2},${y2}`);
    // path.setAttribute("stroke", color);
    // path.setAttribute("stroke-width", "4");
    // svgElement.appendChild(path);

    const lineElement = document.createElementNS("http://www.w3.org/2000/svg", "line");
    lineElement.setAttribute("x1", x1);
    lineElement.setAttribute("y1", y1);
    lineElement.setAttribute("x2", x2);
    lineElement.setAttribute("y2", y2);
    lineElement.setAttribute("stroke", color);
    lineElement.setAttribute("stroke-width", "4");
    svgElement.appendChild(lineElement);
}

function calculateRoomLocations(game, canvasWidth, roomRadius) {
    const roomLocations = {};
    const center = {x: canvasWidth / 2, y: canvasWidth / 2};

    const layoutRadius = canvasWidth / 2 - roomRadius;
    const radialInterval = 2 * Math.PI / game.rooms.length;
    let currentAngle = 0.0;
    game.rooms.forEach((room) => {
        const x = Math.round(center.x + layoutRadius * Math.cos(currentAngle));
        const y = Math.round(center.y - layoutRadius * Math.sin(currentAngle));
        roomLocations[room.name] = {x, y};
        currentAngle += radialInterval;
    });

    return roomLocations;
}

function drawRoomConnections(rooms, roomLocations, roomRadius, svgElement, color = "black") {
    // addArrowHeadDefinitionToSvg(svgElement, color);
    for (let room of rooms) {
        for (let neighbor of room.neighbors) {
            let neighborLocation = roomLocations[neighbor];
            drawConnectionBetweenRooms(room, neighborLocation, svgElement, color);
        }
    }
}

function drawConnectionBetweenRooms(room, neighbor, svgElement, color = "black") {
    const lineAngle = Math.atan2(room.y - neighbor.y, room.x - neighbor.x);

    // Adjust the starting and ending points of the line to end at the room boundary and not the center
    const deltaX = roomRadius * Math.cos(lineAngle);
    const deltaY = roomRadius * Math.sin(lineAngle);
    const roomBoundaryLocation = {x: room.x - deltaX, y: room.y - deltaY};
    const neighborBoundaryLocation = {x: neighbor.x + deltaX, y: neighbor.y + deltaY};
    drawLine(roomBoundaryLocation.x, roomBoundaryLocation.y, neighborBoundaryLocation.x, neighborBoundaryLocation.y, svgElement, color);
}

function addArrowHeadDefinitionToSvg(svgElement, color) {
    const marker = document.createElementNS("http://www.w3.org/2000/svg", "marker");
    marker.setAttribute("markerWidth", "3");
    marker.setAttribute("markerHeight", "4");
    marker.setAttribute("refX", "0.1");
    marker.setAttribute("refY", "2");
    marker.setAttribute("orient", "auto");
    const path = document.createElementNS("http://www.w3.org/2000/svg", "path");
    path.setAttribute("d", "M0,0 V4 L2,2 Z");
    path.setAttribute("fill", color);
    marker.appendChild(path);

    const defs = document.createElementNS("http://www.w3.org/2000/svg", "defs");
    defs.appendChild(marker);
    svgElement.appendChild(defs);
}