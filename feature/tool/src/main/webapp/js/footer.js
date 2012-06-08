function save(description) {
    $.ajax({
      url: 'save.htm',
      data: 'description=' + description,  
      datatype:   'json',
      success: function(data) {
        $('#ajaxMessage').html(data.message);
      }
    });
  }
  
var dialogWidth = $(window).width() * 0.9;
var dialogHeight = $(document).height();
var ckeditorWidth = $(window).width() * 0.8;

var myToolbar = [
		{
			name : 'document',
			items : [ 'Source', '-', 'Save', 'NewPage', 'DocProps', 'Preview',
					'Print', '-', 'Templates' ]
		},
		// { name: 'clipboard', items : [
		// 'Cut','Copy','Paste','PasteText','PasteFromWord','-','Undo','Redo' ]
		// },
		// { name: 'editing', items : [
		// 'Find','Replace','-','SelectAll','-','SpellChecker', 'Scayt' ] },
		// { name: 'forms', items : [ 'Form', 'Checkbox', 'Radio', 'TextField',
		// 'Textarea', 'Select', 'Button', 'ImageButton', 'HiddenField' ] },
		// { name: 'basicstyles', items : [
		// 'Bold','Italic','Underline','Strike','Subscript','Superscript','-','RemoveFormat'
		// ] },
		{
			name : 'paragraph',
			items : [ 'NumberedList', 'BulletedList', '-', 'Outdent', 'Indent',
					'-', 'Blockquote', 'CreateDiv', '-', 'JustifyLeft',
					'JustifyCenter', 'JustifyRight', 'JustifyBlock', '-',
					'BidiLtr', 'BidiRtl' ]
		}, {
			name : 'links',
			items : [ 'Link', 'Unlink', 'Anchor' ]
		},
//               { name: 'insert', items : [
//               'Image','Flash','Table','HorizontalRule','Smiley','SpecialChar','PageBreak','Iframe'
//               ] },
//               { name: 'styles', items : [ 'Styles','Format','Font','FontSize' ] },
//               { name: 'colors', items : [ 'TextColor','BGColor' ] },
//               { name: 'tools', items : [ 'Maximize', 'ShowBlocks','-','About' ] }

];

//2. Create a config var to hold your toolbar
var config = {
	height : 180,
	width : ckeditorWidth,
	position : [ 'center', 'center' ],
	toolbar_mySimpleToolbar : myToolbar,
	toolbar : 'mySimpleToolbar'
};

//3. change the textarea into an editor using your config and toolbar
$('#editor_area').ckeditor(config);

var frame = parent.document.getElementById(window.name);
$(frame).css('height', dialogHeight);

$(document).ready(function() {
	
	
	
	oTable = $('#catalog_description_table').dataTable({
		"bJQueryUI" : true,	
		"bProcessing": true,
		"sAjaxSource": 'list.json',
		"sPaginationType" : "full_numbers",
		 "aoColumns": [
            /* Id */  { "bSearchable": false,
			                 "bVisible":    false },
			/* CourseId */  null,
			/* Title */ null,
			/* Department */ { "bSearchable": false,
			                 "bVisible":    false },
			/* Career */  { "bSearchable": false,
			                 "bVisible":    false },
			 /* Language */ { "bSearchable": false,
			 "bVisible":    false },			 
			/* Description */  null
        ],
		
		
		 "fnInitComplete": function(oSettings, json) {
$("td:nth-child(3):contains('true')").css("background-image",	'url("/library/image/silk/accept.png")');
$("td:nth-child(3):contains('true')").css("background-repeat", "no-repeat");
$("td:nth-child(3):contains('true')").wrapInner('<span  class="hidden_description_flag">');

$("td:nth-child(3):contains('false')").css("background-image",		'url("/library/image/silk/exclamation.png")');
$("td:nth-child(3):contains('false')").css("background-repeat", "no-repeat");
$("td:nth-child(3):contains('false')").wrapInner('<span  class="hidden_description_flag">');

    }
		
	});
	

});


$("#cdm_editor").dialog({
	autoOpen : false,
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

$("#save_button").on("click", function(event) {	
	save($('#editor_area').val());
	$("#cdm_editor").dialog('close');
	$("#infoMessage").html("Here's an info message with dynamic content.");
});

$("#cancel_button").on("click", function(event) {
	$("#cdm_editor").dialog('close');
});


