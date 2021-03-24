import logo from './logo.svg';
import './App.css';
import axios from "axios";
import React, {useState, useEffect, useRef} from 'react'

let allCheckins = ""

function CheckinFeed() {

    const interval = setInterval(() => {
        getCheckins();
    }, 3000);

    const [newCheckins, setNewCheckins] = useState([])

    /**
     * Makes an axios request.
     */
    const getCheckins = () => {
        clearInterval(interval)
        const toSend = {
            //TODO: Pass in the values for the data. Follow the format the route expects!
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
            "http://localhost:4567/checkin",
            toSend,
            config
        )
            .then(response => {
                console.log(response.data);
                //TODO: Go to the Main.java in the server from the stencil, and find what variable you should put here.
                //Note: It is very important that you understand how this is set up and why it works!
                setNewCheckins(response.data["checkins"]);
            })

            .catch(function (error) {
                console.log(error);
            });
    }

    useEffect(() => {
        for (let i=0; i < newCheckins.length; i++) {
            let currentCheckin = newCheckins[i].split(",")
            allCheckins = allCheckins + "<option value="+currentCheckin[0]+">"+currentCheckin[0]+currentCheckin[1]
                +currentCheckin[2]+currentCheckin[3]+currentCheckin[4]+"</option>"
        }
    })

    return (
        <div className="CheckinFeed">
            <select name="checkins" multiple size="10">
                <optgroup label="checkin-list">
                {allCheckins}
                </optgroup>
            </select>
         </div>
    );
}

export default CheckinFeed;

// $(function()){
//     setInterval(funcEverySec, 1000);
// });
//
// function funcEverySec {
//
// }