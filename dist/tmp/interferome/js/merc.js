/**remove the message pane*/
$(document).ready(function () {
    $(".message_pane .delete").click(function () {
        $(".msg_p_div").remove();
    });
});

/** pagination orderby */
$(document).ready(function () {
    $("#item_select_size").change(function () {
        pagination();
    });
});

$(document).ready(function () {
    $("#item_order_by").change(function () {
        pagination();
    });
});

$(document).ready(function () {
    $("#item_order_by_type").change(function () {
        pagination();
    });
});

function pagination() {
    window.location.href = $('.page_url').attr('href') + "&pageSize=" + $("#item_select_size").val() + "&orderBy=" + $("#item_order_by").val() + "&orderByType=" + $("#item_order_by_type").val();
}
;

$('#delete_event').live('click', function (e) {
    e.preventDefault();
    window.location.href = $('#delete_event').attr('href') + "&pageSize=" + $("#item_select_size").val() + "&orderBy=" + $("#item_order_by").val() + "&orderByType=" + $("#item_order_by_type").val();
});


//experiment permissions
$(document).ready(function () {
    $("#add_permission").click(function () {
        var value_index = $("#selected_username").val();
        var selectedText = $('#selected_username option:selected').text();
        var rowIndex = $("#user_permissions > tbody > tr").length;
        //if an user permissions already added, just return.
        var el = $("input[id=user_id][value=" + value_index + "]").val();
        if (el != null) {
            return;
        }

        if (value_index != '-1') {
            $('#user_permissions > tbody:last').append("<tr>" +
                "<td><center>" + selectedText + "</center>" +
                "<input type='hidden' name='permissionBeans[" + rowIndex + "].id' value='0' id='permissionBeans_" + rowIndex + "__id'/>" +
                "<input type='hidden' name='permissionBeans[" + rowIndex + "].userId' value='" + value_index + "' id='user_id'/></td>" +
                "<input type='hidden' name='permissionBeans[" + rowIndex + "].name' value='" + selectedText + "' id='permissionBeans_" + rowIndex + "__name'/></td>" +
                "<td><center><input type='checkbox' name='permissionBeans[" + rowIndex + "].viewAllowed' value='true' id='permissionBeans_" + rowIndex + "__viewAllowed'/></center></td>" +
                "<td><center><input type='checkbox' name='permissionBeans[" + rowIndex + "].updateAllowed' value='true' id='permissionBeans_" + rowIndex + "_updateAllowed'/></center></td>" +
                "<td><center><input type='checkbox' name='permissionBeans[" + rowIndex + "].importAllowed' value='true'  id='permissionBeans_" + rowIndex + "__importAllowed'/></center></td>" +
                "<td><center><input type='checkbox' name='permissionBeans[" + rowIndex + "].exportAllowed' value='true'  id='permissionBeans_" + rowIndex + "__exportAllowed'/></center></td>" +
                "<td><center><input type='checkbox' name='permissionBeans[" + rowIndex + "].deleteAllowed' value='true'  id='permissionBeans_" + rowIndex + "__deleteAllowed'/></center></td>" +
                "<td><center><input type='checkbox' name='permissionBeans[" + rowIndex + "].changePermAllowed' value='true'  id='permissionBeans_" + rowIndex + "__changePermAllowed'/></center></td>" +
                "</tr>");
        }
    });
});


