<#assign s=JspTaglibs["/WEB-INF/struts-tags.tld"] />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title><@s.text name="interferome.submitdata.action.title" /></title>
<#include "../template/header.ftl"/>
<script type="text/javascript">
function refresh()
{
	document.getElementById("imagevalue").src='${base}/security/securityCode.jspx?now=' + new Date();
}
</script>
</head>
<body>
	<#include "../template/navsection.ftl"/>
	<div class="nav_namebar_div nav_title_gray">
		<a href="${base}/site/showSubmitData.jspx"><@s.text name="interferome.submitdata.action.title" /></a>
	</div>
	<div style="clear:both"></div>
 	<div class="main_container">
 		<#include "../template/action_errors.ftl">

		<div class="container_inner_left">
	        <div class="blank_separator"></div>
            <@s.if test="%{successActMsg != null}">
            <div class="blank_separator"></div>
            <#include "../template/success_act_message.ftl"/>
            </@s.if>
            <div class="data_outer_div">

                <br/>
                <div class="contactus_data_title">
                    Interferome Microarray Data Submission
                </div>

                <div class="contactus_data_textarea">
                    To improve the quality of the data provided through the Interferome we rely on contributions from the scientific community. If you have
                    Interferon treated datasets in <a href='http://www.mged.org/Workgroups/MIAME/miame.html'>MIAMI compliant form</a> can be submitted to the
                    administrators for review prior to inclusion in the Interferome database.
                </div>
                <div class="blank_separator"></div>

                <br/>
            </div>
            <div class="parag_title_div">
				Data Submission
		    </div>
            <@s.form action="submitData.jspx" namespace="/site" method="post">
			<div class="data_outer_div">
                <div class="blank_separator"></div>

                <div class="data_label_div">
                    Your Name:
                </div>
                <div class="data_value_div">
                    <@s.textfield name="contactName" cssClass="input_field_normal"/>  * (user name required)
                </div>
                <div class="data_label_div">
                    Your Email
                </div>
                <div class="data_value_div">
                    <@s.textfield name="contactEmail" cssClass="input_field_normal"/>  * (user email required)
                </div>
                <div class="data_label_div">
                    Your Phone
                </div>
                <div class="data_value_div">
                    <@s.textfield name="contactPhone" cssClass="input_field_normal"/>  (optional)
                </div>

                <div class="data_label_div">
                    Experiment Title:
                </div>
                <div class="data_value_div">
                    <@s.textfield name="subject" cssClass="input_field_normal"/>  * (subject required)
                </div>

                <div class="data_label_div">
					Experiment Description:
			    </div>
				<div class="data_value_div">
					<@s.textarea  name="message" cols="70" rows="8" cssClass="input_textarea" /> * (contact messages required)
				</div>

                <div class="data_label_div">
                    File Upload
                </div>
                <div class="data_value_div">
                    <@s.file name="upload" cssClass="file_upload"/>
                </div>



                    <div class="data_label_div">Word Verification: </div>
                <div class="data_value_div">
                    <@s.textfield name="securityCode" cssClass="input_field_normal" />  * (<@s.text name="security.code.spec" />)
                </div>
                <div class="blank_separator"></div>
                <div class="data_label_div">&nbsp;</div>
                <div class="security_code">
                    <img src="${base}/security/securityCode.jspx?now=new Date()" border="0" id="imagevalue" name="imagevalue" />
                    &nbsp; <a href="#" onclick="refresh()"><img src="${base}/images/refresh.png" class="security_code_img" /> can't read this?</a>
 			    </div>

                <div style="clear:both"></div>
			</div>

            <div class="data_outer_noborder_div">
                <div class="data_label_div">&nbsp;</div>
                <div class="data_value_div">
			        <@s.submit value="Submit" cssClass="input_button" /> &nbsp; <@s.reset value="Clear" cssClass="input_button" />
                </div>
			</div>
            </@s.form>
            <div class="blank_separator"></div>
		</div>
        <@s.if test="%{#session.authentication_flag =='authenticated'}">
		<div class="container_inner_right">
			<#include "../template/user_nav.ftl">
		</div>
        </@s.if>
		<div style="clear:both"></div>
 	</div>
	<#include "../template/footer.ftl"/>
</body>
</html>
