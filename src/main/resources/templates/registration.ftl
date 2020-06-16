<#import "parts/common.ftl" as c>
<#import "parts/login.ftl" as l>
<@c.page>
Add new user
    <div>
        <#if message??>
            ${message}
        </#if>
    </div>
<@l.login "/registration"/>
</@c.page>