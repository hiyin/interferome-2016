<#include "use_policy.ftl" />
<#include "ands.ftl" />
<div style="clear:both"></div>
<br/>
<div class="sponsor">
    <a href="http://www.monash.edu/eresearch" target="_blank"><img src="${base}/images/logo/logo_merc.png"
                                                                   alt="Monash MeRC"/></a>&nbsp;&nbsp;&nbsp;&nbsp;
    <a href="http://www.monashinstitute.org/" target="_blank"><img src="${base}/images/logo/logo_mimr.png"
                                                                   alt="Monash Institute of Medical Research"/></a>&nbsp;&nbsp;&nbsp;&nbsp;
    <a href="http://www.microbialgenomics.net/" target="_blank"><img src="${base}/images/logo/logo_arc.png"
                                                                     alt="Australian Research Council (ARC)"/></a>&nbsp;&nbsp;&nbsp;&nbsp;
    <a href=""><img src="${base}/images/logo/logo_ciiid.png"
                    alt="Centre for Innate Immunity and Infectious Disease"/></a> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
</div>
<div class="blank_separator"></div>
<div class="footer">
    <br/>

    <div class="login_section">
    <@s.if test="%{#session.authentication_flag == null}">
        <div class="login">
            <a href="${base}/user/showLogin.jspx">Login</a>
        </div>
        <div class="login">
            <a href="${base}/user/register_options">Register</a>
        </div>
    </@s.if>
    </div>
    <div class="copyright">
        Copyright &copy; 2010-2012 Monash University. All Rights Reserved.
        &nbsp;&nbsp;&nbsp;&nbsp;<b><@s.text name="app.version" /></b> <br/>
    </div>
    <br/>
</div>
<br/>