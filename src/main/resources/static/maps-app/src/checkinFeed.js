import './App.css';
import axios from "axios";
import React, {useState, useEffect} from 'react'
import {AwesomeButton} from "react-awesome-button";
import TextBox from "./TextBox";

//global reference to HTML String of all new checkins, which is built inside useEffect()
let allCheckins = ""

//global reference to User ID of selected user
let user = "";

//function to set a the user global reference from the text box
function setUser(u) {
    user = u
}

/**
 * function that creates two Select tables: one with all checkins and another with a specific user's checkins
 * @returns {JSX.Element}
 * @constructor for CheckinFeed, which is called in App.js
 */
function CheckinFeed() {

    const [newCheckins, setNewCheckins] = useState([])
    const [userData, setUserData] = useState("")
    //userDataBuilder builds a HTML String of checkin records from the post request
    let userDataBuilder = ""


    /**
     * Makes an axios request every 3 seconds to update our list of Checkins.
     */
    const getCheckins = () => {

        console.log("new checkin interval")
        const toSend = {
            dummy: 42,
        };

        let config = {
            headers: {
                "Content-Type": "application/json",
                'Access-Control-Allow-Origin': '*',
            }
        }

        axios.post(
            //alerts the backend that we want to retrieve all new checkins since the last post
            "http://localhost:4567/checkin",
            toSend,
            config
        )
            .then(response => {

                if (response !== undefined) {
                    setNewCheckins(response.data["checkins"]);
                }
            })

            .catch(function (error) {
                console.log(error);
            });

    }

    /**
     * Searches for a specific user to display all checkins for that user
     */
    const searchForUser = () => {
        console.log("trying to search for user")

        //---------post request for selected user-------------
        const toSend2 = {
            //sends the user ID typed into the GUI to the backend
            id: user
        };

        let config = {
            headers: {
                "Content-Type": "application/json",
                'Access-Control-Allow-Origin': '*',
            }
        }
        axios.post(
            //post request to retrieve all user checkins for user with given ID
            "http://localhost:4567/user",
            toSend2,
            config
        )
            .then(response2 => {
                if (response2 !== undefined) {
                    let rawUserData = response2.data["user"]
                    userDataBuilder = ""
                    for (let j = 0; j < rawUserData.length; j++) {
                        //loops through all checkins received of a specific user and iteratively builds HTML options
                        let currentUserCheckin = rawUserData[j].split(",")
                        userDataBuilder = userDataBuilder + "<option value =" + currentUserCheckin[0] + ">" +
                            currentUserCheckin[0] + "," + currentUserCheckin[1] + ", " + currentUserCheckin[2] + ", " +
                            currentUserCheckin[3] + ", " + currentUserCheckin[4]
                            + "</option>"

                    }
                    console.log(userDataBuilder)
                    //hook: sets userDataBuilder so that upon return, we are displaying selected clicked user's data
                    setUserData(userDataBuilder)
                }
            })

            .catch(function (error) {
                console.log(error);
                console.log(error.response.data);
            });

    }

    useEffect(() => {
        //runs getCheckins every 3 seconds
        setTimeout(getCheckins, 3000);
        console.log("inside useEffect");
        for (let i = 0; i < newCheckins.length; i++) {
            let currentCheckin = newCheckins[i].split(",")
            //loops through all new checkins received since the last post request and iteratively builds HTML options
            allCheckins = allCheckins + "<option value=" + currentCheckin[0] + ">" + currentCheckin[0] + ", " +
                currentCheckin[1] + ", " + currentCheckin[2] + ", " + currentCheckin[3] + ", " + currentCheckin[4] +
                "</option>"
        }


    })

    return (
        <div className="CheckinFeed">
            <select name="checkins" multiple size="10">
                {/*uses allCheckins HTML String to create option elements within the optgroup tags*/}
                <optgroup label="checkin-list" dangerouslySetInnerHTML={{__html: allCheckins}}>
                </optgroup>
            </select>

            <div className="User feed">
                <select name="Usercheckins" multiple size="10">
                    {/*uses userData HTML String to create option elements within the optgroup tags*/}
                    <optgroup label="User checkin-list" dangerouslySetInnerHTML={{__html: userData}}>
                    </optgroup>
                </select>
                {/*Button and textbox input for retrieving checkin data for a specific user*/}
                <TextBox label={"User ID "} onChange={setUser}/>
                {/*buttin submits entered user ID to post request*/}
                <AwesomeButton type="primary" onPress={() => {
                    searchForUser()
                }}>Find User Data</AwesomeButton>
            </div>
        </div>

    );
}

export default CheckinFeed;