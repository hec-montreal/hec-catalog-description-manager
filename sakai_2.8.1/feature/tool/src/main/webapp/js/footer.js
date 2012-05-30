var myToolbar = [
	{ name: 'document', items : [ 'Source','-','Save','NewPage','DocProps','Preview','Print','-','Templates' ] },
	{ name: 'clipboard', items : [ 'Cut','Copy','Paste','PasteText','PasteFromWord','-','Undo','Redo' ] },
	{ name: 'editing', items : [ 'Find','Replace','-','SelectAll','-','SpellChecker', 'Scayt' ] },
	//{ name: 'forms', items : [ 'Form', 'Checkbox', 'Radio', 'TextField', 'Textarea', 'Select', 'Button', 'ImageButton', 'HiddenField' ] },
	'/',
	{ name: 'basicstyles', items : [ 'Bold','Italic','Underline','Strike','Subscript','Superscript','-','RemoveFormat' ] },
	{ name: 'paragraph', items : [ 'NumberedList','BulletedList','-','Outdent','Indent','-','Blockquote','CreateDiv',
	'-','JustifyLeft','JustifyCenter','JustifyRight','JustifyBlock','-','BidiLtr','BidiRtl' ] },
	{ name: 'links', items : [ 'Link','Unlink','Anchor' ] },
	{ name: 'insert', items : [ 'Image','Flash','Table','HorizontalRule','Smiley','SpecialChar','PageBreak','Iframe' ] },
	'/',
	{ name: 'styles', items : [ 'Styles','Format','Font','FontSize' ] },
	{ name: 'colors', items : [ 'TextColor','BGColor' ] },
	{ name: 'tools', items : [ 'Maximize', 'ShowBlocks','-','About' ] }
 
        ];                                    
 
        // 2. Create a config var to hold your toolbar
        var config =
        {
			height: 180,
			width: 620,			
			position: ['center','center'],
            toolbar_mySimpleToolbar: myToolbar,
            toolbar: 'mySimpleToolbar'        
        };
 
        // 3. change the textarea into an editor using your config and toolbar
        $('#editor_area').ckeditor(config);
		 
		var frame = parent.document.getElementById( window.name );
		$(frame).css('height',550);
		
	
		
		$(document).ready(function() {
	oTable = $('#table_id').dataTable({
		"bJQueryUI": true,
		"sPaginationType": "full_numbers"
	});
} );

 $("#cdm_editor").dialog({autoOpen: false,modal:true,width: 1000,draggable: true, position: top, resizable: true});
 $("#save_button").button();
 $("#cancel_button").button();

$('#table_id').on("click", "tbody tr",function(event){

	var title = $(this).children("td:nth-child(1)").text() + ' - ' + $(this).children("td:nth-child(2)").text()
	$("#cdm_editor").dialog('option', 'title', title);
	$('#editor_area').val('');
	$("#cdm_editor").dialog('open');
});

$("#save_button").on("click",function(event){
	$("#cdm_editor").dialog('close');
	$("#infoMessage").html("Here's an info message with dynamic content.");
	
});

$("#cancel_button").on("click",function(event){
	$("#cdm_editor").dialog('close');
});

$("td:nth-child(4):contains('1')").css("background-image",'url("/library/image/silk/accept.png")');
$("td:nth-child(4):contains('1')").css("background-repeat","no-repeat");
$("td:nth-child(4):contains('0')").css("background-image",'url("/library/image/silk/exclamation.png")');
$("td:nth-child(4):contains('0')").css("background-repeat","no-repeat");





		