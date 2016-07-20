/*****************************Initialisation of frame sizes  **********************************/
var iframeHeight = 500;
var dialogWidth = $(window).width() * 0.9;
var editorHeight = 180;

$(window).resize(function(){
dialogWidth = $(window).width() * 0.9;
$("#cdm_editor").dialog("option","width", dialogWidth);
        });

var frame = parent.document.getElementById(window.name);
$(frame).css('height', iframeHeight);

/*****************************Initialisation of frame editor  **********************************/
var myToolbar = [
		{
			name : 'document',
			items : [ 'Source', '-','Print' ]
		},
 { items : [ 'Bold','Italic','Underline','-','FontSize' ] },
		{
			name : 'paragraph',
			items : [ 'NumberedList', 'BulletedList', '-', 'Outdent', 'Indent',
					'-', 'Blockquote', '-', 'JustifyLeft',
					'JustifyCenter', 'JustifyRight', 'JustifyBlock', '-',
					'BidiLtr', 'BidiRtl' ]
		}];

var config = {
	height: editorHeight,
	position : [ 'center', 'center' ],
	toolbar_mySimpleToolbar : myToolbar,
	toolbar : 'mySimpleToolbar',
	fontSize_defaultLabel : '12'
};

$('#editor_area').ckeditor(config);	

/*****************************Initialisation of Catalog Description datatable  **********************************/
$(document).ready(function() {

	oTable = $('#catalog_description_table').dataTable({
		"bJQueryUI" : true,
		"bProcessing" : true,
		"sAjaxSource" : 'list.json',
		"sPaginationType" : "full_numbers",
		"bDestroy" : true,
		"bAutoWidth": false,
		"sDom": '<"H"lf<"#showInactives_checkbox_div" and ">r>t<"F"ip>',
		"fnServerData" : function(sSource, aoData, fnCallback) {
			var showInactives = $("#showInactives_checkbox").prop("checked");
			if (!showInactives){
				showInactives = "false";
			}
			var data_string = 'showInactives=' + showInactives;
			$.ajax({
				"dataType" : 'json',
				"type" : "POST",
				"url" : sSource,
				"data" : data_string,
				"success" : fnCallback,
				"error" : function(xhr, ajaxOptions, thrownError) {
					var genericError = $('#genericError').val();
					$('#ajaxMessage').html(genericError);
					$('#ajaxReturn').addClass("error");
				}
			});
		},
		"aoColumns" : [
		/* Id */{
			"bSearchable" : false,
			"bVisible" : false
		},
		/* CourseId */null,
		/* Title */null,
		/* Department */{
			"bSearchable" : false,
			"bVisible" : false
		},
		/* Career */{
			"bSearchable" : false,
			"bVisible" : false
		},
		/* Description */null,
		/* CreatedDate */{"sType": "date"}],

		/* after init is complete, we set the last_column value :
		 * - with a tick image if description is not null
		 * - with an alert image if description is null
		 */
		"fnInitComplete" : function(oSettings, json) {
			initDescriptionTable();			
			
			// we add the "inactive check box" to the table header 
			$("#showInactives_checkbox_div").html("<input type=\"checkbox\" id=\"showInactives_checkbox\"><span>" + messageBundle["table_showInactives_checkbox"] + "</span>");
			$('#showInactives_checkbox').on("click", function(event) {
				oTable.fnReloadAjax(null, function() {
							initDescriptionTable();
						}, null);
			});
		},
		"fnDrawCallback" : function(oSettings, json) {
			initDescriptionTable();
		}
	});
});

/*****************************Initialisation of Editor dialog box  **********************************/
$("#cdm_editor").dialog({
	autoOpen : false,
	modal : true,
	resizable : true,
	draggable : true,
	width : dialogWidth,
	height : "auto",
	autoResize:true
});
$("#save_button").button();
$("#cancel_button").button();
$("#accordeonWrap").accordion({
	autoHeight : false
});

/***************************** Binding 'click' event on a table row (open dialog_box) **********************************/
$('#catalog_description_table').on("click", "tbody tr", function(event) {

	var id_row = oTable.fnGetData(this)[0];
	$('#course_id').val(id_row);
	openDialogCatalogDescriptionCurrentRow(id_row);
});

$('#showInactives_checkbox').on("click", function(event) {

	oTable.fnReloadAjax(null, function() {
				initDescriptionTable();
			}, null);
});



/***************************** Binding 'click' event on table buttons (save and cancel) **********************************/
$("#save_button").on("click", function(event) {
	save(escape($('#editor_area').val()), $('#course_id').val(), $('#last_modified_date').val());
	$("#cdm_editor").dialog('close');
});

$("#cancel_button").on("click", function(event) {
	$("#cdm_editor").dialog('close');
});
