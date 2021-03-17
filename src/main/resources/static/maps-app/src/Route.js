import logo from './logo.svg';
import './App.css';
import TextBox from './TextBox'
import React, {useState, useEffect} from 'react'
import ReactDOM from 'react-dom';
import { AwesomeButton } from "react-awesome-button";
import "react-awesome-button/dist/styles.css";
import axios from "axios";
import Canvas from "./Canvas";

function Route() {
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

    let toCanvas = [route[3], startLat, startLon, endLat, endLon];

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
        <Canvas routetorender={toCanvas} onChange = {refreshCanvas} onMouseDown = {setClickCoordinate} onMouseUp = {clickedOnCanvas}/>
    </div>
  );
}
export default Route;
