import logo from './logo.svg';
import './App.css';
import TextBox from './TextBox'
import React, {useState, useEffect, useRef} from 'react'
import ReactDOM from 'react-dom';
import { AwesomeButton } from "react-awesome-button";
import "react-awesome-button/dist/styles.css";
import axios from "axios";

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

let startLat = 41.831311;
let startLon = -71.406524;
let endLat = 41.821395;
let endLon = -71.396608;
let clickCoordinate = [];
let releaseCoordinate = [];
let clickCoordinate2 = [];
let releaseCoordinate2 = [];
let routeStartLat;
let routeStartLon;
let routeEndLat;
let routeEndLon;

let hasClicked = false;

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
    routeStartLat = parseFloat(slat)
}

function getRouteStartLat() {
    return parseFloat(routeStartLat)
}

function setRouteStartLon(slon) {
    routeStartLon = parseFloat(slon)
}

function getRouteStartLon() {
    return parseFloat(routeStartLon)
}

function setRouteEndLat(elat) {
    routeEndLat = parseFloat(elat)
}

function getRouteEndLat() {
    return parseFloat(routeEndLat)
}

function setRouteEndLon(elon) {
    routeEndLon = parseFloat(elon)
}

function getRouteEndLon() {
    return parseFloat(routeEndLon)
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

function setSecondClickCoordinate(click) {
    clickCoordinate2 = click
}

function getSecondClickCoordinate() {
    return clickCoordinate2
}



function Route(props) {
    const w = 600.0;
    const h = 600.0;
    let coordToPix = parseFloat((getStartLon() - getEndLon())/w);
    const [zoomInFactor, setZoomInFactor] = useState(1);
    const [startCanvas, refreshCanvas] = useState("");
//TODO: Fill in the ? with appropriate names/values for a route.
//Hint: The defaults for latitudes and longitudes were 0s. What might the default useState value for a route be?
    const [ways, setWays] = useState([]);
    const [route, setRoute] = useState([]);
    //let toCanvas = [route[3], startLat, startLon, endLat, endLon];

    /**
     * Makes an axios request.
     */
    const requestWays = () => {
        console.log(9 + " " + startLon)
        const toSend = {
            //TODO: Pass in the values for the data. Follow the format the route expects!
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

        //Install and import this!
        //TODO: Fill in 1) location for request 2) your data 3) configuration
        axios.post(
            "http://localhost:4567/ways",
            toSend,
            config
        )
            .then(response => {
                console.log(response.data);
                //TODO: Go to the Main.java in the server from the stencil, and find what variable you should put here.
                //Note: It is very important that you understand how this is set up and why it works!
                setWays(response.data["ways"]);//console.log  the response.data["route"]
            })

            .catch(function (error) {
                console.log(error);
            });
    }

        let ref = useRef();
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
            ctx.clearRect(0, 0, canvas.width, canvas.height);
            ctx.lineWidth = 1

            console.log(getEndLat());
            console.log(getEndLon());
            console.log(getStartLat());
            console.log(getStartLon());

            printCanvas(ways)
            printCanvas(route)

            console.log("done")

        })

    const printCanvas = (toPrint) => {
            console.log(toPrint.length)
        let parsedWay;
        let type;
        let startLatWay;
        let startLonWay;
        let endLatWay;
        let endLonWay;
        let startCircle;
        let startPixX;
        let startPixY;
        let endPixX;
        let endPixY;

        startCircle = false

        console.log("low" + toPrint.length);
        for (let i=0; i<toPrint.length; i++) {
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
            //ctx.moveTo(w*(startLon - startLon)/(endLon - startLon), h-h*(startLat - startLat)/(endLat - startLat));
            //ctx.lineTo(w*(endLon - startLon)/(endLon - startLon), h-h*(endLat - startLat)/(endLat - startLat));
            ctx.strokeStyle = "red";
            if (type === "" || type === "unclassified") {
                ctx.strokeStyle = "blue";
            } else {
                if (type === "path") {
                    console.log("yes")
                    ctx.strokeStyle = "pink";
                    ctx.lineWidth = 5
                    ctx.stroke()
                    if (!startCircle) {
                        //ctx.moveTo(startPixX, startPixY);
                        //ctx.beginPath();
                        //ctx.arc(startPixX, startPixY, 15, 0, 2 * Math.PI);
                        //ctx.rect(startPixX - 21, startPixY - 21, 42, 42)
                        startCircle = true
                    } else {
                        if (i === toPrint.length - 1) {
                            //ctx.moveTo(endPixX, endPixY);
                            //ctx.beginPath();
                            //ctx.arc(endPixX, endPixY, 15, 0, 2 * Math.PI);
                            //ctx.rect(endPixX - 21, endPixY - 21, 42, 42)
                        }
                    }
                }
            }
            ctx.stroke();
            ctx.lineWidth = 1
        }
    }
    const refreshButton = () => {

            console.log(ways.length)
        console.log(hasClicked)

        requestWays()
        const toSend = {
            //TODO: Pass in the values for the data. Follow the format the route expects!
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

        //Install and import this!
        //TODO: Fill in 1) location for request 2) your data 3) configuration
        axios.post(
            "http://localhost:4567/route",
            toSend,
            config
        )
            .then(response => {
                console.log(response.data);
                //TODO: Go to the Main.java in the server from the stencil, and find what variable you should put here.
                //Note: It is very important that you understand how this is set up and why it works!
                let currentRoute = response.data["route"]
                setRoute(currentRoute);//console.log  the response.data["route"]
                printCanvas(currentRoute)//.slice(0, 10))
            })

            .catch(function (error) {
                console.log(error);
            });

        console.log(ways.length)

        //to test
        //41.825158
        // -71.404687
        //41.827525
        //-71.400441



    }

    //clickedOnCanvas setter sets pixels x,y
    //release does the same


    const clickedOnCanvas = (clicked) => {


            setClickCoordinate(clicked)


    }

    const releasedOnCanvas = (released) => {
        setReleaseCoordinate(released)
        console.log(getClickCoordinate()[0])
        console.log(getReleaseCoordinate()[0])
        console.log(getClickCoordinate()[1])
        console.log(getReleaseCoordinate()[1])
        if(getClickCoordinate()[0] === getReleaseCoordinate()[0] &&
            getClickCoordinate()[1] === getReleaseCoordinate()[1]) {

            console.log("clicked and released")
            if (hasClicked){

                console.log("entered clicked condition")

                //make post req
                setRouteStartLon(getStartLon() + getSecondReleaseCoordinate()[0]*coordToPix)
                setRouteStartLat(getStartLat() - getSecondReleaseCoordinate()[1]*coordToPix)
                setRouteEndLon(getStartLon() + parseFloat(released[0])*coordToPix)
                setRouteEndLat(getStartLat() - parseFloat(released[1])*coordToPix)
                console.log(getRouteStartLat())
                console.log(getRouteStartLon())
                console.log(getRouteEndLat())
                console.log(getRouteEndLon())

                refreshButton();

                hasClicked = false;
            } else {
                setSecondReleaseCoordinate(released)
                hasClicked = true;
            }
            //route start/end
        } else {
            //panning
            let dX = (parseFloat(getReleaseCoordinate()[0]) - parseFloat(getClickCoordinate()[0]))*coordToPix
            let dY = (parseFloat(getReleaseCoordinate()[1]) - parseFloat(getClickCoordinate()[1]))*coordToPix
            console.log(releaseCoordinate[0])
                setStartLat(getStartLat() - dY);
                setEndLat(getEndLat() - dY);
                setEndLon(getEndLon() + dX);
                setStartLon(getStartLon() + dX);
                requestWays();


        }
    }

  const zoomIn = () => {
      setZoomInFactor(0.91);
      setZoomCoords();
  }

  const zoomOut = () => {
      setZoomInFactor(1.1);
      setZoomCoords();
  }

  const setZoomCoords = () => {
      console.log(zoomInFactor);

      let latFromCenter = Math.abs(Math.abs((getStartLat() - (getStartLat() + getEndLat())/2)) * zoomInFactor);
      let lonFromCenter = Math.abs(Math.abs((getEndLon() -  (getEndLon() + getStartLon())/2)) * zoomInFactor);

      const avgLat = ((getStartLat() + getEndLat())/2)
      const avgLon = ((getStartLon() + getEndLon())/2)

      const newStartLat = avgLat + (Math.abs(getStartLat() - avgLat) * zoomInFactor);
      console.log(newStartLat)
      const newEndLat = avgLat - (Math.abs(getEndLat() - avgLat) * zoomInFactor);
      console.log(newEndLat);
      const newEndLon = avgLon + (Math.abs(getEndLon() - avgLon) * zoomInFactor);
      console.log(newEndLon);
      const newStartLon = avgLon - (Math.abs(getStartLon() - avgLon) * zoomInFactor);
      console.log((newStartLon));
      ReactDOM.unstable_batchedUpdates(() => {
          setStartLat(newStartLat);
          setEndLat(newEndLat);
          setEndLon(newEndLon);
          setStartLon(newStartLon);
      })
      requestWays();
  }

  return (
    <div className="Route">
      <header className="Route-header">
        <title>This is a title</title>
      </header>
        <h1>{startLat}</h1>
        <TextBox label={"Start Latitude"} onChange = {setRouteStartLat}/>
        <TextBox label={"Start Longitude"} onChange = {setRouteStartLon}/>
        <TextBox label={"End Latitude"} onChange = {setRouteEndLat}/>
        <TextBox label={"End Longitude"} onChange = {setRouteEndLon}/>
        <AwesomeButton type="primary" onPress={() => {refreshButton()}}>Find Path</AwesomeButton>
        <AwesomeButton type="primary" onPress={() => {zoomIn()}}>+</AwesomeButton>
        <AwesomeButton type="primary" onPress={() => {zoomOut()}}>-</AwesomeButton>
        <p>{ways[2]}</p>
        <div className="Canvas">
            <canvas
                ref={ref}
                style={{ width: w, height: h }}
                routetorender={props.routetorender}
                onChange={(e) => props.onChange(e.target.value)}
                onMouseDown={(e) => clickedOnCanvas([e.pageX, e.pageY])}
                onMouseUp={(e) => releasedOnCanvas([e.pageX, e.pageY])}
            />
            hi
        </div>
    </div>
  );
}
export default Route;

//<Canvas routetorender={toCanvas} onChange = {refreshCanvas} //onMouseDown = {setClickCoordinate} onMouseUp = {clickedOnCanvas}