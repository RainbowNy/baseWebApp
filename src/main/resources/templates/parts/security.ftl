<#assign
    session = Session.SPRING_SECURITY_CONTEXT??
>

<#if session>
    <#assign
        user = Session.SPRING_SECURITY_CONTEXT.authentication.principal
        username = user.getUsername()
        isAdmin = user.isAdmin()
        currentUserId = user.getId()
    >
    <#else>
    <#assign
        username = "unknown"
        isAdmin = false
        currentUserId = -1
    >
</#if>