<script type='text/javascript' src='${xmet_root}/jme/jme.js'></script>

<script type="text/javascript">


$(document).ready(function() {
	loadEnzymesList("#search_enzyme");
});

	$(function() {
		$( "input:submit, a, button", ".search" ).button();
	});
	
function loadEnzymesList(selectTag) {
		  //clear the list	
		  $(selectTag).html("");
		  //get all enzymes
	      $.ajax({
	          dataType: "json",
	          url: "/xmetdb/catalog?media=application%2Fjson",
	          success: function(data, status, xhr) {
	          	  data.sort(function sortfunction(a,b){
	          		  if(a.code == b.code){
		          			return 0;
		          	  }
 	          		  return (a.code < b.code) ? -1 : 1;
				  });
	        	  data.forEach(function enz(element, index, array) {
	        		  $("<option value='" + element.code + "'>" + element.code + "&nbsp;" +element.name + "</option>").appendTo(selectTag);
	        	  });
	          },
	          error: function(xhr, status, err) { },
	          complete: function(xhr, status) { }
	       });
}	
</script>