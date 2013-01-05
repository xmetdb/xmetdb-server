<script type='text/javascript' src='${xmet_root}/scripts/myprofile.js'></script>
<script type='text/javascript' src='${xmet_root}/jquery/jquery.validate.min.js'></script>

<script type='text/javascript'>
$(document).ready(function() {
	getMyAccount('${xmet_root}','${xmet_request_json}',true);
	
		// validate the comment form when it is submitted
	$("#form_myaccount").validate({
		rules : {
			'firstname': {
				required : true
			},
			'lastname': {
				required : true
			},
			'email': {
				required : true,
				email: true
			},
			'homepage': {
				url: true
			}			
			
		},
		messages : {
			'firstname' : "Please provide your first name",
			'lastname'   : {
				required: "Please provide your last name"
			},
			'email'   : {
				required: "Please provide e-mail",
				email: "Please provide valid e-mail"
			},
			'homepage'   : {
				url: "Please provide valid web address"
			}
		}
	});
});
</script>