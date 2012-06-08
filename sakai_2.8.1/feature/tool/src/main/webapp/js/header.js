function doAjax() {
      $.ajax({
        url: 'testAjax.htm',
        datatype:   'json',
        success: function(data) {
          $('#ajaxMessage').html(data.message);
        }
      });
    }

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