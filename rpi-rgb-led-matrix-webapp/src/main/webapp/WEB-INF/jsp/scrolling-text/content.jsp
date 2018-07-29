
    <div class="copyspace">
        <h1>Scrolling Text</h1>
        
        <div class="saveMessages">
            ${responseMessages}
        </div>
        
        <form action="update" method="POST">
        <div class="featuredProject">
            <h3>text:</h3>
            <table>
                
            </table>
            <tr>
                <td><input type="text" name="red" value="${ledMatrix.red}"/></td>
                <td><input type="text" name="green" value="${ledMatrix.green}"/></td>
                <td><input type="text" name="blue" value="${ledMatrix.blue}"/></td>
                <td><input type="text" name="text" value="${ledMatrix.scrollingText}"/></td>
            </tr>
        </table>
            <input type="submit" class="settingsSaveButton" value="Save"/>
        </div>
            
        </form>
            
        <br class="clearingBreak">
    </div>