$("#user_permissions input[type=checkbox]").live('click', function () {
    var act = $(this).attr('name');
    if ($(this).is(":checked")) {
        //alert(act);
        if (act == 'permForAllRegUser.updateAllowed' || act == 'permForAllRegUser.importAllowed' || act == 'permForAllRegUser.exportAllowed' || act == 'permForAllRegUser.deleteAllowed' || act == 'permForAllRegUser.changePermAllowed') {
            setViewTrueForAllRegUser();
        }
        if (act == 'permForAnonyUser.updateAllowed' || act == 'permForAnonyUser.importAllowed' || act == 'permForAnonyUser.exportAllowed' || act == 'permForAnonyUser.deleteAllowed' || act == 'permForAnonyUser.changePermAllowed') {
            setViewTrueForAnonymous();
        }
        //for dynamically added user permission
        if ((act.indexOf('permissionBeans') != -1) && (act.indexOf('updateAllowed') != -1)) {
            setViewAllowedTrueForDyn(act);
        }
        if ((act.indexOf('permissionBeans') != -1) && (act.indexOf('importAllowed') != -1)) {
            setViewAllowedTrueForDyn(act);
        }
        if ((act.indexOf('permissionBeans') != -1) && (act.indexOf('exportAllowed') != -1)) {
            setViewAllowedTrueForDyn(act);
        }
        if ((act.indexOf('permissionBeans') != -1) && (act.indexOf('deleteAllowed') != -1)) {
            setViewAllowedTrueForDyn(act);
        }
        if ((act.indexOf('permissionBeans') != -1) && (act.indexOf('changePermAllowed') != -1)) {
            setViewAllowedTrueForDyn(act);
        }
    } else {//for dynamic added checkbox
        if ((act.indexOf('permissionBeans') != -1) && (act.indexOf('viewAllowed') != -1)) {
            foreViewAllowedTrueForDyn(act);
        }
    }
});
//for all-registered users
$('input[name=permForAllRegUser.viewAllowed]').live('click', function () {
    if ($(this).is(":checked") == false) {
        if ($('input[name=permForAllRegUser.updateAllowed]').is(":checked")) {
            keepViewAllowedTrueForAllUsers();
        }
        if ($('input[name=permForAllRegUser.importAllowed]').is(":checked")) {
            keepViewAllowedTrueForAllUsers();
        }
        if ($('input[name=permForAllRegUser.exportAllowed]').is(":checked")) {
            keepViewAllowedTrueForAllUsers();
        }
        if ($('input[name=permForAllRegUser.deleteAllowed]').is(":checked")) {
            keepViewAllowedTrueForAllUsers();
        }
        if ($('input[name=permForAllRegUser.changePermAllowed]').is(":checked")) {
            keepViewAllowedTrueForAllUsers();
        }
    }
});

//for all-annonymous user
$('input[name=permForAnonyUser.viewAllowed]').live('click', function () {
    if ($(this).is(":checked") == false) {
        if ($('input[name=permForAnonyUser.editAllowed]').is(":checked")) {
            keepViewAllowedTrueForAnonymous();
        }
        if ($('input[name=permForAnonyUser.importAllowed]').is(":checked")) {
            keepViewAllowedTrueForAnonymous();
        }
        if ($('input[name=permForAnonyUser.exportAllowed]').is(":checked")) {
            keepViewAllowedTrueForAnonymous();
        }
        if ($('input[name=permForAnonyUser.deleteAllowed]').is(":checked")) {
            keepViewAllowedTrueForAnonymous();
        }
        if ($('input[name=permForAnonyUser.changePermAllowed]').is(":checked")) {
            keepViewAllowedTrueForAnonymous();
        }
    }
});
//for all-registered users
function setViewTrueForAllRegUser() {
    $('input[name=permForAllRegUser.viewAllowed]').attr('checked', true);
}
;
function keepViewAllowedTrueForAllUsers() {
    $('input[name=permForAllRegUser.viewAllowed]').attr('checked', true);
}
;

//for anonymous users
function setViewTrueForAnonymous() {
    $('input[name=permForAnonyUser.viewAllowed]').attr('checked', true);
}
;

function keepViewAllowedTrueForAnonymous() {
    $('input[name=permForAnonyUser.viewAllowed]').attr('checked', true);
}
;

//for dynamic added checkbox
function setViewAllowedTrueForDyn(name) {
    var strv = name.substring(0, name.indexOf('.'));
    $('input[name=' + strv + '.viewAllowed]').attr('checked', true);
}
;

