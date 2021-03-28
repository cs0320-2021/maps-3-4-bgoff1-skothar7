function styleStar(star, containerToCreateWithin) {
    const starFields = star.split("\t");

    const starName = document.createElement("div");
    starName.style.font = "verdana";
    starName.style.paddingLeft = "40px";
    starName.innerHTML = starFields[0];

    const starID = document.createElement("span");
    starID.innerHTML = "ID " + starFields[1];
    starID.style.fontSize = "11px";
    starID.style.padding = "5px";
    starID.style.color = "#696969";
    starID.style.background = "#A0A0A0";
    starID.style.borderRadius = "5px";
    starID.style.marginLeft = "10px";
    starName.appendChild(starID);

    containerToCreateWithin.appendChild(starName);

    const starCoord = document.createElement("div");
    starCoord.style.font = "verdana";
    starCoord.style.color = "#A0A0A0";
    starCoord.style.paddingRight = "40px";
    starCoord.innerHTML = starFields[2] + "   ";
    containerToCreateWithin.appendChild(starCoord);
}

function createBackgroundRectangles(outputs, backgroundForOutputs, backgroundForOutputsContainer) {
    for (let i = 0; i < outputs.length; i++) {
        if (i == 0) {
            styleStar(outputs[i], backgroundForOutputs)
            backgroundForOutputs.style.display = "flex";
        } else {
            const newBackground = backgroundForOutputs.cloneNode();
            newBackground.id += i.toString();
            styleStar(outputs[i], newBackground)
            backgroundForOutputsContainer.appendChild(newBackground);
        }
    }
}

let starResults = document.getElementById('starResults').value;

const rectangleBackground = document.getElementById('background-rectangle');
const rectangleBackgroundContainer = document.getElementsByClassName('result-container')[0];

let starResultsSeparated = starResults.split("<br/>");
starResultsSeparated = starResultsSeparated.slice(0,-1);

createBackgroundRectangles(starResultsSeparated, rectangleBackground, rectangleBackgroundContainer);
