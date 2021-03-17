import './App.css';
import React, {useEffect, useRef} from "react";
import {requestRoute} from "./Route"

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
    const firstUpdate = useRef(true);
    useEffect(() => {
        if (firstUpdate.current) {
            curSouthEastLat = 41.822895
            curSouthEastLon = -71.396608
            curNorthWestLat = 41.828693
            curNorthWestLon = -71.406524
            firstUpdate.current = false
            requestRoute()
        } else {
            curSouthEastLat = parseFloat(props.routetorender[3]);
            curSouthEastLon = parseFloat(props.routetorender[4]);
            curNorthWestLat = parseFloat(props.routetorender[1]);
            curNorthWestLon = parseFloat(props.routetorender[2]);
        }
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
        pixToCoord = 50000;

        //default
        // curSouthEastLat = 41.823876;
        // curSouthEastLon = -71.395963;
        //
        // curNorthWestLat = (curSouthEastLat - w/pixToCoord);
        // curNorthWestLon = (curSouthEastLon - h/pixToCoord);

        //with ways

        console.log(curSouthEastLat);
        console.log(curSouthEastLon);
        console.log(curNorthWestLat);
        console.log(curNorthWestLon);


        let rawWaysData = props.routetorender[0]
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

        pixToCoord = ((curNorthWestLon - curSouthEastLon)/w);
        h = (curNorthWestLat - curSouthEastLat)/pixToCoord;

        console.log("low" + listOfWays.length);
        for (let i=0; i<listOfWays.length; i++) {
            parsedWay =  listOfWays[i].split(",");
            type = parsedWay[0]
            startLat = parseFloat(parsedWay[1]);
            startLon = parseFloat(parsedWay[2]);
            endLat = parseFloat(parsedWay[3]);
            endLon = parseFloat(parsedWay[4]);
            if (i === 500) {
                console.log(w*(startLon - curNorthWestLon)/(curSouthEastLon - curNorthWestLon));
            }
            ctx.beginPath();
            ctx.moveTo(w*(startLon - curNorthWestLon)/(curSouthEastLon - curNorthWestLon), 0 - h*(startLat - curNorthWestLat)/(curSouthEastLat - curNorthWestLat));
            ctx.lineTo(w*(endLon - curNorthWestLon)/(curSouthEastLon - curNorthWestLon), 0 - h*(endLat - curNorthWestLat)/(curSouthEastLat - curNorthWestLat));
             //ctx.moveTo(w*(startLon - curNorthWestLon)/(curSouthEastLon - curNorthWestLon), h-h*(startLat - curNorthWestLat)/(curSouthEastLat - curNorthWestLat));
             //ctx.lineTo(w*(endLon - curNorthWestLon)/(curSouthEastLon - curNorthWestLon), h-h*(endLat - curNorthWestLat)/(curSouthEastLat - curNorthWestLat));
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
                onMouseDown={(e) => props.onMouseDown([e.pageX, e.pageY])}
                onMouseUp={(e) => props.onMouseUp([e.pageX, e.pageY])}
            />

        </div>
    );
}

export default Canvas;