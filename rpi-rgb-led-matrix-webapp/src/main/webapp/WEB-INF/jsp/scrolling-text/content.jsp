
    <div class="copyspace">
        <h1>Scrolling Text</h1>
        
        <div class="saveMessages">
            ${responseMessages}
        </div>
        
        <form action="update" method="POST">
        <div class="featuredProject">
            <h3>text:</h3>
            
            <p>
                <input type="text" name="red" />
                <input type="text" name="green" />
                <input type="text" name="blue" />
                <textarea rows="4" cols="50" name="text" >${ledMatrix.scrollingText}</textarea>
            </p>
            
            <input type="submit" class="settingsSaveButton" value="Save"/>
        </div>
            
        </form>
            
        <br class="clearingBreak">
    </div>

