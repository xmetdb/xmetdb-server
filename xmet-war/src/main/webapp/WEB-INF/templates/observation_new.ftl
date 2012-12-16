<#include "/s_html.ftl" >
<head>
<#include "/s_head.ftl" >
<script type='text/javascript' charset='utf8' src='${xmet_root}/jquery/jquery.validate.min.js'></script>
<script type='text/javascript' src='${xmet_root}/jme/jme.js'></script>
<script type='text/javascript' src='${xmet_root}/jquery/jquery.MultiFile.pack.js'></script>
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
		submitFormValidation("#submitForm");
} );

</script>


</head>
<body>
<div class="container columns" style="margin:0;padding:0;">
	<#include "/s_banner.ftl">
	<#include "/s_menu.ftl">

    <div class="twelve columns remove-bottom" >
    
    <div class="row remove-bottom ui-widget-header ui-corner-top">&nbsp;New XMETDB observation <span id="xmet_id"></span></div>
    <div class="half-bottom ui-widget-content ui-corner-bottom" >
    
    <form method="POST" action="/xmetdb/protocol" id="submitForm" name="submitForm" ENCTYPE="multipart/form-data">
    
	<div class='row' style="margin:5px;padding:5px;"> 	
	    <div class='three columns alpha'>
	    	Substrate:<em>*</em>
	    </div>
	    <div class='eight columns omega'>
		<!-- the substrate -->
			<ul class='structresults' id="xmet_substrate_img" style='height:150px;'></ul>
			<input type="hidden" id="xmet_substrate_uri" name="xmet_substrate_uri" value="">
			<input type="hidden" id="xmet_substrate_mol" name="xmet_substrate_mol" value="">
		</div>
		<div class='five columns omega'>
			<a href="#" id='buttonSubstrateDraw' title='Launches structure diagram editor'>Show structure diagram editor</a>
			<br>	
			<a href="#" id="buttonSubstrateSearch">Show search options</a>
			<br/>
			Upload <input type='file' maxlength='1' accept='sdf|mol|csv|xls' name='xmet_substrate_upload' title='Substrate upload' size='20'>
		</div>			
	</div>
	<div class='row remove-bottom' style="margin:5px;padding:5px;"> 	
	  <div class='three columns alpha'>Atom uncertainty:<em>*</em></div>
	  <div class='thirteen columns omega'>
		<select id="xmet_atom_uncertainty" name="xmet_atom_uncertainty" >
		<option value="Certain" selected="selected">Certain</option>
		<option value="Uncertain">Uncertain</option>
		</select>
	  </div>
	</div>
	<div class='row' style="margin:5px;padding:5px;"> 	
    	<div class='three columns alpha'>
    		Product:<em>*</em>
    	</div>
	 	<div class='eight columns omega'>
			<ul class='structresults' id="xmet_product_img" style='height:150px;'></ul>
			<input type="hidden" id="xmet_product_uri" name="xmet_product_uri" value="">
			<input type="hidden" id="xmet_product_mol" name="xmet_product_mol" value="">
		</div>
		<div class='five columns omega'>
			<a href="#" id='buttonProductDraw'  title='Launches structure diagram editor'>Show structure diagram editor</a>
			<br/>
			<a href="#" id="buttonProductSearch">Show search options</a>
			<br/>
			Upload <input type='file' maxlength='1' accept='sdf|mol|csv|xls' name='xmet_product_upload' title='Product upload' size='20'>
		</div>				
	</div>
	<div class='row remove-bottom' style="margin:5px;padding:5px;"> 	
	  <div class='three columns alpha'>Product amount:<em>*</em></div>
	  <div class='thirteen columns omega'>
		<select id="xmet_product_amount" name="xmet_product_amount">
		<option value="Major" selected="selected">Major</option>
		<option value="Minor">Minor</option>
		<option value="Unknown">Unknown</option>
		</select>
	  </div>
	</div>	
	<div class='row remove-bottom' style="margin:5px;padding:5px;"> 	
		<div class='three columns alpha'>Experiment:<em>*</em></div>
		<div class='three columns omega'>
			<select id="xmet_experiment" name="xmet_experiment">
			<option value="MS" selected="selected">MS (Microsomes)</option>
			<option value="HEP">HEP (Hepatocytes)</option>
			<option value="ENZ">ENZ (Enzyme)</option>
			</select>
		</div>
		<div class='two column omega'>Enzyme</div>
		<div class='five columns omega'>
			<select id="xmet_enzyme" name="xmet_enzyme"></select>
		</div>
		<div class='one column omega'>Allele</div>
		<div class='two columns omega'>
			<select id="xmet_allele" name="xmet_allele"></select>
		</div>
	</div>
	<div class='row remove-bottom' style="margin:5px;padding:5px;"> 	
		<div class='three columns alpha'>
			Reference:<em>*</em>
			<input type="hidden" name="published_status"  value="on">
		</div>
		<input type="text" name="xmet_reference" title="DOI or free text?" value="" class="eight columns omega remove-bottom">
		<input type="submit" class="submit five columns omega" value="Submit observation">
	</div>
			
	</form>	
    </div>

	<!-- searchUI starts -->
	<div id="searchUI" class="structresults remove-bottom" style='width:100%;display:none;'></div>
	<br/>
	<!-- Structure diagram editor -->
	<div id="drawUI" class="remove-bottom" style='display:none;'>
		<div class='row ui-widget-header ui-corner-top remove-bottom'>Structure diagram editor</div>
		<div class='row ui-widget-content ui-corner-bottom' >
			<div   style="margin:5px;padding:5px;"> 	
				&nbsp;
				<div class='ten columns alpha'>
					<applet code="JME.class" name="JME" archive="/xmetdb/jme/JME.jar" width="500px" height="400px">
					<param name="options" value="nohydrogens,polarnitro,nocanonize">
					You have to enable Java and JavaScript on your machine ! 
					</applet>
				</div>
				<div class='six columns omega'>
					<br/>
					<a href='#' class='button' onClick='useDrawn("${queryService}","substrate");return false;'>Use the structure as a substrate</a>
					<br/>
					<a href='#' class='button' onClick='useDrawn("${queryService}","product");return false;'>Use the structures as a product</a>&nbsp;
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
	<#include "/s_footer.ftl">
	
</div><!-- container -->

	<#include "/scripts-welcome.ftl">
</body>
</html>