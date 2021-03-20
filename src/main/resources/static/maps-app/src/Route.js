import logo from './logo.svg';
import './App.css';
import TextBox from './TextBox'
import React, {useState, useEffect, useRef} from 'react'
import ReactDOM from 'react-dom';
import { AwesomeButton } from "react-awesome-button";
import "react-awesome-button/dist/styles.css";
import axios from "axios";

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



function Route(props) {

    const [startLat, setStartLat] = useState(0);
    const [startLon, setStartLon] = useState(0);
    const [endLat, setEndLat] = useState(0);
    const [endLon, setEndLon] = useState(0);
    const [startLatBuffer, setStartLatBuffer] = useState(0);
    const [startLonBuffer, setStartLonBuffer] = useState(0);
    const [endLatBuffer, setEndLatBuffer] = useState(0);
    const [endLonBuffer, setEndLonBuffer] = useState(0);
    const [clickCoordinate, setClickCoordinate] = useState([]);
    const [releaseCoordinate, setReleaseCoordinate] = useState([]);
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
        const toSend = {
            //TODO: Pass in the values for the data. Follow the format the route expects!
            srclat : startLat,
            srclong : startLon,
            destlat : endLat,
            destlong : endLon
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

    //function Canvas(props) {
        let w = 600;
        let h = 600;
        let ref = useRef();
        const firstUpdate = useRef(true);
        useEffect(() => {
            if (firstUpdate.current) {
                // setEndLat(41.822895)
                // setEndLon(-71.396608)
                console.log(5)
                // setStartLat(41.828693)
                // setStartLon(-71.406524)

                ReactDOM.unstable_batchedUpdates(() => {
                    setStartLat(41.828693);
                    setEndLat(41.822895);
                    setEndLon(-71.396608);
                    setStartLon(-71.406524);
                    console.log(6)
                    //requestRoute();
                })
                firstUpdate.current = false

                requestRoute()
            } else {
                console.log(7)
                // endLat = parseFloat(props.routetorender[3]);
                // endLon = parseFloat(props.routetorender[4]);
                // startLat = parseFloat(props.routetorender[1]);
                // startLon = parseFloat(props.routetorender[2]);

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
            // endLat = 41.823876;
            // endLon = -71.395963;
            //
            // startLat = (endLat - w/pixToCoord);
            // startLon = (endLon - h/pixToCoord);

            //with ways

            console.log(endLat);
            console.log(endLon);
            console.log(startLat);
            console.log(startLon);


            let rawWaysData = route[3]//props.routetorender[0]
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


            pixToCoord = ((startLon - endLon)/w);
            h = (startLat - endLat)/pixToCoord;

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
                ctx.moveTo(w*(startLonWay - startLon)/(endLon - startLon), 0 - h*(startLatWay - startLat)/(endLat - startLat));
                ctx.lineTo(w*(endLonWay - startLon)/(endLon - startLon), 0 - h*(endLatWay - startLat)/(endLat - startLat));
                //ctx.moveTo(w*(startLon - startLon)/(endLon - startLon), h-h*(startLat - startLat)/(endLat - startLat));
                //ctx.lineTo(w*(endLon - startLon)/(endLon - startLon), h-h*(endLat - startLat)/(endLat - startLat));
                ctx.strokeStyle = "red";
                if (type === "" || type === "unclassified") {
                    ctx.strokeStyle = "blue";
                }
                ctx.stroke();

            }
            console.log("done")

        })
        // return (
        //
        // );
   // }




    const refreshButton = () => {
        setStartLat(startLatBuffer)
        setStartLon(startLonBuffer)
        setEndLat(endLatBuffer)
        setEndLon(endLonBuffer)
        requestRoute()
    }

    const clickedOnCanvas = (released) => {
        setReleaseCoordinate(released)
        if(clickCoordinate === releaseCoordinate) {
            //route start/end
        } else {
            //panning
            let dX = releaseCoordinate[0] - clickCoordinate[0]
            let dY = releaseCoordinate[1] - clickCoordinate[1]
            ReactDOM.unstable_batchedUpdates(() => {
                setStartLat();
                setEndLat();
                setEndLon();
                setStartLon();
                requestRoute();
            })
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

      let latFromCenter = Math.abs(Math.abs((startLat - (startLat + endLat)/2)) * zoomInFactor);
      let lonFromCenter = Math.abs(Math.abs((endLon -  (endLon + startLon)/2)) * zoomInFactor);

      //const oldStartLat = startLat;
      //const oldStartLon = startLon;
      //const oldEndLat = endLat;
      //const oldEndLon = endLon;

      //console.log(oldStartLat);
      //console.log(oldStartLon);
      //console.log(oldEndLat);
      //console.log(oldEndLon);

      const avgLat = ((startLat+endLat)/2)
      const avgLon = ((startLon+endLon)/2)

      const newStartLat = avgLat + (Math.abs(startLat - avgLat) * zoomInFactor);
      console.log(newStartLat)
      const newEndLat = avgLat - (Math.abs(endLat - avgLat) * zoomInFactor);
      console.log(newEndLat);
      const newEndLon = avgLon + (Math.abs(endLon - avgLon) * zoomInFactor);
      console.log(newEndLon);
      const newStartLon = avgLon - (Math.abs(startLon - avgLon) * zoomInFactor);
      console.log((newStartLon));// + 3)/2);// - ((oldEndLon -  (oldEndLon + 3)/2) * 1));
      ReactDOM.unstable_batchedUpdates(() => {
          setStartLat(newStartLat);
          setEndLat(newEndLat);
          setEndLon(newEndLon);
          setStartLon(newStartLon);
          requestRoute();
      })
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
                //onMouseDown={(e) => props.onMouseDown([e.pageX, e.pageY])}
                //onMouseUp={(e) => props.onMouseUp([e.pageX, e.pageY])}
            />

        </div>


    </div>
  );
}
export default Route;

//<Canvas routetorender={toCanvas} onChange = {refreshCanvas} //onMouseDown = {setClickCoordinate} onMouseUp = {clickedOnCanvas}