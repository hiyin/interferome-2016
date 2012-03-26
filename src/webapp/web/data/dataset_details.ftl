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
    <#include "../template/action_errors.ftl">
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

        <div class="data_outer_div">
            <br/>
           <div class="ds_label_title"> Dataset: </div> <div class="exp_name_title"><@s.property value="dataset.name" /></div>
            <br/>
        </div>
        <div class="data_table_div">
            <table class="data_table">
                <thead>
                    <tr class="dt_header">
                        <td>Experiment Factors</td>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td>
                            <div class="ds_data_label"> Interferon Type </div><div class="ds_data_value"><@s.property value="%{dataset.ifnType.typeName}" /></div>
                            <div class="ds_data_label"> Interferon SubType </div><div class="ds_data_value"><@s.property value="%{dataset.ifnType.subTypeName}" /></div>
                            <div class="ds_data_label">
                                In Vivo / In Vitro
                            </div>
                            <div class="ds_data_value">
                                <@s.if test="%{dataset.inVivo == true}">
                                    In Vivo
                                </@s.if>
                                <@s.else>
                                    In Vitro
                                </@s.else>
                            </div>
                            <div class="ds_data_label">
                                Normal / Abnormal
                            </div>
                            <div class="ds_data_value">
                                <@s.if test="%{dataset.ifnVar.abnormal == true}">
                                    Abnormal
                                </@s.if>
                                <@s.else>
                                    Normal
                                </@s.else>
                            </div>
                            <@s.if test="%{dataset.ifnVar.abnormal == true}">
                            <div class="ds_data_label">
                                Abnormal
                            </div>
                            <div class="ds_data_value">
                                <@s.property value="%{dataset.ifnVar.value}" />
                            </div>
                            </@s.if>
                            <div class="ds_data_label"> Treatment Concentration </div><div class="ds_data_value"><@s.property value="%{dataset.treatmentCon}" /> (IU/ml)</div>
                            <div class="ds_data_label"> Treatment Time </div><div class="ds_data_value"><@s.property value="%{dataset.treatmentTime}" /> (hr)</div>
                            <@s.iterator status="fv" value="nameValueBeans" id="nvb" >
                                <div class="ds_data_label"> <@s.property value="%{#nvb.name}" /> </div><div class="ds_data_value"><@s.property value="%{#nvb.value}" /> </div>
                            </@s.iterator>
                            <div class="blank_separator"></div>
                            <div class="ds_data_label"> Description </div><div class="ds_field_desc"><@s.property value="%{dataset.description}" /> </div>
                            <div class="blank_separator"></div>
                            <div class="ds_data_label"> Comment </div><div class="ds_field_desc"><@s.property value="%{dataset.comment}" /> </div>
                            <div class="blank_separator"></div>
                            <div class="ds_data_label"> Sample Characteristic </div><div class="ds_field_desc"><@s.property value="%{dataset.sampleChars}" /> </div>
                        </td>

                    </tr>

                </tbody>
            </table>
        </div>
        <#include "../data/ds_data_page.ftl">
        <div style="clear:both"></div>
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