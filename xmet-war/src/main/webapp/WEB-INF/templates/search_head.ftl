<!-- JME applet -->
<script type='text/javascript' src='${xmet_root}/jme/jme.js'></script>
<script type='text/javascript' charset='utf8' src='${xmet_root}/jquery/jquery.validate.min.js'></script>
<script type='text/javascript' charset='utf8' src='${xmet_root}/jquery/jquery.base64.min.js'></script>
<script type="text/javascript">


$(document).ready(function() {
	loadEnzymesList("${xmet_root}","#xmet_enzyme","#xmet_allele");
	searchFormValidation("#search_form");
});

	$(function() {
		$( "input:submit, a, button", ".search" ).button();
	});


</script>