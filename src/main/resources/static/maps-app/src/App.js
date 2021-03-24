import logo from './logo.svg';
import './App.css';
import Route from './Route'
import CheckinFeed from "./checkinFeed";

function App() {
  return (
    <div className="App">
      <header className="App-header">
        <Route/>
        <CheckinFeed/>
        <img src={logo} className="App-logo" alt="logo" />
      </header>
    </div>
  );
}

export default App;
