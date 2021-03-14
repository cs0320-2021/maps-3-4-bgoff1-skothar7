import './App.css';

let curNorthWestLat;
let curNorthWestLon;

let curSouthEastLat;
let curSouthEastLon;

let pixToCoord;

// global reference to the oldCanvas element
let canvas;

// global reference to the oldCanvas' context
let ctx;

function Canvas(props) {

    useEffect(() => {

        canvas.width = 800;
        canvas.height = 500;
        pixToCoord = 200;

        curSouthEastLat = 41.823876;
        curSouthEastLon = -71.395963;

        curNorthWestLat = (curSouthEastLat - canvas.width/pixToCoord);
        curNorthWestLon = (curSouthEastLon - canvas.height/pixToCoord);

        // TODO: set up the oldCanvas context

        //<oldCanvas id="myCanvas" width="200" height="100"></oldCanvas>
        //var c = document.getElementById("myCanvas");
        ctx = oldCanvas.getContext("2d");

        //var firstList =
        var listOfWays = props.routeToRender.split(";");

        var i;
        var parsedWay;
        var type;
        var startLat;
        var startLon;
        var EndLat;
        var EndLon;
        for(i=0; i<listOfWays.length; i++){
            parsedWay =  listOfWays[i].split(",");
            type = parsedWay[0]
            startLat = parseFloat(parsedWay[1]);
            startLon = parseFloat(parsedWay[2]);
            EndLat = parseFloat(parsedWay[3]);
            EndLon = parseFloat(parsedWay[4]);

            ctx.beginPath();
            ctx.moveTo((startLat - curNorthWestLat)*pixToCoord + canvas.offsetLeft , (startLon - curNorthWestLon)*pixToCoord + canvas.offsetTop);
            ctx.lineTo((EndLat - curNorthWestLat)*pixToCoord + canvas.offsetLeft , (EndLon - curNorthWestLon)*pixToCoord + canvas.offsetTop);
            ctx.stroke();


        }

    })
    return (
        <div className="Canvas">

            <input  type="text" label={props.label} onChange={(e) => props.onChange(e.target.value)}/>
        </div>
    );
}

export default Canvas;