function foreViewAllowedTrueForDyn(name) {
    var strv = name.substring(0, name.indexOf('.'));
    if ($('input[name=' + strv + '.updateAllowed]').is(":checked")) {
        keepViewAllowedTrueForDyn(strv);
    }
    if ($('input[name=' + strv + '.importAllowed]').is(":checked")) {
        keepViewAllowedTrueForDyn(strv);
    }
    if ($('input[name=' + strv + '.exportAllowed]').is(":checked")) {
        keepViewAllowedTrueForDyn(strv);
    }
    if ($('input[name=' + strv + '.deleteAllowed]').is(":checked")) {
        keepViewAllowedTrueForDyn(strv);
    }
    if ($('input[name=' + strv + '.changePermAllowed]').is(":checked")) {
        keepViewAllowedTrueForDyn(strv);
    }
}
;

function keepViewAllowedTrueForDyn(name) {
    $('input[name=' + name + '.viewAllowed]').attr('checked', true);
}
;

//permission request
$("#permission_req input[type=checkbox]").live('click', function () {
    var act = $(this).attr('name');
    if ($(this).is(":checked")) {
        if (act == 'permReq.updateAllowed' || act == 'permReq.importAllowed' || act == 'permReq.exportAllowed' || act == 'permReq.deleteAllowed' || act == 'permReq.changePermAllowed') {
            $('input[name=permReq.viewAllowed]').attr('checked', true);
        }
    }
});

//apply for permissions
$('input[name=permReq.viewAllowed]').live('click', function () {
    if ($(this).is(":checked") == false) {
        if ($('input[name=permReq.updateAllowed]').is(":checked")) {
            $(this).attr('checked', true);
        }
        if ($('input[name=permReq.importAllowed]').is(":checked")) {
            $(this).attr('checked', true);
        }
        if ($('input[name=permReq.exportAllowed]').is(":checked")) {
            $(this).attr('checked', true);
        }
        if ($('input[name=permReq.deleteAllowed]').is(":checked")) {
            $(this).attr('checked', true);
        }
        if ($('input[name=permReq.changePermAllowed]').is(":checked")) {
            $(this).attr('checked', true);
        }
    }
});


$(function () {
    $('a#addtionalParty').click(function (e) {
        e.preventDefault();
        var $this = $(this);
        var horizontalPadding = 20;
        var verticalPadding = 20;

        $('<iframe id="externalSite" class="externalSite" src="' + this.href + '" />').dialog({
            title:($this.attr('title')) ? $this.attr('title') : 'External Site',
            autoOpen:true,
            width:550,
            height:450,
            modal:true,
            resizable:true,
            autoResize:true,
            overlay:{
                opacity:0.5,
                background:"black"
            }
        }).width(550 - horizontalPadding).height(450 - verticalPadding);
    });
});

$('#cancelAddParty').live('click', function (e) {
    e.preventDefault();
    window.parent.$('#externalSite').dialog('close');
    window.parent.$('#externalSite').remove();
});

