/*****************************Initialisation of frame sizes  **********************************/
var iframeHeight = 500;
var dialogWidth = $(window).width() * 0.9;
var dialogHeight = 450;
var editorHeight = 180;
var editorWidth = $(window).width() * 0.8;

var frame = parent.document.getElementById(window.name);
$(frame).css('height', iframeHeight);

/*****************************Initialisation of frame editor  **********************************/
var myToolbar = [
		{
			name : 'document',
			items : [ 'Source', '-', 'Save', 'NewPage', 'DocProps', 'Preview',
					'Print', '-', 'Templates' ]
		},
		{
			name : 'paragraph',
			items : [ 'NumberedList', 'BulletedList', '-', 'Outdent', 'Indent',
					'-', 'Blockquote', 'CreateDiv', '-', 'JustifyLeft',
					'JustifyCenter', 'JustifyRight', 'JustifyBlock', '-',
					'BidiLtr', 'BidiRtl' ]
		}, {
			name : 'links',
			items : [ 'Link', 'Unlink', 'Anchor' ]
		}, ];

var config = {
	height : editorHeight,
	width : editorWidth,
	position : [ 'center', 'center' ],
	toolbar_mySimpleToolbar : myToolbar,
	toolbar : 'mySimpleToolbar'
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
		"fnServerData" : function(sSource, aoData, fnCallback) {
			$.ajax({
				"dataType" : 'json',
				"type" : "POST",
				"url" : sSource,
				"data" : aoData,
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
		/* Description */null ],

		/* after init is complete, we set the last_column value :
		 * - with a tick image if description is not null
		 * - with an alert image if description is null
		 */
		"fnInitComplete" : function(oSettings, json) {
			initDescriptionTable();
		},
	});
});

/*****************************Initialisation of Editor dialog box  **********************************/
$("#cdm_editor").dialog({
	autoOpen : false,
	modal : true,
	resizable : true,
	draggable : true,
	bgiframe : true,
	modal : true,
	width : dialogWidth,
	height : dialogHeight,
	draggable : true,
	position : top,
	resizable : true
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

/***************************** Binding 'click' event on table buttons (save and cancel) **********************************/
$("#save_button").on("click", function(event) {
	save(escape($('#editor_area').val()), $('#course_id').val(), $('#last_modified_date').val());
	$("#cdm_editor").dialog('close');
});

$("#cancel_button").on("click", function(event) {
	$("#cdm_editor").dialog('close');
});
