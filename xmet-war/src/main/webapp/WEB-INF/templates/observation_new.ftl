<#include "/s_html.ftl" >
<head>
<#include "/s_head.ftl" >
<script type='text/javascript' src='${xmet_root}/scripts/jcompound.js'></script>
<script type='text/javascript' charset='utf8' src='${xmet_root}/jquery/jquery.validate.min.js'></script>
<script type='text/javascript' src='${xmet_root}/jquery/jquery.MultiFile.pack.js'></script>
<script type='text/javascript' charset='utf8' src='${xmet_root}/jquery/jquery.base64.min.js'></script>
<script type='text/javascript' charset='utf8' src='${xmet_root}/jquery/jquery.autogrow.js'></script>
<script  type="text/javascript" src="${xmet_root}/jquery/jquery.imagemapster.min.js"></script>
<script  type="text/javascript" src="${xmet_root}/scripts/jopentox-ui-atoms.js"></script>
<script type='text/javascript' src='${xmet_root}/jquery/jquery.jeditable.js'></script>


<script type="text/javascript">

function atomNumber(num, atoms_id) {
	return false;
}	
$(document).ready(function() {
		$( ".useSelected" ).button();
		$( "#structureSearchResults" ).selectable();
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
			var query = '';
			if (this.similarity["checked"]) query += "/query/similarity";
			else if (this.smarts["checked"]) query += "/query/smarts";
			else query += "/query/compound/search/all";
        	runSearch('${queryService}',query,$(this),'#structureSearchResults');
            event.preventDefault();
            return false;
        });
        $('#xmet_comments').autogrow();
		//submitFormValidation("#submitForm");
		//searchFormValidation("#substrateSearchForm");
	    jQuery("#breadCrumb ul").append('<li><a href="${xmet_root}/protocol" title="XMetDB observations">Observations</a></li>');
} );

    <#switch xmet_mode>
    <#case "newdocument">
    	$(document).ready(function() {    
    		loadEnzymesList("${xmet_root}","#xmet_enzyme","#xmet_allele","","");
    		jQuery("#breadCrumb ul").append('<li><a href="${xmet_root}/editor" title="Create new observation">New observation</a></li>');
    		jQuery("#breadCrumb").jBreadCrumb();
    		loadHelp("${xmet_root}","observation_new");
    	});    
    	<#break>
    <#case "update">
		$(document).ready(function() {    
			editObservation("${xmet_root}","${xmet_request_json}","${queryService}");
			jQuery("#breadCrumb ul").append('<li id="breadCrumb_xmet_id"></li>');
			jQuery("#breadCrumb ul").append('<li id="breadCrumb_xmet_id_modify"><a href="${xmet_request}" title="Modify an existing observation">Modify</a></li>');
			jQuery("#breadCrumb").jBreadCrumb();
			loadHelp("${xmet_root}","observation_new");
			$('#download').html(getDownloadLinksObservation("${xmet_root}","${xmet_request}",true));
		});    
    	<#break>
    <#default>

    </#switch>
</script>