$('#save_rm_party').live('click', function (e) {
    e.preventDefault();

    //fetch the party value from the popup windows
    var pKey = $('#ands_p_key').val();
    var pTitle = $('#ands_p_title').val();
    var pGivenName = $('#ands_p_givenname').val();
    var pSName = $('#ands_p_sname').val();
    var pEmail = $('#ands_p_email').val();
    var pAddress = $('#ands_p_address').val();
    var pUrl = $('#ands_p_url').val();
    var pIdType = $('#ands_p_idtype').val();
    var pIdValue = $('#ands_p_idvalue').val();
    var pSrcType = $('#ands_p_srctype').val();
    var pSrcValue = $('#ands_p_srcvalue').val();
    var pGroupName = $('#ands_p_groupname').val();
    var pFromRm = $('#ands_p_fromrm').val();
    //try to find a party which already added in the form
    //If a party from the research master web service,
    //then the party key will be used as identifier value to find a party which is already added
    //If a party from a user defined. then we have to check the party user full name( givenname and surname)
    if (pFromRm == 'true') {
        var alreadyAddedPKey = window.parent.$("input[value=" + pKey + "]").val();
        if (alreadyAddedPKey != null) {
            //close the parent windows
            closeParentWindows();
            return;
        }
    } else {
        //find a party which already added by an user input
        var alreadyAddedUserName = window.parent.$("input[value=" + pGivenName + "]").val();
        var alreadyAddedSName = window.parent.$("input[value=" + pSName + "]").val();
        if ((alreadyAddedUserName != null) && (alreadyAddedSName != null)) {
            //close the parent windows
            closeParentWindows();
            return;
        }
    }

    if (window.parent.$('#md_reg_tab_data > tbody').length == 0) {
        window.parent.$('#md_reg_tab_data').append('<tbody />');
    }
    var rowIndex = window.parent.$("#md_reg_tab_data > tbody > tr").length;
    //alert("party table rows: " + rowIndex);
    closePartyNotFoundDiv();
    var findTbody = window.parent.$('#md_reg_tab_data > tbody:last');

    $(findTbody).append("<tr>\n<td align='center' width='50'>\n<input id='mdRegForm_partyList_" + rowIndex + "__selected' type='checkbox' checked='checked' name='partyList[" + rowIndex + "].selected' value='true' />\n" +
        "</td>\n" +
        "<td>\n<div class='md_blue'>" + pTitle + " " + pGivenName + " " + pSName + " - " + pGroupName + "\n" +
        "<input id='mdRegForm_partyList_" + rowIndex + "__partyKey' type='hidden' name='partyList[" + rowIndex + "].partyKey' value='" + pKey + "' />\n" +
        "<input id='mdRegForm_partyList_" + rowIndex + "__personTitle' type='hidden' name='partyList[" + rowIndex + "].personTitle' value='" + pTitle + "'/>\n" +
        "<input id='mdRegForm_partyList_" + rowIndex + "__personGivenName' type='hidden' name='partyList[" + rowIndex + "].personGivenName' value='" + pGivenName + "' />\n" +
        "<input id='mdRegForm_partyList_" + rowIndex + "__personFamilyName' type='hidden' name='partyList[" + rowIndex + "].personFamilyName' value='" + pSName + "' />\n" +
        "<input id='mdRegForm_partyList_" + rowIndex + "__email' type='hidden' name='partyList[" + rowIndex + "].email' value='" + pEmail + "' />\n" +
        "<input id='mdRegForm_partyList_" + rowIndex + "__address' type='hidden' name='partyList[" + rowIndex + "].address' value='" + pAddress + "' />\n" +
        "<input id='mdRegForm_partyList_" + rowIndex + "__url' type='hidden' name='partyList[" + rowIndex + "].url' value='" + pUrl + "' />\n" +
        "<input id='mdRegForm_partyList_" + rowIndex + "__identifierType' type='hidden' name='partyList[" + rowIndex + "].identifierType' value='" + pIdType + "' />\n" +
        "<input id='mdRegForm_partyList_" + rowIndex + "__identifierValue' type='hidden' name='partyList[" + rowIndex + "].identifierValue' value='" + pIdValue + "' />\n" +
        "<input id='mdRegForm_partyList_" + rowIndex + "__originateSourceType' type='hidden' name='partyList[" + rowIndex + "].originateSourceType' value='" + pSrcType + "' />\n" +
        "<input id='mdRegForm_partyList_" + rowIndex + "__originateSourceValue' type='hidden' name='partyList[" + rowIndex + "].originateSourceValue' value='" + pSrcValue + "' />\n" +
        "<input id='mdRegForm_partyList_" + rowIndex + "__groupName' type='hidden' name='partyList[" + rowIndex + "].groupName' value='" + pGroupName + "' />\n" +
        "<input id='mdRegForm_partyList_" + rowIndex + "__fromRm' type='hidden' name='partyList[" + rowIndex + "].fromRm' value='" + pFromRm + "' />\n" +
        "</div>\n</td>\n</tr>\n"
    );
    //close the parent windows
    closeParentWindows();

});

