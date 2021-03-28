import './App.css';
import TextBox from './TextBox'
import React, {useState, useEffect, useRef} from 'react'
import { AwesomeButton } from "react-awesome-button";
import "react-awesome-button/dist/styles.css";
import axios from "axios";

// global reference to the canvas element
let canvas;

// global reference to the canvas' context
let ctx;

//http://www.petecorey.com/blog/2019/08/19/animating-a-canvas-with-react-hooks/
//helps to smooth lines drawn on canvas by increasing resolution
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

//initiate values for first render
let startLat = 41.831311;
let startLon = -71.406524;
let endLat = 41.821395;
let endLon = -71.396608;
let clickCoordinate = [];
let releaseCoordinate = [];
let releaseCoordinate2 = [];
let routeStartLat = "";
let routeStartLon = "";
let routeEndLat = "";
let routeEndLon = "";
let route = [];
let totalPathLength;
let journeyInfoString = "Enter your source and destination to find the shortest path and " +
    "how long it will take to walk there.";
let hasClicked = false;


//getters and setters for global values
function setStartLat(slat) {
    startLat = parseFloat(slat)
}

function getStartLat() {
    return parseFloat(startLat)
}

function setStartLon(slon) {
    startLon = parseFloat(slon)
}

function getStartLon() {
    return parseFloat(startLon)
}

function setEndLat(elat) {
    endLat = parseFloat(elat)
}

function getEndLat() {
    return parseFloat(endLat)
}

function setEndLon(elon) {
    endLon = parseFloat(elon)
}

function getEndLon() {
    return parseFloat(endLon)
}

function setRouteStartLat(slat) {
    routeStartLat = slat
}

function getRouteStartLat() {
    return routeStartLat
}

function setRouteStartLon(slon) {
    routeStartLon = slon
}

function getRouteStartLon() {
    return routeStartLon
}

function setRouteEndLat(elat) {
    routeEndLat = elat
}

function getRouteEndLat() {
    return routeEndLat
}

function setRouteEndLon(elon) {
    routeEndLon = elon
}

function getRouteEndLon() {
    return routeEndLon
}

function setClickCoordinate(click) {
    clickCoordinate = click
}

function getClickCoordinate() {
    return clickCoordinate
}

function setReleaseCoordinate(release) {
    releaseCoordinate = release
}

function getReleaseCoordinate() {
    return releaseCoordinate
}

function setSecondReleaseCoordinate(release) {
    releaseCoordinate2 = release
}

function getSecondReleaseCoordinate() {
    return releaseCoordinate2
}

function setRoute(r) {
    route = r
}

function getRoute() {
    return route
}


/**
 * Contains all the logic for pixel and coordinate conversions; post requests
 * for rendering ways within a bounded box and for getting the shortest path; drawing on canvas; utilities like
 * panning, zooming and scrollong;
 * @param props
 * @returns {JSX.Element}
 * @constructor for Route, which is called in App.js
 */
