const naiveNeighbors = document.getElementById('naive-neighbors');
const neighbors = document.getElementById('neighbors');
const naiveRadius = document.getElementById('naive-radius');
const radius = document.getElementById('radius');

const numTitle = document.getElementById('numTitle');

let hasAlreadySlid = false;
function slideRight(url) {
    if (window.location.href.indexOf(url) != -1) {
            document.getElementsByClassName('command-input')[0].style.display = "inline-block";
            document.getElementsByClassName('commands-container')[0].style.width = "50%";
            hasAlreadySlid = true;
        }
}

if (!hasAlreadySlid) {
    slideRight("http://localhost:4567/input");
}

function disableButtons(buttons, url) {
    if (!(window.location.href.indexOf(url) != -1)) {
        for (let i = 0; i < buttons.length; i++) {
            buttons[i].setAttribute("disabled", "disabled");
        }
    }
}

disableButtons([naiveNeighbors, neighbors, naiveRadius, radius],
    "http://localhost:4567/stars");

const x = document.getElementById('x');
const y = document.getElementById('y');
const z = document.getElementById('z');

//REGEX taken from
//https://stackoverflow.com/questions/2811031/decimal-or-numeric-values-in-regular-expression-validation
//const validDouble = new RegExp(/^(?!-0?(\.0+)?$)-?(0|[1-9]\d*)?(\.\d+)?(?<=\d)$/);
function isDouble(elem) {
    let input = elem.value
    console.log(input);
    console.log(parseFloat(input));
    if (isNaN(parseFloat(input)) || !isFinite(input)) {
        elem.value = "";
        return false;
    } else {
        return true;
    }
}

function checkNumericFormFields() {
    const xInputCorrect = isDouble(x);
    const yInputCorrect = isDouble(y);
    const zInputCorrect = isDouble(z);
    if (!(xInputCorrect && yInputCorrect && zInputCorrect) && (starName.value.length == 0)) {
        alert("Please provide an INTEGER or a DECIMAL");
        return false;
    }
    return true;
}

function checkFieldsFilled(fields) {
    let filled = true;
    for (let i = 0; i < fields.length; i++) {
        const field = fields[i];
        if (field && field.value.length == 0) {
            filled = false;
        }
    }
    return filled;
}

const num = document.getElementById('num');
const starName = document.getElementById('star-name');
const submit = document.getElementById('submit-input')

let possibleFields1 = [num, starName];
let possibleFields2 = [num, x, y, z];

function enableSubmit() {
    if (checkFieldsFilled(possibleFields1) || checkFieldsFilled(possibleFields2)) {
        submit.style.display = "inline-block";
        submit.removeAttribute("disabled");
    } else {
        submit.setAttribute("disabled", "disabled");
    }
}

document.getElementsByClassName('command-input')[0].addEventListener("input", enableSubmit);

function hideBasedOnOtherElem(elemToHide, elemInputChecked) {
    if (elemInputChecked.value.length != 0) {
        elemToHide.setAttribute("disabled", "disabled");
    } else {
        elemToHide.removeAttribute("disabled");
    }
}

function hideBasedOnInput(textInput, elemToHide) {
    textInput.addEventListener("input", function() {
        hideBasedOnOtherElem(elemToHide, this);
    })
}

hideBasedOnInput(starName, x);
hideBasedOnInput(starName, y);
hideBasedOnInput(starName, z);
hideBasedOnInput(x, starName);
hideBasedOnInput(y, starName);
hideBasedOnInput(z, starName);