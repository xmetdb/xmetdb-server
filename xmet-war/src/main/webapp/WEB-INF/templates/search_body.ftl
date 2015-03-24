<#include "/s_html.ftl" >
<head>
<#include "/s_head.ftl" >
<#include "/search_head.ftl" >
<link rel="stylesheet" href="${xmet_root}/style/form.css" type="text/css">
<script type="text/javascript" src="${xmet_root}/scripts/sketcher.js"></script>
<script type="text/javascript">
jQuery(document).ready(function()
{
	jQuery("#breadCrumb ul").append('<li><a href="${xmet_root}" title="XMetDB search">Search</a></li>');
    jQuery("#breadCrumb").jBreadCrumb();
    loadHelp("${xmet_root}","search");
    
    $( "#search_value" ).keypress(function() {
    	$( "#search_type" ).val(""); 
    });
})
</script>
</head>
<body>

<div class="container columns" style="padding:1;">
	    
	<#include "/s_banner.ftl">
	<#include "/s_menu.ftl">
		
	<!-- Page Content
	================================================== -->
	

	    	
    <div class="twelve columns ui-widget-header ui-corner-top">&nbsp;Search XMetDB</div>
	<div class="twelve columns remove-bottom ui-widget-content ui-corner-bottom add-bottom">
	<div  style="margin:5px;padding:5px;" class="remove-bottom"> 	
		
	<form method="GET" action="${xmet_root}/chemical" name="search_form" id="search_form" class="remove-bottom">
		<div class="row remove-bottom ">
	    	<iframe class="twelve columns alpha remove-bottom" style='height:420px;width:500px;' id="iframeSketcher" src="${xmet_root}/scripts/sketcher_2D.html"></iframe>	
			<div class="four columns omega remove-bottom ">
				<br/>
			    <label>Search for</label>
			    <a href='#' class='chelp scope'></a>
    		 	<br/>
    		 	<input type="checkbox" name="search_substrates" id="search_substrates" checked /><span>Substrate</span>
    		 	<br/>
   				<input type="checkbox" name="search_products" id="search_products" checked /><span>Product</span>
   				<br/>
   				<br/>
				<label>Search options</label>
				<a href='#' class='chelp options'></a>
				<br/>
				<input type='radio' id='auto' value='auto' name='option' checked title='Exact structure or search by identifier' size='20' tabindex='2'/>Auto
				</br>
      		    <input type='radio' name='option' id='similarity' value='similarity'  title='Enter SMILES or draw structure' tabindex='3'/>Similarity
      		    </br>
      		    <input type='radio' name='option' id="smarts" value='smarts' title='Enter or draw a SMARTS query' tabindex='5' size='40'/>Substructure
				<br/>
    		 	<select title ='Tanimoto similarity threshold' name='threshold' style='width:6em;' tabindex='4'><option value='0.9'>0.9</option><option value='0.8'>0.8</option><option value='0.7'>0.7</option><option value='0.6'>0.6</option><option value='0.5' selected >0.5</option><option value='0.4' >0.4</option><option value='0.3' >0.3</option><option value='0.2' >0.2</option></select>
    		 	<label>Chemical identifier</label>
    		 	<a href='#' class='chelp identifier'></a>
    		 	<br/>
    		 	<input type='text' name='search' id='search_value' size='60' value='' tabindex='1' title='Enter any chemical compound identifier (CAS, Name, EINECS, SMILES or InChI). The the input type is guessed automatically.'>
   				<input type='hidden' name='type' id='search_type' value=''>
   				
    		 	<input type="submit" class="search" value="Structure search" tabindex='13'>
			</div>
    	</div>	       	
	</form>
	<!-- Search observations -->
	<form method="GET" action="${xmet_root}/protocol" name="form" id="search_protocol_form" class="remove-bottom">
      	<div class="row remove-bottom">
      		<div class="two columns alpha"><label>Observation status:<a href='#' class='chelp published_status'></a></label></div>
      		<div class="eight columns omega">
				<input type='radio' id='curated_yes' value='curated' name='search_curated' checked  title='Curated only' size='20' tabindex='2'/>Curated 
      		    <input type='radio' name='search_curated' id='curated_no' value='noncurated' title='Not curated only' tabindex='3'/>Not curated 
      		    <input type='radio' name='search_curated' id="curated_any" value='any' title='Any'/>Any
    		 	      		
      		</div>
    	</div>	
      		    		
	    <div class="row remove-bottom">
      		<div class="two columns alpha"><label>Experiment:<a href='#' class='chelp experiment'></a></label></div>
      		<div class="eight columns omega">
				<input class="ui-widget" type='checkbox' name="xmet_exp_ms"  id="search_exp_ms"  tabindex='1'>MS (Microsomes)&nbsp;
				<input class="ui-widget" type='checkbox' name="xmet_exp_hep"  id="search_exp_hep" tabindex='2'>HEP (Hepatocytes)&nbsp;
				<input class="ui-widget" type='checkbox' name="xmet_exp_enz" id="search_exp_enz" tabindex='3'>ENZ (Enzyme)
      		</div>
    	</div>	
	    <div class="row remove-bottom">
      		<div class="two columns alpha"><label>Enzyme:<a href='#' class='chelp enzyme'></a></label></div>
      		<div class="nine columns omega">
				<select id="xmet_enzyme" name="xmet_enzyme" multiple tabindex='4' style='width:400px;'></select>
      		</div>
      		<div class="two columns omega">
      		      <label>Allele:<a href='#' class='chelp allele'></a></label>
      		</div>
      		<div class="two columns omega">
     				<select id="xmet_allele" name="xmet_allele" multiple tabindex='5'>
					</select>
      		</div>
    	</div>	    	
    	<div class="row remove-bottom">
      		<div class="two columns alpha"><label>Reference:<a href='#' class='chelp reference'></a></label></div>
      		<div class="six columns omega">
				<input class="ui-widget" type='text' id="xmet_reference" name="xmet_reference" tabindex='6' style='width:400px;'>
      		</div>
    	</div>	
    	<div class="row remove-bottom">
      		<div class="two columns alpha"><label>XMETID:<a href='#' class='chelp xmetid'></a></label></div>
      		<div class="four columns omega">
				<input class="ui-widget" type='text' id="xmet_number" name="xmet_number" tabindex='7' style="width:10em;">
      		</div>
      		<div class="two columns alpha"><label>Number of hits:<a href='#' class='chelp nhits'></a></label></div>
      		<div class="two columns omega">
      		   	<input type='text' size='3' name='pagesize' value='100' tabindex='8' style="width:3em;">
      		</div>         		
      		<div class="six columns omega">
    		 	   <input type="submit" class="search" value="Observations search" tabindex='9'>
      		</div>       		
    	</div>	    	
 	
    </form>
    

	</div>
	</div>
		
		<!-- Right column and footer
		================================================== -->
		<#include "/s_help.ftl">
		<#include "/s_footer.ftl">
		
	</div><!-- container -->

		<#include "/scripts-welcome.ftl">
	</body>
</html>