function closeParentWindows() {
    //close the popup windows
    window.parent.$('#externalSite').dialog('close');
    window.parent.$('#externalSite').remove();
}
;

function closePartyNotFoundDiv() {
    var pnf = window.parent.$("#party_not_found").val();
    if (pnf != null) {
        window.parent.$('#party_not_found').remove();
    }

}

$(function () {
    $('a#selectLicence').click(function (e) {
        e.preventDefault();
        var $this = $(this);
        var horizontalPadding = 20;
        var verticalPadding = 20;

        $('<iframe id="externalSite" class="externalSite" src="' + this.href + '" />').dialog({
            title:($this.attr('title')) ? $this.attr('title') : 'External Site',
            autoOpen:true,
            width:550,
            height:450,
            modal:true,
            resizable:true,
            autoResize:true,
            overlay:{
                opacity:0.5,
                background:"black"
            }
        }).width(550 - horizontalPadding).height(450 - verticalPadding);
    });
});

$('#saveLicence').live('click', function (e) {
    e.preventDefault();
    var type = $('#plicence_type').val();
    var comm = $('#plicence_comm').val();
    var deri = $('#plicence_deri').val();
    var juri = $('#plicence_juri').val();
    var licence = $('#plicence_cont').val();

    if (licence == null || licence == "") {
        alert("The license has not been provided");
    } else {
        window.parent.$('#flicence_type').val(type);
        window.parent.$('#flicence_comm').val(comm);
        window.parent.$('#flicence_deri').val(deri);
        window.parent.$('#flicence_juri').val(juri);
        window.parent.$('#flicence_cont').val(licence);
        window.parent.$('.license_content').text(licence);
        window.parent.$('#externalSite').dialog('close');
        window.parent.$('#externalSite').remove();
    }
});


$('#cancelLicence').live('click', function (e) {
    e.preventDefault();
    window.parent.$('#externalSite').dialog('close');
    window.parent.$('#externalSite').remove();
});

$('#datasetImport').live('submit', function (e) {
    e.preventDefault();
    var options = {
        dataType:'json',
        beforeSubmit:validateForm,
        success:showResponse,
        resetForm:true
    };
    $(this).ajaxForm(options);
});


function validateForm() {
    var fileName = $('#datasetFile').val();
    if (fileName == "") {
        var emsg = "The dataset file must be provided";
        showErrorMsg(emsg);
        return false;
    }

    var extIndex = parseInt(fileName.toString().lastIndexOf(".")) + 1;
    var fileExt = fileName.toString().substr(extIndex);
    if (fileExt != "txt") {
        var emsg = "The dataset file format is not supported";
        showErrorMsg(emsg);
        return false;
    }
    $('#uploading_info').show();
    return true;
}
;

function showResponse(data) {
    var status = data.success;
    var msg = data.message;
    if (status == 'true') {
        showSuccessMsg(msg);
    } else {
        showErrorMsg(msg);
    }
}
;

function showSuccessMsg(smsg) {

    //show the success message div
    $('.file_success_message').show();
    //hide the error message div if any
    $('.file_error_message').hide();
    //show the success message
    $('#fsuccess_msg').html(smsg);

    //hide the uploading info
    hideUploadingInfo();
    doAfterImport(true);
}
;

function showErrorMsg(emsg) {
    //show the error message div
    $('.file_error_message').show();
    //hide the success message div
    $('.file_success_message').hide();
    //show the error message
    $('#ferror_msg').html(emsg);

    //hide the uploading info
    hideUploadingInfo();
}
;

function hideUploadingInfo() {
    $('#uploading_info').hide();
}
;

//end of importing dataset

