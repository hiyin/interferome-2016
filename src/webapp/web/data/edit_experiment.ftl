<#assign s=JspTaglibs["/WEB-INF/struts-tags.tld"] />
<#assign sj=JspTaglibs["/WEB-INF/struts-jquery-tags.tld"] />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>${pageTitle}</title>
<#include "../template/header.ftl"/>
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
			<@s.form action="editExp.jspx" namespace="/data" method="post">	
			<@s.hidden name="experiment.id" />
			<@s.hidden name="experiment.name" />
			<@s.hidden name="fromMyExp" />
			<@s.hidden name="experiment.entryDate" />
			<@s.hidden name="experiment.baseOwner" />
			<@s.hidden name="experiment.approved" />
			<@s.hidden name="experiment.owner.displayName" />
			<@s.hidden name="experiment.createdTime" />
			
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
					<div class="data_field_desc"><@s.textarea  name="experiment.description" cols="80" rows="5" cssClass="input_textarea" /></div>
					<div style="clear:both"></div> 
				</div>
				
				<div class="each_field_row">
					<div class="data_field_div">Approved:</div>
					<div class="data_field_app"><@s.property value="experiment.approved" /></div>
					<div style="clear:both"></div> 
				</div>
				
				<div class="each_field_row">
					<div class="data_field_div">Imported By:</div>
					<div class="data_field_value"><@s.property value="experiment.owner.displayName" /></div>
					<div style="clear:both"></div> 
				</div>
				
				<div class="each_field_row">
					<div class="data_field_div">Imported Date:</div>
					<div class="data_field_date"><@s.date name="experiment.createdTime"  format="dd-MM-yyyy 'at' hh:mm a" /></div>
					<div style="clear:both"></div> 
				</div>
			</div>
			<div class="parag_title_div">
				Publication
			</div>
			
			<div class="data_outer_div">
				<div class="each_field_row">
					<div class="data_field_div">PubMed ID:</div>
					<div class="data_field_title"><@s.textfield name="experiment.pubMedId" cssClass="input_field_normal"/></div>
					<div style="clear:both"></div> 
				</div>
				
				<div class="each_field_row">
					<div class="data_field_div">Title:</div>
					<div class="data_field_desc"><@s.textfield name="experiment.pubTitle" cssClass="input_field_normal"/></div>
					<div style="clear:both"></div> 
				</div>
				<div class="each_field_row">
					<div class="data_field_div">Publication Date:</div>
					<div class="data_field_date"><@sj.datepicker name="experiment.publicationDate" id="startdate" displayFormat="yy-mm-dd"  buttonImageOnly="true" /></div>
					<div style="clear:both"></div> 
				</div>
				<div class="each_field_row">
					<div class="data_field_div">Abstract:</div>
					<div class="data_field_desc"><@s.textarea  name="experiment.abstraction" cols="80" rows="15" cssClass="input_textarea" /></div>
					<div style="clear:both"></div> 
				</div>
				<div class="each_field_row">
					<div class="data_field_div">Experiment Design:</div>
					<div class="data_field_edesc">
                        <@s.textfield name="experiment.experimentDesign" cssClass="input_field_normal"/> &nbsp; (optional)
                    </div>
					<div style="clear:both"></div> 
				</div>
				<div class="each_field_row">
					<div class="data_field_div">Experiment Type:</div>
					<div class="data_field_edesc">
                        <@s.textfield name="experiment.experimentType" cssClass="input_field_normal"/> &nbsp; (optional)
                    </div>
					<div style="clear:both"></div> 
				</div>
				<div class="each_field_row">
					<div class="data_field_div">Affiliations:</div>
					<div class="data_field_edesc">
                        <@s.textfield name="experiment.affiliations" cssClass="input_field_normal"/> &nbsp; (optional)
                    </div>
					<div style="clear:both"></div> 
				</div>
				<div class="each_field_row">
					<div class="data_field_div">Authors:</div>
					<div class="data_field_desc"><@s.textfield name="experiment.authors" cssClass="input_field_normal"/></div>
					<div style="clear:both"></div> 
				</div>
				<div class="each_field_row">
					<div class="data_field_div">Publication:</div>
					<div class="data_field_desc"><@s.textarea  name="experiment.publication" cols="80" rows="5" cssClass="input_textarea" /></div>
					<div style="clear:both"></div> 
				</div>
			</div>
			<div class="data_outer_noborder_div">
				<@s.submit value="Update" cssClass="input_button" /> &nbsp; <@s.reset value="Clear" cssClass="input_button" />
			</div>
			</@s.form>
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