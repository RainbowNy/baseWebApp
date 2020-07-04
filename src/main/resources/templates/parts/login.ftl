<#macro login path isRegisterForm>
    <form action="${path}" method="post">
        <div class="form-group row">
            <label class="col-sm-2 col-form-label"> User Name : </label>
            <div class="col-sm-5">
                <input class="form-control ${(usernameError??)?string('is-invalid', '')}"
                       type="text"
                       name="username"
                       placeholder="User name"
                       value="<#if user??>${user.username}</#if>"/>

                <#if usernameError??>
                    <div class="invalid-feedback">
                        ${usernameError}
                    </div>
                </#if>
            </div>
        </div>

        <div class="form-group row">
            <label class="col-sm-2 col-form-label"> Password: </label>
            <div class="col-sm-5">
                <input class="form-control ${(passwordError??)?string('is-invalid', '')}"
                       type="password"
                       name="password"
                       placeholder="Password"/>

                <#if passwordError??>
                    <div class="invalid-feedback">
                        ${passwordError}
                    </div>
                </#if>
            </div>
        </div>

        <#if isRegisterForm>
            <div class="form-group row">
                <label class="col-sm-2 col-form-label">Repeat Password: </label>
                <div class="col-sm-5">
                    <input class="form-control ${(additionalPasswordError??)?string('is-invalid', '')}"
                           type="password"
                           name="additionalPassword"
                           placeholder="Type password again"/>

                    <#if additionalPasswordError??>
                        <div class="invalid-feedback">
                            ${additionalPasswordError}
                        </div>
                    </#if>
                </div>
            </div>

            <div class="form-group row">
                <label class="col-sm-2 col-form-label"> Email: </label>
                <div class="col-sm-5">
                    <input class="form-control ${(userEmailError??)?string('is-invalid', '')}"
                           type="email"
                           name="userEmail"
                           placeholder="example@example.nan"
                            value="<#if user??>${user.userEmail}</#if>"/>

                    <#if userEmailError??>
                        <div class="invalid-feedback">
                            ${userEmailError}
                        </div>
                    </#if>
                </div>
            </div>
        </#if>
        <input type="hidden" name="_csrf" value="${_csrf.token}"/>
        <#if !isRegisterForm>
            <a href="/registration">Sign up</a>
            <button class="btn btn-primary" type="submit">Sign In</button>
        <#else>
            <button class="btn btn-primary" type="submit">Sign Up</button>
        </#if>
    </form>
</#macro>

<#macro logout>
    <form action="/logout" method="post">
        <input type="hidden" name="_csrf" value="${_csrf.token}"/>
        <button class="btn btn-primary">Sign Out</button>
    </form>
</#macro>