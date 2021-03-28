import './App.css';

/**
 *
 * @param props allows access to relevant data and functions from the caller
 * @returns {JSX.Element}
 * @constructor for TextBox
 */
function TextBox(props) {
  return (
    <div className="TextBox">
      {props.label}
        {/*if textbox text changes, call the on change function in props*/}
      <input  type="text" label={props.label} onChange={(e) => props.onChange(e.target.value)}/>
    </div>
  );
}

export default TextBox;
