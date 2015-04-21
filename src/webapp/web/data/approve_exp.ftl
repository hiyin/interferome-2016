<#assign s=JspTaglibs["/WEB-INF/struts-tags.tld"] />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>${pageTitle}</title>
<#include "../template/jquery_header.ftl"/>
</head>
<body>
<#include "../template/navsection.ftl"/>
<#include "../template/action_nav_bar.ftl"/>
<div class="main_container">
    <div class="container_inner_left">
        <div class="blank_separator"></div>
    <@s.if test="%{successActMsg != null}">
        <div class="data_header_div">
            <#include "../template/success_act_message.ftl"/>
        </div>
    </@s.if>

        <div class="data_outer_div">
            <div class="each_field_row">
                <div class="exp_name_title">${namePrefix}<@s.property value='experiment.id' /></div>
            </div>
            <div class="each_field_row">
                <div class="exp_desc_div"><@s.property value="experiment.description" /></div>
            </div>

            <div class="each_field_row">
                <div class="exp_audit_info">
                    Imported by <@s.property value="experiment.owner.displayName" />,&nbsp;&nbsp;
                    Imported Date: <@s.date name="experiment.createdTime"  format="dd-MM-yyyy 'at' hh:mm a" />,&nbsp;&nbsp;
                    Modified by <@s.property value="experiment.modifiedByUser.displayName" />,&nbsp;&nbsp;
                    Modified date: <@s.date name="experiment.modifiedTime" format="dd-MM-yyyy 'at' hh:mm a"  />
                </div>
            </div>

            <div class="each_field_row">
                <div class="approve_status">Approved: <@s.property value="experiment.approved" /></div>
            </div>

            <div class="data_link">
                <a href="${base}/${viewExpActName}?experiment.id=<@s.property value='experiment.id' />">View Details</a>
            </div>
            <div style="clear:both"></div>
        </div>
        <div class="parag_title_div">
            Experiment Approval & Public Registration
        </div>
        <@s.form action="approveExp.jspx" namespace="/data" method="post">
        <@s.hidden name="experiment.id" />
        <@s.hidden name="fromMyExp" />

        <div class="data_outer_div">
            <div class="blank_separator"></div>

            <div class="each_field_row">
                * To approve an Experiment and make it as publicly available, please click the 'Approve' Button;
            </div>

            <div class="each_field_row">
                * To include the public registration, please select 'Public Registration' tick box:
            </div>

            <div class="each_field_row">
            <@s.checkbox name="mdRegSelected"  cssClass = "cboxfield"/> Public Registration
                <span class="comment_gray"> (Register the metadata associated with this experiment with the Research Data Australia website)</span>
            </div>
            <div class="blank_separator"></div>
            <div class="blank_separator"></div>
            <div style="clear:both"></div>
        </div>
        <div class="data_header_div">
            <@s.submit value="Approve" cssClass="input_button" id="wait_modal" name='wait_modal' />

            <div id='mask'></div>
			<div id='modal_window' >
			    Approving the experiment, please wait ... <img src="${base}/images/wait_loader.gif" class="loading_image">
			</div>
        </div>
        </@s.form>

        <div class="empty_div"></div>
        <br/>
    </div>
    <div class="container_inner_right">
    <#include "../template/user_nav.ftl">
    </div>
    <div style="clear:both"></div>
</div>
<#include "../template/footer.ftl"/>
</body>
</html>