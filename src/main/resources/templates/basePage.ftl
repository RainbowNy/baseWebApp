<#import "parts/common.ftl" as c>
<#import "parts/login.ftl" as l>
<@c.page>
    <div>
        <@l.logout />
        <span><a href="/user">User list</a></span>
    </div>
    <div>
        <form method="post">
            <input type="hidden" name="_csrf" value="${_csrf.token}"/>
            <input type="text" name="textToSent" placeholder="Type the message">
            <input type="text" name="tag" placeholder="Tag">
            <button type="submit">Add message</button>
        </form>
    </div>

    <div>Список сообщений</div>
    <form method="get" action="/main">
        <input type="text" name="filterName" value="${filter!""}">
        <button type="submit">Find</button>
    </form>
    <#list messages as message>
        <div>
            <b>${message.getId()}</b>
            <span>${message.getText()}</span>
            <i>${message.getTag()}</i>
            <strong>${message.getAuthorName()}</strong>
        </div>
        <#else>
            No messages
    </#list>
</@c.page>