import './App.css';
import React, {useEffect, useRef} from "react";

let curNorthWestLat;
let curNorthWestLon;

let curSouthEastLat;
let curSouthEastLon;

let pixToCoord;

// global reference to the oldCanvas element
let canvas;

// global reference to the oldCanvas' context
let ctx;

//http://www.petecorey.com/blog/2019/08/19/animating-a-canvas-with-react-hooks/
const getPixelRatio = context => {
    let backingStore =
        context.backingStorePixelRatio ||
        context.webkitBackingStorePixelRatio ||
        context.mozBackingStorePixelRatio ||
        context.msBackingStorePixelRatio ||
        context.oBackingStorePixelRatio ||
        context.backingStorePixelRatio ||
        1;
    return (window.devicePixelRatio || 1) / backingStore;
};

function Canvas(props) {
    let w = 600;
    let h = 600;
    let ref = useRef();
    useEffect(() => {
        canvas = ref.current;
        ctx = canvas.getContext("2d");
        let ratio = getPixelRatio(ctx);
        let width = getComputedStyle(canvas).getPropertyValue('width').slice(0, -2);
        let height = getComputedStyle(canvas).getPropertyValue('height').slice(0, -2);
        canvas.width = width * ratio;
        canvas.height = height * ratio;
        canvas.style.width = `${width}px`;
        canvas.style.height = `${height}px`;
        ctx.clearRect(0, 0, canvas.width, canvas.height);
        ctx.lineWidth = 1
        pixToCoord = 8000;

        curSouthEastLat = 41.823876;
        curSouthEastLon = -71.395963;

        curNorthWestLat = (curSouthEastLat - w/pixToCoord);
        curNorthWestLon = (curSouthEastLon - h/pixToCoord);


        let rawWaysData = props.routetorender
        let listOfWays = [];
        if (rawWaysData != null) {
            listOfWays = (rawWaysData + '').split(";");
        }

        let parsedWay;
        let type;
        let startLat;
        let startLon;
        let endLat;
        let endLon;
        console.log("low" + listOfWays.length);
        for (let i=0; i<listOfWays.length; i++) {
            parsedWay =  listOfWays[i].split(",");
            type = parsedWay[0]
            startLat = parseFloat(parsedWay[1]);
            startLon = parseFloat(parsedWay[2]);
            endLat = parseFloat(parsedWay[3]);
            endLon = parseFloat(parsedWay[4]);
            if (i === 500) {
                console.log(h*(startLon - curNorthWestLon)/(curSouthEastLon - curNorthWestLon))
            }
            ctx.beginPath();
            ctx.moveTo(w*(startLat - curNorthWestLat)/(curSouthEastLat - curNorthWestLat) - 300, h*(startLon - curNorthWestLon)/(curSouthEastLon - curNorthWestLon) - 300);
            ctx.lineTo(w*(endLat - curNorthWestLat)/(curSouthEastLat - curNorthWestLat) - 300, h*(endLon - curNorthWestLon)/(curSouthEastLon - curNorthWestLon) - 300);
            ctx.strokeStyle = "red";
            if (type === "" || type === "unclassified") {
                ctx.strokeStyle = "blue";
            }
            ctx.stroke();

        }
        console.log("done")

    })
    return (
        <div className="Canvas">
            <canvas
                ref={ref}
                style={{ width: w, height: h }}
                routetorender={props.routetorender}
                onChange={(e) => props.onChange(e.target.value)}
            />

        </div>
    );
}

export default Canvas;