import logo from './logo.svg';
import './App.css';

function CheckinFeed(props) {
    return (
        <div className="CheckinFeed">
            {props.label}
            <input  type="text" label={props.label} onChange={(e) => props.onChange(parseFloat(e.target.value))}/>
        </div>
    );
}

export default CheckinFeed;
