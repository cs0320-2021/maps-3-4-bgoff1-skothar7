import logo from './logo.png';
import './App.css';
import Route from './Route'
import CheckinFeed from "./checkinFeed";

function App() {
  return (
    <div className="App">
      <header className="App-header">
        <Route/>
        <CheckinFeed/>
          <br>

          </br>
        <img src={logo} className="App-logo" alt="logo" />
        <p>

        </p>
      </header>
    </div>
  );
}

export default App;