function Route(props) {
    const w = 600.0;
    const h = 600.0;
    //Ratio used to convert between clicks on the screen and latitudes used to define ways
    let coordToPix = Math.abs(((getStartLon() - getEndLon())/w));
    //The ratio of zooming from the most recent zoom action
    const [zoomInFactor, setZoomInFactor] = useState(1);
    //Message telling the user how long their route will take
    const [journeyStringMsg, setJourneyStringMsg] = useState("");
    //The ways being drawn on the canvas
    const [ways, setWays] = useState([]);

    // Makes an axios request and gets all ways within current map view
    const requestWays = () => {
        const toSend = {
            //passes in boundaries of current map view
            srclat : getStartLat(),
            srclong : getStartLon(),
            destlat : getEndLat(),
            destlong : getEndLon()
        };

        let config = {
            headers: {
                "Content-Type": "application/json",
                'Access-Control-Allow-Origin': '*',
            }
        }

        axios.post(
            "http://localhost:4567/ways",
            toSend,
            config
        )
            .then(response => {
                setWays(response.data["ways"]);
            })

            .catch(function (error) {
                console.log(error);
            });

    }

    let ref = useRef();
    //We use this to load the initial view of Browns campus on reloading the page
    const [firstRender, setFirstRender] = useState(true);
    useEffect(() => {
        if (firstRender) {
            setFirstRender(false)
            requestWays()
        }
        canvas = ref.current;
        //http://www.petecorey.com/blog/2019/08/19/animating-a-canvas-with-react-hooks/
        //smooths lines
        ctx = canvas.getContext("2d");
        let ratio = getPixelRatio(ctx);
        let width = getComputedStyle(canvas).getPropertyValue('width').slice(0, -2);
        let height = getComputedStyle(canvas).getPropertyValue('height').slice(0, -2);
        canvas.width = width * ratio;
        canvas.height = height * ratio;
        canvas.style.width = `${width}px`;
        canvas.style.height = `${height}px`;
        //Print route on top of ways and ways on top of background so that the nothing gets covered up
        ctx.clearRect(0, 0, canvas.width, canvas.height);
        ctx.lineWidth = 1
        ctx.fillStyle = "#e8d8c3";
        ctx.fillRect(0, 0, canvas.width, canvas.height);
        printCanvas(ways)
        printCanvas(getRoute())
    })

    const printCanvas = (toPrint) => {
        console.log("Printing " + toPrint.length + " elements")
        let parsedWay;
        let type;
        let startLatWay;
        let startLonWay;
        let endLatWay;
        let endLonWay;
        let startCircle = false;
        let startPixX;
        let startPixY;
        let endPixX;
        let endPixY;

        for (let i=0; i<toPrint.length; i++) {
            //extracts all data needed to print the way at index i
            parsedWay = toPrint[i].split(",");
            type = parsedWay[0]
            startLatWay = parseFloat(parsedWay[1]);
            startLonWay = parseFloat(parsedWay[2]);
            endLatWay = parseFloat(parsedWay[3]);
            endLonWay = parseFloat(parsedWay[4]);
            startPixX = w*(startLonWay - getStartLon())/(getEndLon() - getStartLon())
            startPixY = h*(startLatWay - getStartLat())/(getEndLat() - getStartLat())
            endPixX = w*(endLonWay - getStartLon())/(getEndLon() - getStartLon())
            endPixY = h*(endLatWay - getStartLat())/(getEndLat() - getStartLat())
            ctx.beginPath();
            ctx.moveTo(startPixX, startPixY);
            ctx.lineTo(endPixX, endPixY);
            //Traversable ways are red, non-traversable ways are blue, and routes are green
            ctx.strokeStyle = "red";
            if (type === "" || type === "unclassified") {
                ctx.strokeStyle = "blue";
            } else {
                if (type === "path") {
                    ctx.strokeStyle = "#80CA28";
                    //Route made thicker so they stand out
                    ctx.lineWidth = 5
                    ctx.stroke()
                    //startCircle used to tell us when to print the circles around the endpoints of the route
                    if (!startCircle) {
                        //Print circle at beginning of route if not yet printed
                        ctx.moveTo(startPixX, startPixY);
                        ctx.beginPath();
                        ctx.arc(startPixX, startPixY, 15, 0, 2 * Math.PI);
                        startCircle = true
                    } else {
                        if (i === toPrint.length - 1) {
                            //Print circle at end of route if this is the last way being printed
                            ctx.moveTo(endPixX, endPixY);
                            ctx.beginPath();
                            ctx.arc(endPixX, endPixY, 15, 0, 2 * Math.PI);
                        }
                    }
                    startLatWay = parseFloat(parsedWay[1]);
                    startLonWay = parseFloat(parsedWay[2]);
                    endLatWay = parseFloat(parsedWay[3]);
                    endLonWay = parseFloat(parsedWay[4]);
                    //Updating the message telling user how long their route is and will take to walk
                    totalPathLength = totalPathLength + (Math.sqrt(Math.pow(startLatWay-endLatWay, 2) +
                        Math.pow(startLonWay-endLonWay, 2)));
                    journeyInfoString = "Your journey will be "+Number((totalPathLength*69).toFixed(2))+
                        " miles. This will take you "+Number((totalPathLength*22.25*60).toFixed(2))+
                        " minutes by foot."

                }
            }
            ctx.stroke();
            //reset line width for non-route ways
            ctx.lineWidth = 1
        }
    }

    // Makes an axios request and gets the route between the two points defined by the user
    const refreshButton = () => {
        ctx.fillStyle = "#e8d8c3";
        ctx.clearRect(0, 0, canvas.width, canvas.height);
        ctx.fillRect(0, 0, canvas.width, canvas.height);
        printCanvas(ways)
        //Used to convert start and end points to coordinates
        //in case user inputted one set of coordinates and one pair of street names
        if (getRouteStartLat()==="" && getRouteStartLon()==="" && getRouteEndLat()!=="" && getRouteEndLon()!==""){
            setRouteStartLon((getReleaseCoordinate()[0]*(getEndLon() - getStartLon())/w +  getStartLon()).toString())
            setRouteStartLat((getReleaseCoordinate()[1]*(getEndLat() - getStartLat())/h +  getStartLat()).toString())

        } else if (getRouteEndLat()==="" && getRouteEndLon()==="" &&
            getRouteStartLat()!=="" && getRouteStartLon()!==""){

            setRouteEndLon((getReleaseCoordinate()[0]*(getEndLon() - getStartLon())/w +  getStartLon()).toString())
            setRouteEndLat((getReleaseCoordinate()[1]*(getEndLat() - getStartLat())/h +  getStartLat()).toString())
        }

        const toSend = {
            srclat : getRouteStartLat(),
            srclong : getRouteStartLon(),
            destlat : getRouteEndLat(),
            destlong : getRouteEndLon()
        };

        let config = {
            headers: {
                "Content-Type": "application/json",
                'Access-Control-Allow-Origin': '*',
            }
        }

        axios.post(
            "http://localhost:4567/route",
            toSend,
            config
        )
            .then(response => {
                let currentRoute = response.data["route"]
                totalPathLength = 0;
                //find path
                setRoute(currentRoute);
                //print path
                printCanvas(currentRoute)
                //tell user length and walking time of path
                setJourneyStringMsg(journeyInfoString)
            })

            .catch(function (error) {
                console.log(error);
            });
    }

    const clickedOnCanvas = (clicked) => {
            setClickCoordinate(clicked)
    }

    const releasedOnCanvas = (released) => {
        setReleaseCoordinate(released)
        if(getClickCoordinate()[0] === getReleaseCoordinate()[0] &&
            getClickCoordinate()[1] === getReleaseCoordinate()[1]) {
            //Selecting route start/end if clicked and released on same coordinate
            if (hasClicked){
                // clicked and released second coordinate, time to calculate route
                setRouteStartLon(getSecondReleaseCoordinate()[0]*(getEndLon() - getStartLon())/w +  getStartLon())
                setRouteStartLat(getSecondReleaseCoordinate()[1]*(getEndLat() - getStartLat())/h +  getStartLat())
                setRouteEndLon(released[0]*(getEndLon() - getStartLon())/w +  getStartLon())
                setRouteEndLat(released[1]*(getEndLat() - getStartLat())/h +  getStartLat())
                //finds route and print
                refreshButton();
                hasClicked = false;
            } else {
                //clicked and released first coordinate
                setSecondReleaseCoordinate(released)
                hasClicked = true;
            }
        } else {
            //Panning if clicked and released on different coordinates
            let dX = (parseFloat(getReleaseCoordinate()[0]) - parseFloat(getClickCoordinate()[0]))*coordToPix
            let dY = (parseFloat(getReleaseCoordinate()[1]) - parseFloat(getClickCoordinate()[1]))*coordToPix
                setStartLat(getStartLat() + dY);
                setEndLat(getEndLat() + dY);
                setEndLon(getEndLon() - dX);
                setStartLon(getStartLon() - dX);
                //reprint way and route with new boundaries
                requestWays();
                printCanvas(getRoute())


        }
    }

    const scrollHandler = (scrollVal) => {
        if (scrollVal<0){
            //zooming in
            setZoomCoords(0.91)
        } else if (scrollVal>0){
            //zooming out
            setZoomCoords(1.1)
        }
    }

    const setZoomCoords = (ratio) => {
        //set bounding box based on zoom ratio
        const avgLat = ((getStartLat() + getEndLat())/2)
        const avgLon = ((getStartLon() + getEndLon())/2)
        const newStartLat = avgLat + (Math.abs(getStartLat() - avgLat) * ratio);
        const newEndLat = avgLat - (Math.abs(getEndLat() - avgLat) * ratio);
        const newEndLon = avgLon + (Math.abs(getEndLon() - avgLon) * ratio);
        const newStartLon = avgLon - (Math.abs(getStartLon() - avgLon) * ratio);
        setStartLat(newStartLat);
        setEndLat(newEndLat);
        setEndLon(newEndLon);
        setStartLon(newStartLon);
        //reprint canvas if zooming out
        if (zoomInFactor > 1) {
            requestWays();
        } else {
            ctx.clearRect(0, 0, canvas.width, canvas.height);
            ctx.lineWidth = 1
            ctx.fillStyle = "#e8d8c3";
            ctx.fillRect(0, 0, canvas.width, canvas.height);
            printCanvas(ways)
            printCanvas(getRoute())
        }
        setZoomInFactor(ratio);
  }

  return (
    <div className="Route">
      <header className="Route-header">
        <title>This is a title</title>
      </header>
        <TextBox label={"Street 1 or Start Latitude "} onChange = {setRouteStartLat}/>
        <TextBox label={"Cross Street 1 or Start Longitude "} onChange = {setRouteStartLon}/>
        <TextBox label={"Street 2 or End Latitude "} onChange = {setRouteEndLat}/>
        <TextBox label={"Cross Street 2 or End Longitude "} onChange = {setRouteEndLon}/>
        <AwesomeButton type="primary" onPress={() => {refreshButton()}}>Find Path</AwesomeButton>
        <AwesomeButton type="primary" onPress={() => {setZoomCoords(0.91)}}>+</AwesomeButton>
        <AwesomeButton type="primary" onPress={() => {setZoomCoords(1.1)}}>-</AwesomeButton>
        <p>
            {journeyStringMsg}
      </p>
        <canvas
            ref={ref}
            style={{ width: w, height: h }}
            onChange={(e) => props.onChange(e.target.value)}
            onMouseDown={(e) => clickedOnCanvas([e.pageX - canvas.offsetLeft, e.pageY - canvas.offsetTop])}
            onMouseUp={(e) => releasedOnCanvas([e.pageX - canvas.offsetLeft, e.pageY - canvas.offsetTop])}
            onWheel = {(e) => scrollHandler(e.deltaY)}
        />
    </div>
  );
}
export default Route;