</head>
<body>
<div class="container columns" style="margin:0;padding:0;">
	<#include "/s_banner.ftl">
	<#include "/s_menu.ftl">

    <div class="twelve columns add-bottom" >
    
    <div class="row remove-bottom ui-widget-header ui-corner-top">&nbsp;<span id="xmet_id">New XMetDB observation</span></div>
    <div class="half-bottom ui-widget-content ui-corner-bottom" >
    
    <#switch xmet_mode>
    <#case "newdocument">
    	<form method="POST" action="${xmet_root}/protocol" id="submitForm" name="submitForm" ENCTYPE="multipart/form-data">
    	<#break>
    <#case "update">
    	<form method="POST" action="${xmet_request}?method=PUT" id="submitForm" name="submitForm" ENCTYPE="multipart/form-data">
    	<#break>
    <#default>
    	<form method="GET" action="${xmet_root}/protocol" id="submitForm" name="submitForm" ENCTYPE="multipart/form-data"> 
    </#switch>

	<div class='row' style="margin:5px;padding:5px;"> 	
	 	<div class='three columns alpha'><label for='xmet_substrate_uri'>Substrate<em></em><a href='#' class='chelp substrate'></a></label>
	 	<br/>
	 	<div id="sim_substrate"></div>
	 	</div>
	    <div class='five columns omega'>
		    <ul class='structresults' id="xmet_substrate_img" style='height:150px;'></ul>
		    <br/>
			<input type="hidden" id="xmet_substrate_uri" name="xmet_substrate_uri" value="">
			<input type="hidden" id="xmet_substrate_mol" name="xmet_substrate_mol" value="">
			<input type="hidden" id="xmet_substrate_type" name="xmet_substrate_type" value="uri">
			<input type="hidden" id="xmet_substrate_name" name="xmet_substrate_name" value="">
	    </div>
		<div class='three columns omega'><label for='xmet_product_uri'>Product<em></em><a href='#' class='chelp product'></a></label>
		<br/>
		<div id="sim_product"></div>
		</div>
	    <div class='five columns omega'>
			<ul class='structresults' id="xmet_product_img" style='height:150px;'></ul>
			<br/>
			<input type="hidden" id="xmet_product_uri" name="xmet_product_uri" value="">
			<input type="hidden" id="xmet_product_mol" name="xmet_product_mol" value="">
			<input type="hidden" id="xmet_product_type" name="xmet_product_type" value="uri">
			<input type="hidden" id="xmet_product_name" name="xmet_product_name" value="">
		</div>
	</div>    	
	<div class='row  remove-bottom' style="margin:5px;padding:5px;"> 	
	    <div class='eight columns omega '>
	    	<label for='xmet_substrate_upload'><em></em></label>
			<a href="#" id='buttonSubstrateDraw' title='Launches structure diagram editor'>Show structure diagram editor and search options</a><a href='#' class='chelp search'></a>
			<br/>
			Upload <input type='file' maxlength='1' accept='sdf|mol|csv|xls' name='xmet_substrate_upload' title='Substrate upload' size='20' class='remove-bottom' onClick='clearStructure("substrate")'>
	    </div>
	    <div class='eight columns omega'>
	    	<label for='xmet_product_upload'><em></em></label>
			<a href="#" id='buttonProductDraw'  title='Launches structure diagram editor'>Show structure diagram editor and search options</a><a href='#' class='chelp search'></a>
			<br/>
			Upload <input type='file' maxlength='1' accept='sdf|mol|csv|xls' name='xmet_product_upload' title='Product upload' size='20' class='remove-bottom' onClick='clearStructure("product")'>
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
		<input type="text" name="xmet_reference" id="xmet_reference" title="Enter reference DOI (doi:XYZ) or PubMed ID (pmid:123) or free text" value="" class="eight columns omega remove-bottom">
		<div class="five columns omega">&nbsp;</div>

	</div>
	<div class='row remove-bottom' style="margin:5px;padding:5px;"> 	
		<div class='three columns alpha'>
			<label>Comment:</label>
		</div>
		<textarea name="xmet_comments" id="xmet_comments" title="Note" value="" row="1" class="eight columns omega remove-bottom"></textarea>
		<div class='five columns omega'>&nbsp;</div>
	</div>
	<div class='row half-bottom' style="margin:5px;padding:5px;"> 	
		<div class='three columns alpha'>
			<label>Status:<a href='#' class='chelp hcurator'></a></label></label>
		</div>
		<div class='two columns omega' id="xmet_curated">Not curated</div>
		<div class="two columns omega"><img id='imgcurated' style='display:none;' src='${xmet_root}/images/star.png'>&nbsp;</div>
		<div class='two columns omega'>&nbsp;</div>
		<div class='five columns omega'><a href='#' title='Links' id='xmet_links'></a></div>
	</div>		
	
	<div class='row remove-bottom' style="margin:5px;padding:5px;"><hr class='remove-bottom'/></div>
	
	<div class='row remove-bottom' style="margin:5px;padding:5px;"> 	
		<div class='eleven columns alpha'>&nbsp;</div>
		<input type="submit" class="submit four columns omega" value="Submit observation">
		<div class='one column omega'>&nbsp;</div>
	</div>

	</form>	
    </div>

	<!-- searchUI starts 
	<div id="searchUI" class="structresults remove-bottom" style='width:100%;display:none;'></div>
	-->
	<br/>
	<!-- Structure diagram editor -->
	<div id="drawUI" class="remove-bottom" style='display:none;'>
		<div class='row ui-widget-header ui-corner-top remove-bottom'>Draw the structure or search by structure<span style='float:right;' class='ui-icon ui-icon-closethick' title='Click to close this window' onClick='$("#drawUI").hide();'></span></div>
		<div class='ui-widget-content ui-corner-bottom half-bottom'>
			<div class='row remove-bottom'  style="margin:5px;padding:5px;"> 	
				<form method='GET' action='#' name='substrateSearchForm'>
				<iframe class="twelve columns alpha remove-bottom" style='height:420px;width:500px;' id="iframeSketcher" src="${xmet_root}/scripts/sketcher_2D.html" onLoad="iframeOnLoad()"></iframe>
				<div class='four columns omega remove-bottom'>
					<br/>
	   				<input type='hidden' name='type' value='smiles'>
	   				<input type='hidden' name='b64search' value=''>
					<label>Search options</label>
					<br/>
					<input type='radio' id='auto' value='auto' name='option'  title='Exact structure or search by identifier' size='20' tabindex='2'/>Auto
					</br>
	      		    <input type='radio' name='option' id='similarity' value='similarity' checked title='Enter SMILES or draw structure' tabindex='3'/>Similarity
	      		    </br>
	      		    <input type='radio' name='option' id="smarts" value='smarts' title='Enter or draw a SMARTS query' tabindex='5' size='40'/>Substructure
					<br/>
	    		 	<select title ='Tanimoto similarity threshold' name='threshold' style='width:6em;' tabindex='4'><option value='0.9'>0.9</option><option value='0.8'>0.8</option><option value='0.7'>0.7</option><option value='0.6'>0.6</option><option value='0.5' selected >0.5</option><option value='0.4' >0.4</option><option value='0.3' >0.3</option><option value='0.2' >0.2</option></select>
	    		 	<label>Chemical identifier</label>
	    		 	<br/>
	    		 	<input type='text' name='search' size='60' value='' tabindex='1' title='Enter any chemical compound identifier (CAS, Name, EINECS, SMILES or InChI). The the input type is guessed automatically.'>
	    		 	<input type="submit" class="search" value="Structure search" tabindex='13'>

				</div>
				</form>
			</div>
			<div class='row'  style="margin:5px;padding:5px;">
				<div class="five columns alpha remove-bottom">
					<label>Enter chemical name of the drawn structure</label>
					<input type="text" name="xmet_name" id="xmet_name" value="">
				</div>
				<div class="five columns omega remove-bottom">
					<label>Use the drawn structure</label>
					<a href='#' class='button remove-bottom' onClick='useDrawn("${queryService}","substrate");return false;'>as a substrate</a>
					&nbsp;
					<a href='#' class='button ' onClick='useDrawn("${queryService}","product");return false;'>as a product</a>&nbsp;
				</div>				
				<div class='six columns omega remove-bottom'>&nbsp;</div>
			</div>
			<div id='divresults' class='remove-bottom' style="display:none;margin:5px;padding:5px;" >
				<hr class='row remove-bottom'/>
				<div class='row remove-bottom'>
					<div class='ten columns alpha' style='width:500px;'><label>Search results</label><span class='details'>Click on the structure diagram to select the structure.<br>Ctrl + click to select multiple structures.</span></div>
					<div class='six columns omega'>
						<label>Use selected structures as </label> 
						<a href='#' class='button' onClick='useSelected("substrate");return false;'>a substrate</a>
						<a href='#' class='button' onClick='useSelected("product");return false;'>a product(s)</a>
					</div>
				</div>
				<div class='row'>
					<div class='sixteen columns alpha'>
						<ul id='structureSearchResults' class='structresults remove-bottom'></ul>
					</div>
				</div>
			</div>
		</div>
	</div> 
	<!-- drawUI end -->

	<!-- middle panel  twelve columns-->
	</div>

<!-- Footer and the like -->

	<!-- Right column and footer
	================================================== -->
	<#include "/s_help.ftl">
	
</div><!-- container -->

		<#include "/ga.ftl">
</body>
</html>
