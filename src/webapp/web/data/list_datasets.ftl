<#assign s=JspTaglibs["/WEB-INF/struts-tags.tld"] />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>${pageTitle}</title>
<#include "../template/jquery_header.ftl"/>
<script type="text/javascript">
function doAfterImport(success) {
    if(success) {
		setTimeout('window.location.href = "${base}/data/listDatasets.jspx?experiment.id=<@s.property value='experiment.id' />&fromMyExp=<@s.property value='fromMyExp' />"', 3000);
    }
};
</script>
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
                <div class="exp_name_title">${namePrefix}<@s.property value="experiment.id" /></div>
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
            Datasets
        </div>
        <@s.if test="%{permissionBean.importAllowed == true}">
        <!-- File import message block -->
        <div class="file_success_message">
            <div class="success_message_item">
               <ul>
                   <li><p id="fsuccess_msg">ok</p></li>
               </ul>
            </div>
        </div>

        <div class="file_error_message">
            <div class="error_message_item">
                <ul>
                    <li><p id="ferror_msg">error</p></li>
                </ul>
            </div>
        </div>
        <!-- End of File importing message -->

        <!-- Ajax File importing -->
        <div class="data_outer_div">
            <@s.form action="importDataset.jspx" id="datasetImport" namespace="/data" method="post" enctype="multipart/form-data" >
                <div id='uploading_info' >
                    Importing the dataset, please wait ... <img src="${base}/images/wait_loader.gif" class="loading_image">
                </div>
                <div class="input_field_div">Choose a dataset file: </div>
                <div class="input_field_div">
                    <@s.file name="upload" id="datasetFile" cssClass="file_upload"/>
                    <@s.hidden name="experiment.id" />
                </div>
                <div class="input_field_comment">
                    * (Please choose the BASEFile exporter plugin in BASE to export the dataset in a txt file. Only the txt format is supported)
                </div>

                <div class="blank_separator"></div>
                <@s.submit value="Import" cssClass="input_button" />
            </@s.form>
            <br/>
        </div>
        <!-- End of File importing -->
        </@s.if>
        <div class="data_table_div">
            <span class="name_title">
                A total of <font color="green">
                <@s.if test = "%{datasetFactorBeans != null}">
                    <@s.property value="datasetFactorBeans.size" /></@s.if>
                <@s.else>0</@s.else>
                </font> Datasets
            </span>
            <div class="blank_separator"></div>
            <div class="blank_separator"></div>
            <@s.if test = "%{datasetFactorBeans != null && datasetFactorBeans.size >0}">
            <div class="data_comment">Click the dataset name to view the dataset details</div>
            <br/>

            <table class="data_table">
                <thead>
                    <tr class="dt_header">
                        <td width="260">Dataset Name</td>
                        <td>Experiment Factor Value</td>
                        <td width="60">&nbsp;</td>
                    </tr>
                </thead>
                <tbody>
                    <@s.iterator status="dsState" value="datasetFactorBeans" id="dsfvbean" >
                    <tr>
                        <td align="left"><div class="ds_link"><a href="${base}/data/viewDataset?experiment.id=<@s.property value='experiment.id' />&dataset.id=<@s.property value='#dsfvbean.dataset.id' />&fromMyExp=<@s.property value='fromMyExp' />"><@s.property value="%{#dsfvbean.dataset.name}" /></a></div></td>
                        <td align="left">
                            <div class="ds_data_label"> Interferon Type </div><div class="ds_data_value"><@s.property value="%{#dsfvbean.dataset.ifnType.typeName}" /></div>
                            <div class="ds_data_label"> Interferon SubType </div><div class="ds_data_value"><@s.property value="%{#dsfvbean.dataset.ifnType.subTypeName}" /></div>
                            <div class="ds_data_label">
                                In Vivo / In Vitro
                            </div>
                            <div class="ds_data_value">
                                <@s.if test="%{#dsfvbean.dataset.inVivo == true}">
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
                                <@s.if test="%{#dsfvbean.dataset.ifnVar.abnormal == true}">
                                    Abnormal
                                </@s.if>
                                <@s.else>
                                    Normal
                                </@s.else>
                            </div>
                            <@s.if test="%{#dsfvbean.dataset.ifnVar.abnormal == true}">
                            <div class="ds_data_label">
                                Abnormal
                            </div>
                            <div class="ds_data_value">
                                <@s.property value="%{#dsfvbean.dataset.ifnVar.value}" />
                            </div>
                            </@s.if>
                            <div class="ds_data_label"> Treatment Concentration </div><div class="ds_data_value"><@s.property value="%{#dsfvbean.dataset.treatmentCon}" /> (IU/ml)</div>
                            <div class="ds_data_label"> Treatment Time </div><div class="ds_data_value"><@s.property value="%{#dsfvbean.dataset.treatmentTime}" /> (hr)</div>
                            <@s.iterator status="fv" value="%{#dsfvbean.nameValueBeans}" id="nvb" >
                                <div class="ds_data_label"> <@s.property value="%{#nvb.name}" /> </div><div class="ds_data_value"><@s.property value="%{#nvb.value}" /> </div>
                            </@s.iterator>
                            <div class="blank_separator"></div>
                            <div class="ds_data_label"> Description </div><div class="ds_field_desc"><@s.property value="%{#dsfvbean.dataset.description}" /> </div>
                            <div class="blank_separator"></div>
                            <div class="ds_data_label"> Comment </div><div class="ds_field_desc"><@s.property value="%{#dsfvbean.dataset.comment}" /> </div>
                            <div class="blank_separator"></div>
                            <div class="ds_data_label"> Sample Characteristic </div><div class="ds_field_desc"><@s.property value="%{#dsfvbean.dataset.sampleChars}" /> </div>
                        </td>
                        <td>
                            <@s.if test="%{permissionBean.deleteAllowed == true}">
                            <div class="data_link_small">
                                <div id='confirm-dialog'>
                                <div class="msg_content">The data will be removed from the repository permanently!<p>Are you sure to delet this dataset?</p></div>
                                <div id='confirm'>
                                    <div class='header'><span>Deleting Dataset Confirm</span></div>
                                    <div class='message'></div>
                                    <div class='buttons'>
                                        <div class='no simplemodal-close'>No</div>
                                        <div class='yes'>Yes</div>
                                    </div>
                                </div>
                                <a href='${base}/data/deleteDataset.jspx?experiment.id=<@s.property value='experiment.id' />&dataset.id=<@s.property value='#dsfvbean.dataset.id' />&fromMyExp=<@s.property value="fromMyExp" />' class='confirm' >Delete</a>
                                </div>
                            </div>
                            </@s.if>
                        </td>
                    </tr>
                   </@s.iterator>
                </tbody>
            </table>
            </@s.if>
        </div>
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