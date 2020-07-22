<a class="btn btn-primary" data-toggle="collapse" href="#addmessage" role="button" aria-expanded="false" aria-controls="collapseExample">
    Edit messages
</a>
<div class="collapse <#if message??>show</#if>" id="addmessage">
    <div class="form-group mt-3">
        <form method="post" enctype="multipart/form-data">
            <input type="hidden" name="id" value="<#if message??>${message.id}</#if>"/>
            <input type="hidden" name="_csrf" value="${_csrf.token}"/>

            <div class="form-group">
                <input class="form-control ${(textError??)?string('is-invalid', '')}" value="<#if message??>${message.text}</#if>" type="text" name="text" placeholder="Type the message">

                <#if textError??>
                    <div class="invalid-feedback">
                        ${textError}
                    </div>
                </#if>
            </div>

            <div class="form-group">
                <input class="form-control ${(tagError??)?string('is-invalid', '')}" value="<#if message??>${message.tag}</#if>" type="text" name="tag" placeholder="Tag">

                <#if tagError??>
                    <div class="invalid-feedback">
                        ${tagError}
                    </div>
                </#if>
            </div>

            <div class="form-group">
                <div class="custom-file">
                    <input type="file" name="file" id="file">
                    <label class="custom-file-label" for="file">Choose file</label>
                </div>
            </div>

            <div class="form-group">
                <button class="btn btn-primary" type="submit">Save message</button>
            </div>
        </form>
    </div>
</div>