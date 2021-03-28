import logo from './logo.png';
import './App.css';
import Route from './Route'
import CheckinFeed from "./checkinFeed";

/**
 * Main display for the GUI.
 * @returns {JSX.Element}
 * @constructor for the main App GUI
 */
function App() {
  return (
    <div className="App">
      <header className="App-header">
        {/*Map display and input text boxes at top of page*/}
        <Route/>
        {/*Feed of server checkins and search box at bottom of page*/}
        <CheckinFeed/>

        {/*Spinning tim face*/}
        <img src={logo} className="App-logo" alt="logo" />
        <p>

        </p>
      </header>
    </div>
  );
}

export default App;
