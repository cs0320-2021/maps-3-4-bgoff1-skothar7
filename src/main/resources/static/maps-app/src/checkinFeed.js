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
    const [userData, setUserData] = useState("h")
    let userDataBuilder = ""





    /**
     * Makes an axios request.
     */
    const getCheckins = () => {

        console.log("new checkin interval")
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
    setTimeout(getCheckins, 9000);
    //setInterval(getCheckins, 3000);

    // setTimeout(() => {
    //     getCheckins();
    //     console.log("new interval");
    // }, 3000);



    const searchForUser = () => {
        console.log("trying to search for user")

        //---------post request for selected user-------------
        const toSend2 = {
            id: user
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
            toSend2,
            config
        )
            .then(response2 => {
                //TODO: Go to the Main.java in the server from the stencil, and find what variable you should put here.
                //Note: It is very important that you understand how this is set up and why it works!
                if (response2 !== undefined) {
                    let rawUserData = response2.data["user"]
                    userDataBuilder = ""
                    for (let j = 0; j < rawUserData.length; j++) {
                        let currentUserCheckin = rawUserData[j].split(",")
                        userDataBuilder = userDataBuilder + "<option value =" + currentUserCheckin[0] + ">"+ currentUserCheckin[0] + ","+ currentUserCheckin[1]
                            + ", " + currentUserCheckin[2] + ", " + currentUserCheckin[3] + ", " + currentUserCheckin[4]
                            +"</option>"

                    }
                    console.log(userDataBuilder)
                    setUserData(userDataBuilder)
                }
            })

            .catch(function (error) {
                console.log(error);
                console.log(error.response.data);
            });

    }

    useEffect(() => {
        console.log("inside useEffect");
        for (let i=0; i < newCheckins.length; i++) {
            let currentCheckin = newCheckins[i].split(",")
            allCheckins = allCheckins + "<option value="+currentCheckin[0]+">"+currentCheckin[0] + ", " + currentCheckin[1]
                + ", " + currentCheckin[2] + ", " + currentCheckin[3] + ", " + currentCheckin[4]+"</option>"
        }


    })

    return (
        <div className="CheckinFeed">
            <select name="checkins" multiple size="10">
                <optgroup label="checkin-list" dangerouslySetInnerHTML={{__html: allCheckins}}>
                </optgroup>
            </select>

            <div className= "User feed">
                <select name="Usercheckins" multiple size="10">
                    <optgroup label="User checkin-list" dangerouslySetInnerHTML={{__html: userData}}>
                    </optgroup>
                </select>
                <TextBox label={"User ID "} onChange = {setUser}/>
                <AwesomeButton type="primary" onPress={() => {searchForUser()}}>Find User Data</AwesomeButton>
            </div>
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