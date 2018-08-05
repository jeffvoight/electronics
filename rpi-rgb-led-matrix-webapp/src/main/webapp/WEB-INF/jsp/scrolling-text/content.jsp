<div class="copyspace">
    <h1>Scrolling Text</h1>

    <div class="saveMessages">
        ${responseMessages}
    </div>

    <form action="update" method="POST">
        <div class="featuredProject">
            <h3>text:</h3>
            <table>
                <c:forEach var="item" items="${scrollItems}">
                <tr>
                    <td><input type="checkbox" name="active" checked="${item.active}"/></td>                
                    <td><input type="color" name="color" value="${item.color}"/></td>                
                    <td><input type="text" name="text" value="${item.text}"/></td>
                </tr>
                </c:forEach>
                <tr>
                    <td><input type="checkbox" name="active" checked="off"/></td>                
                    <td><input type="color" name="color" value="#ffffff"/></td>                
                    <td><input type="text" name="text" value=""/></td>
                </tr>
            </table>
            <input type="submit" class="settingsSaveButton" value="Save"/>
        </div>

    </form>

    <br class="clearingBreak">
</div>

