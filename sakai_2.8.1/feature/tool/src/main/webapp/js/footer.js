function save(description) {
	$.ajax({
		url : 'save.htm',
		data : 'description=' + description,
		datatype : 'json',
		success : function(data) {
			$('#ajaxMessage').html(data.message);
		}
	});
}

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
$(document)
		.ready(
				function() {

					oTable = $('#catalog_description_table')
							.dataTable(
									{
										"bJQueryUI" : true,
										"bProcessing" : true,
										"sAjaxSource" : 'list.json',
										"sPaginationType" : "full_numbers",
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
										/* Language */{
											"bSearchable" : false,
											"bVisible" : false
										},
										/* Description */null ],

										/* after init is complete, we set the last_column value :
										 * - with a tick image if description is not null
										 * - with an alert image if description is null
										 */
										"fnInitComplete" : function(oSettings,
												json) {
											$(
													"td:nth-child(3):contains('true')")
													.css("background-image",
															'url("/library/image/silk/accept.png")');
											$(
													"td:nth-child(3):contains('true')")
													.css("background-repeat",
															"no-repeat");
											$(
													"td:nth-child(3):contains('true')")
													.wrapInner(
															'<span  class="hidden_description_flag">');

											$(
													"td:nth-child(3):contains('false')")
													.css("background-image",
															'url("/library/image/silk/exclamation.png")');
											$(
													"td:nth-child(3):contains('false')")
													.css("background-repeat",
															"no-repeat");
											$(
													"td:nth-child(3):contains('false')")
													.wrapInner(
															'<span  class="hidden_description_flag">');
										}

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
$('#catalog_description_table').on(
		"click",
		"tbody tr",
		function(event) {

			var title = $(this).children("td:nth-child(1)").text() + ' - '
					+ $(this).children("td:nth-child(2)").text();
			$("#cdm_editor").dialog('option', 'title', title);
			$('#editor_area').val('');
			$("#cdm_editor").dialog('open');
		});

/***************************** Binding 'click' event on table buttons (save and cancel) **********************************/
$("#save_button").on("click", function(event) {
	save($('#editor_area').val());
	$("#cdm_editor").dialog('close');
	$("#infoMessage").html("Here's an info message with dynamic content.");
});

$("#cancel_button").on("click", function(event) {
	$("#cdm_editor").dialog('close');
});