// search function ajax call get interferon sub type
$(document).ready(function () {
    $("#ifn_type").change(function () {
        var value_index = $("#ifn_type").val();
        //alert("value_index: " + value_index);
        if (value_index == '-1') {
            $("#ifn_sub_type").get(0).options.length = 0;
            $("#ifn_sub_type").get(0).options[0] = new Option("All", "-1");
            return;
        }
        $.ajax({
            type:"get",
            url:"findSubType.jspx?",
            data:{'searchBean.ifnType':value_index},
            cache:false,
            contentType:"application/json; charset=utf-8",
            dataType:"json",
            success:function (respData) {
                $("#ifn_sub_type").get(0).options.length = 0;
                if (value_index == 'I') {
                    $("#ifn_sub_type").get(0).options[0] = new Option("All", "-1");
                }
                $.each(respData, function (key, value) {
                    $("#ifn_sub_type").get(0).options[$("#ifn_sub_type").get(0).options.length] = new Option(value, key);
                });
            },

            error:function () {
                $("#ifn_sub_type").get(0).options.length = 0;
                alert("Failed to load the interferon sub types");
            }
        });
    })
});

//remove the factor and factor value, not use it
$('.remove_image').live('click', function () {
    var id = $(this).attr("id");
    $("tr[id='" + id + "']").remove();
    //resort the input element index
    $(".search_con_tab > tbody > tr").each(function (index) {
        var id = $(this).attr("id");
        $(this).attr("id", index);
        $("input[name='selectedFactorValues[" + id + "].factorId']").attr("name", "selectedFactorValues[" + index + "].factorId");
        $("input[name='selectedFactorValues[" + id + "].factorName']").attr("name", "selectedFactorValues[" + index + "].factorName");
        $("input[name='selectedFactorValues[" + id + "].valueId']").attr("name", "selectedFactorValues[" + index + "].valueId");
        $("input[name='selectedFactorValues[" + id + "].value']").attr("name", "selectedFactorValues[" + index + "].value");
        $("img[id='" + id + "']").attr("id", index);
    });

    return;
});


//check the radio options
$("input[@type='radio']").live('click', function () {
    var checkedValue = $(this).val();
    var inputName = $(this).attr('name');

    //for dose
    if (inputName == 'searchBean.anyRangeDose') {
        if (checkedValue == "any") {
            $('.dose_range_div').hide();
        } else {
            $('.dose_range_div').show();
        }
    }

    //for treatment time
    if (inputName == 'searchBean.anyRangeTime') {
        if (checkedValue == "any") {
            $('.time_range_div').hide();
        } else {
            $('.time_range_div').show();
        }
    }

    //for normal or abnormal
    if (inputName == 'searchBean.variation') {

        if (checkedValue == "Normal") {
            $('.normal_ab_div').hide();
            //reset abnormal select option to default
            $("#ab_variation option[value='-1']").attr('selected', true);
        } else if (checkedValue == "any") {
            $('.normal_ab_div').hide();
            //reset abnormal select option to default
            $("#ab_variation option[value='-1']").attr('selected', true);
        } else {
            $('.normal_ab_div').show();
        }
    }

    //for fold change
    if (inputName == 'searchBean.anyRangeFold') {
        if (checkedValue == "any") {
            //remove up if any
            $('#foldchange_up').html('');
            //remove down if any
            $('#foldchange_down').html('');
            //hide the up and down selection
            $('#selected_up_down').hide();
        } else {
            //show the up and down selection
            $('#selected_up_down').show();
        }
        //reset the select option to default
        $("#selected_up_down").val("-1").attr('selected', true);
    }
});

$(document).ready(function () {
    $("#selected_up_down").change(function () {
        var checkedFoldRange = $("input[name='searchBean.anyRangeFold']:checked").val();
        //only byrange selected
        if (checkedFoldRange == 'byrange') {
            var value_index = $("#selected_up_down").val();
            // alert("selected value index: " + value_index);
            //if index -1, just return
            if (value_index == '-1') {
                return;
            }
            if (value_index == 'up') {
                var html = $('#foldchange_up').html();
                if (html == '') {
                    createUpRange();
                }
            } else {
                var html = $('#foldchange_down').html();
                if (html == '') {
                    createDownRange();
                }
            }
        }
    });
});


