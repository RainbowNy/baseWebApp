<#assign
    session = Session.SPRING_SECURITY_CONTEXT??
>

<#if session>
    <#assign
        user = Session.SPRING_SECURITY_CONTEXT.authentication.principal
        username = user.getUsername()
        isAdmin = user.isAdmin()
    >
    <#else>
    <#assign
        username = "unknown"
        isAdmin = false
    >
</#if>