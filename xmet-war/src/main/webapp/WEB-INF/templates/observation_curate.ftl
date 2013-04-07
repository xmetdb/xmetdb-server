<#include "/s_html.ftl" >
<head>
<#include "/s_head.ftl" >

<script type='text/javascript' charset='utf8' src='${xmet_root}/jquery/jquery.validate.min.js'></script>
<script type='text/javascript' src='${xmet_root}/jquery/jquery.MultiFile.pack.js'></script>
<script type='text/javascript' charset='utf8' src='${xmet_root}/jquery/jquery.base64.min.js'></script>
<script type='text/javascript' charset='utf8' src='${xmet_root}/jquery/jquery.autogrow.js'></script>
<script  type="text/javascript" src="${xmet_root}/jquery/jquery.imagemapster.min.js"></script>
<script  type="text/javascript" src="${xmet_root}/scripts/jopentox-ui-atoms.js"></script>
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
		$( "#structureSearchResults" ).selectable();
		loadEnzymesList("${xmet_root}","#xmet_enzyme","#xmet_allele");
		$( "#buttonSubstrateDraw" ).click(function() {  toggleDrawUI('substrate','#buttonSubstrateDraw','structure diagram editor and search');  return false; });
		$( "#buttonProductDraw" ).click(function() {  toggleDrawUI('product','#buttonProductDraw','structure diagram editor and search');  return false; });		
		$('form[name="substrateSearchForm"]').removeAttr('onsubmit')
        .submit(function(event){
 			$( "#divresults").show();
			var molFile = document.getElementById("iframeSketcher").contentWindow.getMolecule();
			if ((molFile!==undefined) && (molFile != null)) {
				this.type.value = "mol"; 
				this.b64search.value = $.base64.encode(molFile);
				this.search.value= "";
			} else {
				this.type.value = "smiles"; 
				this.b64search.value = "";
			}
			var queryService = '${queryService}';
			if (this.similarity["checked"]) queryService += "/query/similarity";
			else if (this.smarts["checked"]) queryService += "/query/smarts";
			else queryService += "/query/compound/search/all";
        	runSearch(queryService,$(this),'#structureSearchResults');
            event.preventDefault();
            return false;
        });
        $('#xmet_comment').autogrow();
		//submitFormValidation("#submitForm");
		//searchFormValidation("#substrateSearchForm");
	    jQuery("#breadCrumb ul").append('<li><a href="${xmet_root}/protocol" title="XMetDB observations">Observations</a></li>');
} );

	$(document).ready(function() {    
			curateObservation("${xmet_root}","${xmet_request_json}");
			jQuery("#breadCrumb ul").append('<li id="breadCrumb_xmet_id"></li>');
			jQuery("#breadCrumb ul").append('<li id="breadCrumb_xmet_id_modify"><a href="${xmet_request}" title="Curate an existing observation">Curate</a></li>');
			jQuery("#breadCrumb").jBreadCrumb();
			loadHelp("${xmet_root}","observation_new");
	});    
</script>


</head>
<body>
<div class="container columns" style="margin:0;padding:0;">
	<#include "/s_banner.ftl">
	<#include "/s_menu.ftl">

    <div class="twelve columns remove-bottom" >
    
    <div class="row remove-bottom ui-widget-header ui-corner-top">&nbsp;<span id="xmet_id">New XMetDB observation</span></div>
    <div class="half-bottom ui-widget-content ui-corner-bottom" >
    
   	<form method="POST" action="${xmet_request}?method=PUT" id="submitForm" name="submitForm">

	<div class='row' style="margin:5px;padding:5px;"> 	
	 	<div class='three columns alpha'><label for='xmet_substrate_uri'>Substrate<em></em><a href='#' class='chelp substrate'></a></label>
	 	<br/>
	 	<div id="sim_substrate"></div>
	 	</div>
	    <div class='five columns omega'>
		    <ul class='structresults' id="xmet_substrate_img" style='height:150px;'></ul>
		    <input type="hidden" id="xmet_substrate_atoms" name="xmet_substrate_atoms" value="">
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
			<input type="hidden" id="xmet_product_atoms" name="xmet_product_atoms" value="">
			<input type="hidden" id="xmet_product_uri" name="xmet_product_uri" value="">
			<input type="hidden" id="xmet_product_mol" name="xmet_product_mol" value="">
			<input type="hidden" id="xmet_product_type" name="xmet_product_type" value="uri">
		</div>
	</div>    	
	<div class='row remove-bottom' style="margin:5px;padding:5px;"><hr class='remove-bottom'/></div>
	<div class='row' style="margin:5px;padding:5px;"> 	
 	   <div class='three columns alpha'><label>Atom uncertainty:<a href='#' class='chelp atomuncertainty'></a></label></div>
	   <div class='six columns omega'>
			<select id="xmet_atom_uncertainty" name="xmet_atom_uncertainty" class="remove-bottom" >
			<option value="Certain" selected="selected">Certain</option>
			<option value="Uncertain">Uncertain</option>
			</select>
		</div>
		<div class='three columns alpha'><label>Product amount:<a href='#' class='chelp productamount'></a></label></div>
		 <div class='three columns omega'>
			<select id="xmet_product_amount" name="xmet_product_amount" class="remove-bottom">
			<option value="Major" selected="selected">Major</option>
			<option value="Minor">Minor</option>
			<option value="Unknown">Unknown</option>
			</select>
		 </div>
	</div> 	
	<div class='row remove-bottom' style="margin:5px;padding:5px;"> 	
		<div class='three columns alpha'><label>Experiment:<a href='#' class='chelp experiment'></a></label></div>
		<div class='six columns omega'>
			<select id="xmet_experiment" name="xmet_experiment" class="remove-bottom">
			<option value="MS" selected="selected">MS (Microsomes)</option>
			<option value="HEP">HEP (Hepatocytes)</option>
			<option value="ENZ">ENZ (Enzyme)</option>
			</select>
		</div>
		<div class='seven columns omega'></div>
	</div>
	<div class='row remove-bottom' style="margin:5px;padding:5px;"> 	

		<div class='three columns alpha'><label>Enzyme:<a href='#' class='chelp enzyme'></a></label></div>
		<div class='nine columns omega'>
			<select id="xmet_enzyme" name="xmet_enzyme" class="remove-bottom" style="width:400px;"></select>
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
		<input type="text" name="xmet_reference" id="xmet_reference" title="Enter reference DOI or free text" value="" class="eight columns omega remove-bottom">
		<div class="five columns omega">&nbsp;</div>

	</div>
	<div class='row remove-bottom' style="margin:5px;padding:5px;"> 	
		<div class='three columns alpha'>
			<label>Comment:</label>
		</div>
		<textarea name="xmet_comments" id="xmet_comments" title="Note" value="" row="1" class="eight columns omega remove-bottom"></textarea>
		<input type="submit" class="submit five columns omega" value="Curate observation">
	</div>			
	</form>	
    </div>

	<!-- searchUI starts 
	<div id="searchUI" class="structresults remove-bottom" style='width:100%;display:none;'></div>
	-->
	<br/>

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
