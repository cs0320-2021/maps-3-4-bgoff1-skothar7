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



function Route(props) {
    const w = 600.0;
    const h = 600.0;
    let coordToPix = parseFloat((getStartLon() - getEndLon())/w);
    const [startLatBuffer, setStartLatBuffer] = useState(0);
    const [startLonBuffer, setStartLonBuffer] = useState(0);
    const [endLatBuffer, setEndLatBuffer] = useState(0);
    const [endLonBuffer, setEndLonBuffer] = useState(0);
    const [zoomInFactor, setZoomInFactor] = useState(1);
    const [startCanvas, refreshCanvas] = useState("");
//TODO: Fill in the ? with appropriate names/values for a route.
//Hint: The defaults for latitudes and longitudes were 0s. What might the default useState value for a route be?
    const [route, setRoute] = useState([]);
    //let toCanvas = [route[3], startLat, startLon, endLat, endLon];

    /**
     * Makes an axios request.
     */
    const requestRoute = () => {
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
            "http://localhost:4567/route",
            toSend,
            config
        )
            .then(response => {
                console.log(response.data);
                //TODO: Go to the Main.java in the server from the stencil, and find what variable you should put here.
                //Note: It is very important that you understand how this is set up and why it works!
                setRoute(response.data["route"]);//console.log  the response.data["route"]
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
                requestRoute()
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
            console.log(getEndLat());
            console.log(getStartLat());
            console.log(getStartLon());


            let rawWaysData = route[3]
            let listOfWays = [];
            if (rawWaysData != null) {
                listOfWays = (rawWaysData + '').split(";");
            }

            let parsedWay;
            let type;
            let startLatWay;
            let startLonWay;
            let endLatWay;
            let endLonWay;

            console.log("low" + listOfWays.length);
            for (let i=0; i<listOfWays.length; i++) {
                parsedWay =  listOfWays[i].split(",");
                type = parsedWay[0]
                startLatWay = parseFloat(parsedWay[1]);
                startLonWay = parseFloat(parsedWay[2]);
                endLatWay = parseFloat(parsedWay[3]);
                endLonWay = parseFloat(parsedWay[4]);
                if (i === 500) {
                    console.log(w*(startLonWay - startLon)/(endLon - startLon));
                }
                ctx.beginPath();
                ctx.moveTo(w*(startLonWay - getStartLon())/(getEndLon() - getStartLon()), h*(startLatWay - getStartLat())/(getEndLat() - getStartLat()));
                ctx.lineTo(w*(endLonWay - getStartLon())/(getEndLon() - getStartLon()), h*(endLatWay - getStartLat())/(getEndLat() - getStartLat()));
                //ctx.moveTo(w*(startLon - startLon)/(endLon - startLon), h-h*(startLat - startLat)/(endLat - startLat));
                //ctx.lineTo(w*(endLon - startLon)/(endLon - startLon), h-h*(endLat - startLat)/(endLat - startLat));
                ctx.strokeStyle = "red";
                if (type === "" || type === "unclassified") {
                    ctx.strokeStyle = "blue";
                } else {
                    if (type === "path") {
                        ctx.strokeStyle = "pink";
                    }
                }
                ctx.stroke();
            }
            console.log("done")

        })

    const refreshButton = () => {
        setStartLat(startLatBuffer)
        setStartLon(startLonBuffer)
        setEndLat(endLatBuffer)
        setEndLon(endLonBuffer)
        requestRoute()
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
            "http://localhost:4567/path",
            toSend,
            config
        )
            .then(response => {
                console.log(response.data);
                //TODO: Go to the Main.java in the server from the stencil, and find what variable you should put here.
                //Note: It is very important that you understand how this is set up and why it works!
                setRoute(route.concat(response.data["path"]));//console.log  the response.data["route"]
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
        if(getClickCoordinate() === getReleaseCoordinate()) {
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
                requestRoute();
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
      requestRoute();
  }

  return (
    <div className="Route">
      <header className="Route-header">
        <title>This is a title</title>
      </header>
        <h1>{startLat}</h1>
        <TextBox label={"Start Latitude"} onChange = {setStartLatBuffer}/>
        <TextBox label={"Start Longitude"} onChange = {setStartLonBuffer}/>
        <TextBox label={"End Latitude"} onChange = {setEndLatBuffer}/>
        <TextBox label={"End Longitude"} onChange = {setEndLonBuffer}/>
        <AwesomeButton type="primary" onPress={() => {refreshButton()}}>Refresh</AwesomeButton>
        <AwesomeButton type="primary" onPress={() => {zoomIn()}}>+</AwesomeButton>
        <AwesomeButton type="primary" onPress={() => {zoomOut()}}>-</AwesomeButton>
        <p>{route[2]}</p>
        <div className="Canvas">
            <canvas
                ref={ref}
                style={{ width: w, height: h }}
                routetorender={props.routetorender}
                onChange={(e) => props.onChange(e.target.value)}
                onMouseDown={(e) => clickedOnCanvas([e.pageX, e.pageY])}
                onMouseUp={(e) => releasedOnCanvas([e.pageX, e.pageY])}
            />

        </div>
    </div>
  );
}
export default Route;

//<Canvas routetorender={toCanvas} onChange = {refreshCanvas} //onMouseDown = {setClickCoordinate} onMouseUp = {clickedOnCanvas}