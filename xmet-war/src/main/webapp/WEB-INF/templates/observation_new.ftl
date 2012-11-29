<#include "/html.ftl" >
<head>
<#include "/head.ftl" >
<link href="/xmetdb/style/form.css" rel="stylesheet" type="text/css">
<script type='text/javascript' src='/xmetdb/jme/jme.js'></script>
<script type='text/javascript' src='/xmetdb/jquery/jquery.MultiFile.pack.js'></script>
<style>
    .structresults .ui-selecting { background: #FECA40; border-color: #FECA40; }
    .structresults .ui-selected { background: #F39814; border-color: #F39814; }
    .structresults { list-style-type: none; margin: 0; padding: 0; width: 930px; }
    .structresults li { margin: 3px; padding: 1px; float: left; width: 155px; height: 155px; font-size: 4em; text-align: center; }
</style>


<script type="text/javascript">

$(document).ready(function() {
		$( ".useSelected" ).button();
		loadEnzymesList("#xmet_enzyme");
		addSearchUI('substrate','${xmet_root}');
		$( "#buttonSubstrateSearch" ).click(function() {  toggleSearchUI('#searchUI','.buttonSearch','search options');  return false; });
		$( "#buttonProductSearch" ).click(function() {  toggleSearchUI('#searchUI','.buttonSearch','search options');  return false; });
		$( "#buttonSubstrateDraw" ).click(function() {  toggleDrawUI('substrate','.buttonDraw','structure diagram editor');  return false; });
		$( "#buttonProductDraw" ).click(function() {  toggleDrawUI('product','.buttonDraw','structure diagram editor');  return false; });		
		$('form[name="substrateSearchForm"]').removeAttr('onsubmit')
        .submit(function(event){
        	runSearch('${queryService}',$(this),'#structureSearchResults');
            event.preventDefault();
            return false;
        });
} );

</script>

</head>
<body>
	<div class='w_wrap'>
	<#include "/banner.ftl">
	<#include "/menu.ftl">

	<div class='w_content ui-widget ' style='margin-top: 25px; margin-left: 0px;  margin-right: 10px; padding: 0 .1em;'>
	
	<div class='ui-widget-header ui-corner-top'><p>&nbsp;New XMETDB observation <span id="xmet_id"></span></p></div>
	
	
<!-- the real thing -->
<form method="POST" action="/xmetdb/protocol" id="submitForm" name="submitForm" ENCTYPE="multipart/form-data">
<div class='ui-widget-content ui-corner-bottom' style='margin-top: 0px;margin-bottom: 0px;' />
		
	<p><label for="xmet_substrate">Substrate:<em>*</em></label>
			<ul class='structresults' id="xmet_substrate_img" style='height:150px;'></ul>
			<input type="hidden" id="xmet_substrate_uri" name="xmet_substrate_uri" value="">
			<input type="hidden" id="xmet_substrate_mol" name="xmet_substrate_mol" value="">
	</p>
	<p><label>&nbsp;</label>
		<span >
				<a class='button buttonDraw' href="#" id='buttonSubstrateDraw' title='Launches structure diagram editor'>Show structure diagram editor</a>
				&nbsp;|&nbsp;
				<a class='button buttonSearch' href="#" id="buttonSubstrateSearch">Show search options</a>
				&nbsp;|&nbsp;
				Upload <input type='file' maxlength='1' accept='sdf|mol|csv|xls' name='xmet_substrate_upload' title='Substrate upload' size='20'>
		</span>
	</p>
	<p><label for="xmet_product">Product:<em>*</em></label>
			<ul class='structresults' id="xmet_product_img" style='height:150px;'></ul>
			<input type="hidden" id="xmet_product_uri" name="xmet_product_uri" value="">
			<input type="hidden" id="xmet_product_mol" name="xmet_product_mol" value="">
	</p>
	<p><label>&nbsp;</label>
			<span >
					<a class='button buttonDraw' href="#" id='buttonProductDraw'  title='Launches structure diagram editor'>Show structure diagram editor</a>
					&nbsp;|&nbsp;
					<a class='button buttonSearch' href="#" id="buttonProductSearch">Show search options</a>
					&nbsp;|&nbsp;
					Upload <input type='file' maxlength='1' accept='sdf|mol|csv|xls' name='xmet_product_upload' title='Product upload' size='20'>
			</span>			
	</p>	
			
   <p><label for="xmet_atom_uncertainty">Atom uncertainty:<em>*</em></label>
			<select id="xmet_atom_uncertainty" name="xmet_atom_uncertainty" >
			<option value="Certain" selected="selected">Certain</option>
			<option value="Uncertain">Uncertain</option>
			</select>
	</p>	
		<!-- Product amount -->
	<p><label for="xmet_product_amount">Product amount:<em>*</em></label>
		<select id="xmet_product_amount" name="xmet_product_amount">
			<option value="Major" selected="selected">Major</option>
			<option value="Minor">Minor</option>
			<option value="Unknown">Unknown</option>
		</select>
	</p>
	<!-- Experiment -->
	<p><label for="xmet_experiment">Experiment:<em>*</em></label>
		<select id="xmet_experiment" name="xmet_experiment">
			<option value="MS" selected="selected">MS (Microsomes)</option>
			<option value="HEP">HEP (Hepatocytes)</option>
			<option value="ENZ">ENZ (Enzyme)</option>
		</select>
	</p>
	<!-- Enzymes -->
	<p><label for="xmet_enzyme">Enzyme:<em>*</em></label>
		<select id="xmet_enzyme" name="xmet_enzyme">
		</select>
		Allele
	</p>
	<!-- References -->
	<p><label for="xmet_reference">Reference:<em>*</em></label>
		<input type="text" name="xmet_reference" size='60' title="DOI or free text?" value="">
	</p>
	<!-- Submit --> 
	<p><label >&nbsp;</em></label>
		<input type="submit" class="submit" value="Submit observation">
	</p>
</p>
</div>
	
</form>	
	<br/>
	<div id="searchUI" class="structresults" style='width:100%;display:none;'></div>
	<br/>
	<!-- Structure diagram editor -->
	<div id="drawUI" style='width:100%;display:none;'>
	<div class='ui-widget-header ui-corner-top'><p>Structure diagram editor<span style='float:right;'>
	<a href='#' onClick='useDrawn("${queryService}","substrate");return false;'>Use the structure as a substrate</a>
	&nbsp;|&nbsp;
	<a href='#' onClick='useDrawn("${queryService}","product");return false;'>Use the structures as a product</a>&nbsp;
	</span></p></div>
	<div class='ui-widget-content' style='margin:5 5 5 5;padding: 5 5 5 5;'>
	&nbsp;
	<applet code="JME.class" name="JME" archive="/xmetdb/jme/JME.jar" width="500px" height="400px">
	<param name="options" value="nohydrogens,polarnitro,nocanonize">
	You have to enable Java and JavaScript on your machine ! 
	</applet>
	
	</div> 
	
	<div class='ui-widget-header ui-corner-bottom'><p>&nbsp;<span style='float:right;'>
	<a href='#' onClick='useDrawn("${queryService}","substrate");return false;'>Use the structure as a substrate</a>
	&nbsp;|&nbsp;
	<a href='#' onClick='useDrawn("${queryService}","product");return false;'>Use the structure as a product</a>&nbsp;
	</span></p></div>	
	
	</div> <!-- drawUI -->
	
<!-- Footer and the like -->

	</div> <#-- w_content -->

	<#-- Prevents the footer from hiding the last line on smaller screens. -->
	<div class='pusher'></div>

	
	</div> <#-- w_wrap -->
	
<#include "/footer.ftl">
<#include "/scripts-welcome.ftl">
</body>
</html>