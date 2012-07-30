/* Refresh the datatable after saving a catalog description */
$.fn.dataTableExt.oApi.fnReloadAjax = function(oSettings, sNewSource,
		fnCallback, bStandingRedraw) {
	if (typeof sNewSource != 'undefined' && sNewSource != null) {
		oSettings.sAjaxSource = sNewSource;
	}
	this.oApi._fnProcessingDisplay(oSettings, true);
	var that = this;
	var iStart = oSettings._iDisplayStart;
	var aData = [];

	this.oApi._fnServerParams(oSettings, aData);

	oSettings.fnServerData(oSettings.sAjaxSource, aData, function(json) {
		/* Clear the old information from the table */
		that.oApi._fnClearTable(oSettings);

		/* Got the data - add it to the table */
		var aData = (oSettings.sAjaxDataProp !== "") ? that.oApi
				._fnGetObjectDataFn(oSettings.sAjaxDataProp)(json) : json;

		for ( var i = 0; i < aData.length; i++) {
			that.oApi._fnAddData(oSettings, aData[i]);
		}

		oSettings.aiDisplay = oSettings.aiDisplayMaster.slice();
		that.fnDraw();

		if (typeof bStandingRedraw != 'undefined' && bStandingRedraw === true) {
			oSettings._iDisplayStart = iStart;
			that.fnDraw(false);
		}

		that.oApi._fnProcessingDisplay(oSettings, false);

		/* Callback user function - for event handlers etc */
		if (typeof fnCallback == 'function' && fnCallback != null) {
			fnCallback(oSettings);
		}
	}, oSettings);
};

/* Set the last "Catalog description" table column :
 * - with a tick image if description is not null
 * - with an alert image if description is null */
function initDescriptionTable() {
	$("td:nth-child(3):contains('true')").addClass("td_image_description_true");
	$("td:nth-child(3):contains('true')").wrapInner("<span  class=\"hidden_description_flag\"></span>");

	$("td:nth-child(3):contains('false')").addClass("td_image_description_false");
	$("td:nth-child(3):contains('false')").wrapInner("<span  class=\"hidden_description_flag\"></span>");
}

/* Save a catalog description */
function save(description, id, last_modified_date) {

	$.ajax({
		url : 'save.json',
		type : "POST",
		data : 'description=' + description + '&id=' + id + '&last_modified_date=' + last_modified_date,
		datatype : 'json',
		success : function(data) {
			$('#ajaxMessage').html(data.message);
			$('#ajaxReturn').show();

			if (data.status == 'success') {
				$('#ajaxReturn').addClass("info");
				//$('#ajaxReturn').fadeOut(4000);
			} else {
				$('#ajaxReturn').addClass("error");
			}

			oTable.fnReloadAjax(null, function() {
				initDescriptionTable();
			}, null);
		},

		error : function(xhr, ajaxOptions, thrownError) {
			var genericError = $('#genericError').val();
			$('#ajaxMessage').html(genericError);
			$('#ajaxReturn').addClass("error");
		}
	});
}

/* Get a catalog description and initialize the editor dialog box  */
function openDialogCatalogDescriptionCurrentRow(id) {
	$.ajax({
		url : 'get.json',
		data : 'id=' + id,
		datatype : 'json',
		success : function(cd) {

			if (cd.status == 'success') {
				var dialog_title = cd.courseid + ' - ' + cd.title;
				var description = cd.description;
				var last_modified_date = cd.last_modified_date;
				$("#selected_course_acad_department").html(cd.acad_department);
				$("#selected_course_acad_career").html(cd.acad_career);
				$("#selected_course_credits").html(cd.credits);
				$("#selected_course_requirements").html(cd.requirements);
				$("#selected_course_language").html(cd.language);
				$("#cdm_editor").dialog('option', 'title', dialog_title);
				$('#editor_area').val(description);
				$('#last_modified_date').val(last_modified_date);
				$("#cdm_editor").dialog('open');
			} else {
				$('#ajaxMessage').html(cd.message);
				$('#ajaxReturn').addClass("error");
			}
		},

		error : function(xhr, ajaxOptions, thrownError) {
			var genericError = $('#genericError').val();
			$('#ajaxMessage').html(genericError);
			$('#ajaxReturn').addClass("error");
		}
	});
}