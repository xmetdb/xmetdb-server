<script type='text/javascript' src='${xmet_root}/jme/jme.js'></script>
<script type='text/javascript' charset='utf8' src='/xmetdb/jquery/jquery.validate.min.js'></script>
<script type="text/javascript">


$(document).ready(function() {
	loadEnzymesList("#search_enzyme");
	searchFormValidation("#search_form");
});

	$(function() {
		$( "input:submit, a, button", ".search" ).button();
	});
	

</script>