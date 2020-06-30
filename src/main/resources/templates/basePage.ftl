<#import "parts/common.ftl" as c>

<@c.page>
    <div class="form-row">
        <div class="form-group col-md-6">
            <form class="form-inline" method="get" action="/main">
                <input type="text" name="filterName" class="form-control" value="${filter!""}" placeholder="Search by tag">
                <button type="submit" class="btn btn-primary ml-2">Search</button>
            </form>
        </div>
    </div>

    <a class="btn btn-primary" data-toggle="collapse" href="#addmessage" role="button" aria-expanded="false" aria-controls="collapseExample">
        Add new message
    </a>
    <div class="collapse" id="addmessage">
        <div class="form-group mt-3">
            <form method="post" enctype="multipart/form-data">
                <input type="hidden" name="_csrf" value="${_csrf.token}"/>
                <div class="form-group">
                    <input class="form-control" type="text" name="textToSent" placeholder="Type the message">
                </div>

                <div class="form-group">
                    <input class="form-control" type="text" name="tag" placeholder="Tag">
                </div>

                <div class="form-group">
                    <div class="custom-file">
                        <input type="file" name="file" id="file">
                        <label class="custom-file-label" for="file">Choose file</label>
                    </div>
                </div>

                <div class="form-group">
                    <button class="btn btn-primary" type="submit">Add message</button>
                </div>
            </form>
        </div>
    </div>

    <div class="card-columns">
    <#list messages as message>
        <div class="card my-3">
            <#if message.filename??>
                <img class="card-img-top" src="/img/${message.filename}">
            </#if>

            <div class="m-2">
                <span>${message.getText()}</span>
                <i>${message.getTag()}</i>
            </div>

            <div class="card-footer text-muted">
                ${message.getAuthorName()}
            </div>
        </div>
    <#else>
        No messages
    </#list>
    </div>
</@c.page>