//remove the fold change up
$('#remove_up').live('click', function () {
    $('#foldchange_up').html('');
    $("#selected_up_down").val("-1").attr('selected', true);
    return;
});

//remove the fold change down
$('#remove_down').live('click', function () {
    $('#foldchange_down').html('');
    $("#selected_up_down").val("-1").attr('selected', true);
    return;
});


function createUpRange() {
    var upInput = "<table>" +
        "<tr>" +
        "<td width='50'>Up: <input id='upProvided' type='hidden' value='true' name='searchBean.upProvided'></td>" +
        "<td><input id='upValue' class='search_input' type='text' value='2.0' name='searchBean.upValue'></td>" +
        "<td class='search_unit'>(value &gt;= 2.0)</td>" +
        "<td><img src='../images/delete.png' id='remove_up' class='remove_image' /></td>" +
        "</tr>" +
        "</table>";
    $('#foldchange_up').html(upInput);
    return;
}

function createDownRange() {
    var downInput = "<table>" +
        "<tr>" +
        "<td width='50'>Down: <input id='downProvided' type='hidden' value='true' name='searchBean.downProvided'></td>" +
        "<td><input id='downValue' class='search_input' type='text' value='2.0' name='searchBean.downValue'></td>" +
        "<td class='search_unit'>(value &gt;= 2.0)</td>" +
        "<td><img id='remove_down' class='remove_image' src='../images/delete.png'></td>" +
        "</tr>" +
        "</table>";
    $('#foldchange_down').html(downInput);
    return;
}

//organ
$('#select_organ').live('change', function () {
    var totalselected = $("#select_organ option:selected").length;
    //if select all option, just return.
    if (totalselected == 1) {
        var selectVal = $("#select_organ option:selected").val();
        if (selectVal == '-1') {
            return;
        }
    }
    //if not select the all option, just make sure the all option is not selected
    $("#select_organ option:selected").each(function () {
        var selectVal = $(this).val();
        if (selectVal == '-1') {
            $(this).removeAttr("selected");
        }
    });
});

//cell
$('#select_cell').live('change', function () {
    var totalselected = $("#select_cell option:selected").length;
    //if select all option, just return.
    if (totalselected == 1) {
        var selectVal = $("#select_cell option:selected").val();
        if (selectVal == '-1') {
            return;
        }
    }
    //if not select the all option, just make sure the all option is not selected
    $("#select_cell option:selected").each(function () {
        var selectVal = $(this).val();
        if (selectVal == '-1') {
            $(this).removeAttr("selected");
        }
    });
});

//cell line
$('#select_cellline').live('change', function () {
    var totalselected = $("#select_cellline option:selected").length;
    //if select all option, just return.
    if (totalselected == 1) {
        var selectVal = $("#select_cellline option:selected").val();
        if (selectVal == '-1') {
            return;
        }
    }
    //if not select the all option, just make sure the all option is not selected
    $("#select_cellline option:selected").each(function () {
        var selectVal = $(this).val();
        if (selectVal == '-1') {
            $(this).removeAttr("selected");
        }
    });
});

$('a.expand_collapse').live('click', function (event) {
    event.preventDefault();

    //search result div
    var searchResulsDiv = $('div.search_results_div');
    var resultId = searchResulsDiv.attr('id');

    if (resultId != 'gene') {
        searchResulsDiv.html('');
    }
    //search conditions
    var hideSearchConDiv = $('div.hide_search_cond');
    var hideSearchConId = hideSearchConDiv.attr('id');
    if (hideSearchConId == 'search_open') {
        hideSearchConDiv.attr('id', "search_close");
        hideSearchConDiv.hide();
    } else {
        hideSearchConDiv.attr('id', "search_open");
        hideSearchConDiv.show();
        var menuDiv = $('div.search_menu_div');
        if (menuDiv != 'undefine') {
            menuDiv.html('');
        }
    }


});

