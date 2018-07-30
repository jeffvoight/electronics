
<div class="copyspace">
    <h1>Scrolling Text</h1>

    <div class="saveMessages">
        ${responseMessages}
    </div>

    <form action="update" method="POST">
        <div class="featuredProject">
            <h3>text:</h3>
            <table>
                <% for (int i = 0; i < 10; i++) { %>
                <tr>
                    <td><input type="checkbox" name="active" checked="true"/></td>                
                    <td><input type="color" name="color" value="${ledMatrix.color}"/></td>                
                    <td><input type="text" name="text" value="${ledMatrix.scrollingText}"/></td>
                </tr>
                <% }%>
            </table>
            <input type="submit" class="settingsSaveButton" value="Save"/>
        </div>

    </form>

    <br class="clearingBreak">
</div>

