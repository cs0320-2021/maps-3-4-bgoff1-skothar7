<#assign content>

<div class="main-container">
    <div class="commands-container">
        <div class="commands">
            <form method="POST" id="command-form" action="/input">
                <input type="submit" id="naive-neighbors" name="naive-neighbors" value="Naive Neighbors"><br>
                <input type="submit" id="neighbors" name="neighbors" value="Neighbors"><br>
                <input type="submit" id="naive-radius" name="naive-radius" value="Naive Radius"><br>
                <input type="submit" id="radius" name="radius" value="Radius"><br>
            </form>
        </div>
    </div>

    <div class="command-input-container">
        <div class="command-input">
            <form method="POST" id="input-form" action="/results" onsubmit="return checkNumericFormFields();">
                <label for = "num" id="numTitle">Number of neighbors/radius to search for: </label><br>
                <input type="number" id = "num" name="neighbors-or-radius" min="0"><br>
                <label for = "star-name">Star Name: </label><br>
                <input type="text" name="star-name" id = "star-name"><br>
                <p> OR </p>
                <label for = "x">X: </label>
                <input type="text" name = "x" id = "x">
                <label for = "y">Y: </label>
                <input type="text" name = "y" id = "y">
                <label for = "z">Z: </label>
                <input type="text" name = "z" id = "z"><br>
                <input type="submit" value="SEARCH" id="submit-input", disabled="disabled">
            </form>
        </div>
    </div>
</div>

</#assign>
<#include "main.ftl">