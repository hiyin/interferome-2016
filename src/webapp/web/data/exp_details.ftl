<#assign s=JspTaglibs["/WEB-INF/struts-tags.tld"] />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
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
			<div class="parag_title_div">
					Experiment Details
			</div>
			<div class="data_outer_div">
				<div class="each_field_row">
					<div class="data_field_div">Name:</div>
					<div class="data_field_title">${namePrefix}<@s.property value="experiment.id" /></div>
					<div style="clear:both"></div> 
				</div>

                <div class="each_field_row">
					<div class="data_field_div">Reference:</div>
					<div class="data_field_title">
                        <@s.if test="%{referenceLink != null }">
                            <a href="${referenceLink}${experiment.name}" target="_blank" ><@s.property value="experiment.name" /> </a>
                        </@s.if>
                        <@s.else>
                            <@s.property value="experiment.name" />
                        </@s.else>
					</div>
					<div style="clear:both"></div>
				</div>

				<div class="each_field_row">
					<div class="data_field_div">Base Registered Date:</div>
					<div class="data_field_date"><@s.date name="experiment.entryDate"  format="yyyy-MM-dd" /> </div>
					<div style="clear:both"></div> 
				</div>
				<div class="each_field_row">
					<div class="data_field_div">Base Owner:</div>
					<div class="data_field_value"><@s.property value="experiment.baseOwner" /></div>
					<div style="clear:both"></div> 
				</div>
				 
				<div class="each_field_row">
					<div class="data_field_div">Description:</div>
					<div class="data_field_desc"><@s.property value="experiment.description" escape=false /></div>
					<div style="clear:both"></div> 
				</div>
				
				<div class="each_field_row">
					<div class="data_field_div">Approved:</div>
					<div class="data_field_app"><@s.property value="experiment.approved" /></div>
					<div style="clear:both"></div> 
				</div>

                <div class="each_field_row">
					<div class="data_field_div">Metadata Registered:</div>
					<div class="data_field_app"><@s.property value="experiment.mdPublished" /></div>
					<div style="clear:both"></div>
				</div>
				
				<div class="each_field_row">
					<div class="data_field_div">Imported By:</div>
					<div class="data_field_value"><@s.property value="experiment.owner.displayName" /></div>
					<div style="clear:both"></div> 
				</div>
				
				<div class="each_field_row">
					<div class="data_field_div">Imported Date:</div>
					<div class="data_field_date"><@s.date name="experiment.createdTime"  format="yyyy-MM-dd 'at' hh:mm a" /></div>
					<div style="clear:both"></div> 
				</div>
				
				<div class="each_field_row">
					<div class="data_field_div">Modified By:</div>
					<div class="data_field_value"><@s.property value="experiment.modifiedByUser.displayName" /></div>
					<div style="clear:both"></div> 
				</div>
				
				<div class="each_field_row">
					<div class="data_field_div">Modified Date:</div>
					<div class="data_field_date"><@s.date name="experiment.modifiedTime"  format="yyyy-MM-dd 'at' hh:mm a" /></div>
					<div style="clear:both"></div> 
				</div>
			</div>
			<div class="parag_title_div">
				Publication
			</div>
			
			<div class="data_outer_div">
				<div class="each_field_row">
					<div class="data_field_div">Publication Id:</div>
					<div class="data_field_title"><@s.property value="experiment.pubMedId" /></div>
					<div style="clear:both"></div> 
				</div>
				
				<div class="each_field_row">
					<div class="data_field_div">Title:</div>
					<div class="data_field_desc"><@s.property value="experiment.pubTitle" /></div>
					<div style="clear:both"></div> 
				</div>
				<div class="each_field_row">
					<div class="data_field_div">Publication Date:</div>
					<div class="data_field_date"><@s.date name="experiment.publicationDate"  format="yyyy-MM-dd" /> </div>
					<div style="clear:both"></div> 
				</div>
				<div class="each_field_row">
					<div class="data_field_div">Abstract:</div>
					<div class="data_field_desc"><@s.property value="experiment.abstraction" escape=false /></div>
					<div style="clear:both"></div> 
				</div>
				<div class="each_field_row">
					<div class="data_field_div">Experiment Design:</div>
					<div class="data_field_desc"><@s.property value="experiment.experimentDesign" /></div>
					<div style="clear:both"></div> 
				</div>
				<div class="each_field_row">
					<div class="data_field_div">Experiment Type:</div>
					<div class="data_field_desc"><@s.property value="experiment.experimentType" /></div>
					<div style="clear:both"></div> 
				</div>
				<div class="each_field_row">
					<div class="data_field_div">Affiliations:</div>
					<div class="data_field_desc"><@s.property value="experiment.affiliations" /></div>
					<div style="clear:both"></div> 
				</div>
				<div class="each_field_row">
					<div class="data_field_div">Authors:</div>
					<div class="data_field_desc"><@s.property value="experiment.authors" /></div>
					<div style="clear:both"></div> 
				</div>
				<div class="each_field_row">
					<div class="data_field_div">Publication:</div>
					<div class="data_field_desc"><@s.property value="experiment.publication"  escape=false/></div>
					<div style="clear:both"></div> 
				</div>
			</div>

			<div class="data_outer_noborder_div">
				<@s.if test="%{permissionBean.viewAllowed == false}">
			 	<div class="data_link_big">
			 		<a href="${base}/perm/showApplyForPerms.jspx?experiment.id=<@s.property value='experiment.id' />">Apply For Permissions</a>
			 	</div>
			 	</@s.if>

                <@s.if test="%{experiment.approved == false}">
                    <@s.if test="%{user.userType == 1 || user.userType ==2}">
                    <div class="data_link">
                        <a href='${base}/data/showApproveExp.jspx?experiment.id=<@s.property value='experiment.id' />&fromMyExp=<@s.property value="fromMyExp" />'>Approve</a>
                    </div>
                    </@s.if>
			 	</@s.if>
                <@s.else>
                    <@s.if test="%{experiment.owner.id == user.id || user.userType == 1 || user.userType ==2}">
                    <div class="data_link_big">
                        <a href='${base}/data/showMdReg.jspx?experiment.id=<@s.property value='experiment.id' />&fromMyExp=<@s.property value="fromMyExp" />' id='wait_modal' name='wait_modal'>Public Registration</a>
                    </div>
                    <div id='mask'></div>
                    <div id='modal_window' >
                         Calling the Research Master Web Service, please wait ... <img src="${base}/images/wait_loader.gif" class="loading_image">
                    </div>
                    </@s.if>
                </@s.else>
			 	<@s.if test="%{permissionBean.changePermAllowed}"> 
			 	<div class="data_link">
			 		<a href='${base}/perm/showChangeExpPerms.jspx?experiment.id=<@s.property value='experiment.id' />&fromMyExp=<@s.property value="fromMyExp" />'>Permissions</a>
			 	</div>
			 	</@s.if>
			 	<@s.if test="%{permissionBean.deleteAllowed == true}">
				<div class="data_link">
					<div id='confirm-dialog'>
						<div class="msg_content">All data will be removed from the database permanently!<p>Are you sure to delete this experiment?</p></div>
						<div id='confirm'>
							<div class='header'><span>Deleting Experiment Confirm</span></div>
							<div class='message'></div>
							<div class='buttons'>
								<div class='no simplemodal-close'>No</div>
								<div class='yes'>Yes</div>
							</div>
						</div>
						<a href="${base}/${deleteExpActName}?experiment.id=<@s.property value='experiment.id' />" class="confirm" >Delete</a>
					</div>
				</div>
			 	</@s.if>
			 	<@s.if test="%{permissionBean.updateAllowed == true}">
			 	<div class="data_link">
			 		<a href='${base}/data/showEditExp.jspx?experiment.id=<@s.property value='experiment.id' />&fromMyExp=<@s.property value="fromMyExp" />'>Update</a>
			 	</div>
			 	</@s.if>
			 	<div style="clear:both"></div>  
			</div>

            <div class="parag_title_div">
				Datasets
			</div>
            <div class="data_outer_div">
                <table class="exp_ds_tab">
                     <tr>
                         <td>
                            <span class="name_title">
                                A total of <font color="green">${totalDatasetNum}</font> Datasets
                            </span>
                         </td>
                         <td>
                            <div class="data_link">
                                <@s.if test="%{permissionBean.viewAllowed == true}">
                                    <a href='${base}/data/listDatasets.jspx?experiment.id=<@s.property value='experiment.id' />&fromMyExp=<@s.property value="fromMyExp" />'>
                                    <@s.if test="%{totalDatasetNum == 0}">
                                        <@s.if test="%{permissionBean.importAllowed == true}">
                                            Add Dataset
                                        </@s.if>
                                    </@s.if>
                                     <@s.else>
                                        View Datasets
                                    </@s.else>
                                    </a>
                                </@s.if>
                            </div>
                         </td>
                     </tr>
                </table>
            </div>
            <div style="clear:both"></div>
            <div class="blank_separator"></div>
		</div>
		<div class="container_inner_right">
			<#include "../template/user_nav.ftl">
		</div>
		<div style="clear:both"></div>  
	</div>
	<#include "../template/footer.ftl"/>
</body>
</html>