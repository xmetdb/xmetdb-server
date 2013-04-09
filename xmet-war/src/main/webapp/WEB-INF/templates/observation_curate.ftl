<#include "/s_html.ftl" >
<head>
<#include "/s_head.ftl" >


<script type='text/javascript' charset='utf8' src='${xmet_root}/jquery/jquery.validate.min.js'></script>
<script type='text/javascript' charset='utf8' src='${xmet_root}/jquery/jquery.base64.min.js'></script>
<script  type="text/javascript" src="${xmet_root}/jquery/jquery.imagemapster.min.js"></script>
<script  type="text/javascript" src="${xmet_root}/scripts/jopentox-ui-atoms.js"></script>
<script type='text/javascript' src='${xmet_root}/jquery/jquery.jeditable.js'></script>
<style>
    .structresults .ui-selecting { background: #FECA40; border-color: #FECA40; }
    .structresults .ui-selected { background: #F39814; border-color: #F39814; }
    .structresults { list-style-type: none; margin: 0; padding: 0; width: 930px; }
    .structresults li { margin: 3px; padding: 1px; float: left; width: 155px; height: 155px; font-size: 4em; text-align: center; }
</style>

<script type="text/javascript">
function atomNumber(num, atoms_id) {
    var index = _xmet.atoms[atoms_id].indexOf(num);
    if (index>=0) _xmet.atoms[atoms_id].splice(index,1);
    else _xmet.atoms[atoms_id].push(num);
    $(atoms_id).text(_xmet.atoms[atoms_id]);
}	    
$(document).ready(function() {
		$( ".useSelected" ).button();
	    jQuery("#breadCrumb ul").append('<li><a href="${xmet_root}/protocol" title="XMetDB observations">Observations</a></li>');
} );

	$(document).ready(function() {    
        $('#xmet_reference').editable(
        	'${xmet_request}?method=put',{
        	cssclass : '',
        	style   : 'fill:auto;',
        	cancel  : 'Cancel',
        	submit  : 'Update',
        	height  : '1em',
        	indicator : '<img src="${xmet_root}/images/progress.gif">',
        	tooltip  : 'Click to edit...',
        	callback : function(value, settings) {
        		$('#imgcurated').show();
        		$('#curated').html("Curated");
            }
        });        
        $('#xmet_comments').editable(
        	'${xmet_request}?method=put',{
        	type	: 'textarea',
        	cancel  : 'Cancel',
        	submit  : 'Update',
        	indicator : '<img src="${xmet_root}/images/progress.gif">',
        	tooltip  : 'Click to edit...',
        	callback : function(value, settings) {
        		$('#imgcurated').show();
        		$('#curated').html("Curated");
            }
        });
		$('#curated').editable(
			 '${xmet_request}?method=put',{ 
		     data   : " {true:'Curated',false:'Not curated'}",
		     type   : 'select',
		     submit : 'Update',
		     cancel  : 'Cancel',
		     indicator : '<img src="${xmet_root}/images/progress.gif">',
        	 tooltip  : 'Click to edit...',
        	 callback : function(value, settings) {
        	     if ("Curated"==value) {
        	    	 $('#imgcurated').show();
        	     } else $('#imgcurated').hide();
             },
             data: function(value, settings) {
                 //var retval = value.replace(/<br[\s\/]?>/gi, '\n');
                 return value;
              }             
		 });  
		 curateObservation("${xmet_root}","${xmet_request_json}");
		
		 $('#xmet_substrate_atoms').editable(
	        	'${xmet_request}?method=put',{
	        	type	: 'text',
	        	cancel  : 'Cancel',
	        	submit  : 'Update',
	        	indicator : '<img src="${xmet_root}/images/progress.gif">',
	        	tooltip  : 'Click to edit...',
	        	submitdata :  function(value,settings) { 
	        		var data = {};
	        		data['compound_uri'] = $('#xmet_substrate_uri').val(); 
	        		return data;
	        	},
	        	callback : function(value, settings) {
	        		$('#imgcurated').show();
	        		$('#curated').html("Curated");
	            },
	            data: function(value, settings) {
	                 //var retval = value.replace(/<br[\s\/]?>/gi, '\n');
	                 return value;
	            } 	            
	        });
		 $('#xmet_product_atoms').editable(
		        	'${xmet_request}?method=put',{
		        	type	: 'text',
		        	cancel  : 'Cancel',
		        	submit  : 'Update',
		        	indicator : '<img src="${xmet_root}/images/progress.gif">',
		        	tooltip  : 'Click to edit...',
		        	submitdata : function(value,settings) { 
		        		var data = {};
		        		data['compound_uri'] = $('#xmet_product_uri').val(); 
		        		return data;
		        	},
		        	callback : function(value, settings) {
		        		$('#imgcurated').show();
		        		$('#curated').html("Curated");
		            }
		        });
        			
			jQuery("#breadCrumb ul").append('<li id="breadCrumb_xmet_id"></li>');
			jQuery("#breadCrumb ul").append('<li id="breadCrumb_xmet_id_modify"><a href="${xmet_request}" title="Curate an existing observation">Curate</a></li>');
			jQuery("#breadCrumb").jBreadCrumb();
			loadHelp("${xmet_root}","observation_curate");
			$('#download').html(getDownloadLinksObservation("${xmet_root}","${xmet_request}"));
	});   
	
	function getCompoundURI(selector) {
		var data = {};
		data['compound_uri'] = $(selector).val(); 
		return data;
	}

</script>


</head>
<body>
<div class="container columns" style="margin:0;padding:0;">
	<#include "/s_banner.ftl">
	<#include "/s_menu.ftl">

    <div class="twelve columns remove-bottom" >
    
    <div class="row remove-bottom ui-widget-header ui-corner-top">&nbsp;<span id="xmet_id">New XMetDB observation</span></div>
    <div class="half-bottom ui-widget-content ui-corner-bottom" >
    
	<div class='row' style="margin:5px;padding:5px;"> 	
	 	<div class='three columns alpha'><label for='xmet_substrate_uri'>Substrate<em></em><a href='#' class='chelp substrate'></a></label>
	 	<br/>
	 	<div id="sim_substrate"></div>
	 	</div>
	    <div class='five columns omega'>
		    <ul class='structresults' id="xmet_substrate_img" style='height:150px;'></ul>
		    <br/>
		    <div id="xmet_substrate_atoms" class='ui-state-default'></div>
			<input type="hidden" id="xmet_substrate_uri" name="xmet_substrate_uri" value="">
			<input type="hidden" id="xmet_substrate_mol" name="xmet_substrate_mol" value="">
			<input type="hidden" id="xmet_substrate_type" name="xmet_substrate_type" value="uri">
	    </div>
		<div class='three columns omega'><label for='xmet_product_uri'>Product<em></em><a href='#' class='chelp product'></a></label>
		<br/>
		<div id="sim_product"></div>
		</div>
	    <div class='five columns omega'>
			<ul class='structresults' id="xmet_product_img" style='height:150px;'></ul>
			<br/>
			<div id="xmet_product_atoms" class='ui-state-default'></div>
			<input type="hidden" id="xmet_product_uri" name="xmet_product_uri" value="">
			<input type="hidden" id="xmet_product_mol" name="xmet_product_mol" value="">
			<input type="hidden" id="xmet_product_type" name="xmet_product_type" value="uri">
		</div>
	</div>    	
	<div class='row remove-bottom' style="margin:5px;padding:5px;"><hr class='remove-bottom'/></div>
	<div class='row' style="margin:5px;padding:5px;"> 	
 	   <div class='three columns alpha'><label>Atom uncertainty:<a href='#' class='chelp atomuncertainty'></a></label></div>
	   <div class='six columns omega'>
			<span id="xmet_atom_uncertainty">&nbsp;</span>
		</div>
		<div class='three columns alpha'><label>Product amount:<a href='#' class='chelp productamount'></a></label></div>
		 <div class='three columns omega'>
			<div id="xmet_product_amount">&nbsp;</div>
		 </div>
	</div> 	
	<div class='row remove-bottom' style="margin:5px;padding:5px;"> 	
		<div class='three columns alpha'><label>Experiment:<a href='#' class='chelp experiment'></a></label></div>
		<div class='six columns omega'>
			<span id="xmet_experiment">&nbsp;</span>
		</div>
		<div class='seven columns omega'></div>
	</div>
	<div class='row remove-bottom' style="margin:5px;padding:5px;"> 	

		<div class='three columns alpha'><label>Enzyme:<a href='#' class='chelp enzyme'></a></label></div>
		<div class='nine columns omega'>
			<span id="xmet_enzyme">&nbsp;</span>
		</div>
		<div class='two columns omega'><label>Allele:<a href='#' class='chelp allele'></a></label></div>
		<div class='two columns omega'>
			<select id="xmet_allele" name="xmet_allele" class="remove-bottom" ></select>
		</div>
	</div>
	<div class='row remove-bottom' style="margin:5px;padding:5px;"> 	
		<div class='three columns alpha'>
			<label>Reference:<a href='#' class='chelp reference'></a></label>
			<input type="hidden" name="published_status"  value="on">
		</div>
		<div class="ten columns omega remove-bottom ui-state-default" id="xmet_reference" title="Enter reference DOI or free text"> </div>
		<div class="three columns omega">&nbsp;</div>

	</div>
	<div class='row remove-bottom' style="margin:5px;padding:5px;"> 	
		<div class='three columns alpha'>
			<label>Comment:</label>
		</div>
		<div name="xmet_comments" id="xmet_comments" title="Note" value="" row="1" class="ten columns omega remove-bottom ui-state-default"> </div>
	</div>			
	<div class='row remove-bottom' style="margin:5px;padding:5px;"> 	
		<div class='three columns alpha'>
			<label>Status</label>
		</div>
		<div name="curated" id="curated" title="Curation status" row="1" class="three columns omega remove-bottom ui-state-default">
		</div>
		<div class="one column omega"><img id='imgcurated' style='display:none;' src='${xmet_root}/images/star.png'></div>
		<div class="nine columns omega">&nbsp;</div>
	</div>		
    </div>

	<!-- middle panel  twelve columns-->
	</div>


<!-- Footer and the like -->

	<!-- Right column and footer
	================================================== -->
	<#include "/s_help.ftl">
	<#include "/s_footer.ftl">
	
</div><!-- container -->

	<#include "/scripts-welcome.ftl">
</body>
</html>
