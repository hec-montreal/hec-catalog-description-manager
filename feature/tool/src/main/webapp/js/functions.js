$.fn.dataTableExt.oApi.fnReloadAjax = function ( oSettings, sNewSource, fnCallback, bStandingRedraw )
{
    if ( typeof sNewSource != 'undefined' && sNewSource != null )
    {
        oSettings.sAjaxSource = sNewSource;
    }
    this.oApi._fnProcessingDisplay( oSettings, true );
    var that = this;
    var iStart = oSettings._iDisplayStart;
    var aData = [];
  
    this.oApi._fnServerParams( oSettings, aData );
      
    oSettings.fnServerData( oSettings.sAjaxSource, aData, function(json) {
        /* Clear the old information from the table */
        that.oApi._fnClearTable( oSettings );
          
        /* Got the data - add it to the table */
        var aData =  (oSettings.sAjaxDataProp !== "") ?
            that.oApi._fnGetObjectDataFn( oSettings.sAjaxDataProp )( json ) : json;
          
        for ( var i=0 ; i<aData.length ; i++ )
        {
            that.oApi._fnAddData( oSettings, aData[i] );
        }
          
        oSettings.aiDisplay = oSettings.aiDisplayMaster.slice();
        that.fnDraw();
          
        if ( typeof bStandingRedraw != 'undefined' && bStandingRedraw === true )
        {
            oSettings._iDisplayStart = iStart;
            that.fnDraw( false );
        }
          
        that.oApi._fnProcessingDisplay( oSettings, false );
          
        /* Callback user function - for event handlers etc */
        if ( typeof fnCallback == 'function' && fnCallback != null )
        {
            fnCallback( oSettings );			
        }
    }, oSettings );
};



function initDescriptionTable() {
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

function save(description,id) {
		
	$.ajax({
		url : 'save.json',
		type: "POST",
		data : 'description=' + description + '&id=' + id,
		datatype : 'json',
		success : function(data) {		
			$('#ajaxMessage').html(data.message);
			$('#ajaxReturn').addClass("info");		
			$('#ajaxReturn').show();	
			$('#ajaxReturn').fadeOut( 4000); 
			oTable.fnReloadAjax(null, function() { 
   initDescriptionTable();
}, null);			
		}
	});
}

function openDialogCatalogDescriptionCurrentRow(id) {
	$.ajax({
		url : 'get.json',
		data : 'id=' + id,
		datatype : 'json',
		success : function(cd) {
			var dialog_title = cd.courseid  + ' - ' + cd.title;
			var description = cd.description; 
			$("#cdm_editor").dialog('option', 'title', dialog_title);
			$('#editor_area').val(description);
			$("#cdm_editor").dialog('open');
		}
	});	
}