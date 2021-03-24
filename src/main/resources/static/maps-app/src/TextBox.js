import logo from './logo.svg';
import './App.css';

function TextBox(props) {
  return (
    <div className="TextBox">
      {props.label}
      <input  type="text" label={props.label} onChange={(e) => props.onChange(e.target.value)}/>
    </div>
  );
}

export default TextBox;
