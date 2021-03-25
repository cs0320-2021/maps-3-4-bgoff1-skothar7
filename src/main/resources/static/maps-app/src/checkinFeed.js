import logo from './logo.svg';
import './App.css';
import axios from "axios";
import React, {useState, useEffect} from 'react'
import {AwesomeButton} from "react-awesome-button";
import TextBox from "./TextBox";

let allCheckins = ""

let user = "";

function setUser(u) {
    user = u
}

function getUser() {
    return user
}

function CheckinFeed() {

    const [newCheckins, setNewCheckins] = useState([])
    const [option, setOption] = useState("h")
    let userData = ""




    /**
     * Makes an axios request.
     */
    const getCheckins = () => {
        const toSend = {
            dummy : 42,
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
                //TODO: Go to the Main.java in the server from the stencil, and find what variable you should put here.
                //Note: It is very important that you understand how this is set up and why it works!
                if(response != undefined) {
                    setNewCheckins(response.data["checkins"]);
                }
            })

            .catch(function (error) {
                console.log(error);
                //console.log(error.response.data);
            });
    }

    const interval = setInterval(() => {
        getCheckins();
    }, 3000);

    const searchForUser = () => {
            const toSend = {
                id: getUser(),
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
                "http://localhost:4567/user",
                toSend,
                config
            )
                .then(response => {
                    //TODO: Go to the Main.java in the server from the stencil, and find what variable you should put here.
                    //Note: It is very important that you understand how this is set up and why it works!
                    if (response != undefined) {
                        let rawUserData = response.data["user"]
                        userData = ""
                        for (let j = 0; j < rawUserData.length; j++) {
                            let currentUserCheckin = rawUserData[j].split(",")
                            userData = userData + currentUserCheckin[0] + ", " + currentUserCheckin[1]
                                + ", " + currentUserCheckin[2] + ", " + currentUserCheckin[3] + ", " + currentUserCheckin[4]
                            console.log(userData)
                        }
                    }
                })

                .catch(function (error) {
                    console.log(error);
                    //console.log(error.response.data);
                });
    }

    useEffect(() => {
        for (let i=0; i < newCheckins.length; i++) {
            let currentCheckin = newCheckins[i].split(",")
            allCheckins = allCheckins + "<option value="+currentCheckin[0]+">"+currentCheckin[0] + ", " + currentCheckin[1]
                + ", " + currentCheckin[2] + ", " + currentCheckin[3] + ", " + currentCheckin[4]+"</option>"
        }
    })

    return (
        <div className="CheckinFeed">
            <select onChange={(e) => {setOption(e.target.value)}} name="checkins" multiple size="10">
                <optgroup label="checkin-list" dangerouslySetInnerHTML={{__html: allCheckins}}>
                </optgroup>
            </select>
            <TextBox label={"User ID "} onChange = {setUser}/>
            <AwesomeButton type="primary" onPress={() => {searchForUser()}}>Find User Data</AwesomeButton>
            {userData}
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