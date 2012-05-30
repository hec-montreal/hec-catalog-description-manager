function doAjax() {
      $.ajax({
        url: 'testAjax.htm',
        datatype:   'json',
        success: function(data) {
          $('#ajaxMessage').html(data.message);
        }
      });
    }