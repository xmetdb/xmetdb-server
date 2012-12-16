<#include "/s_html.ftl" >
<head>
<#include "/s_head.ftl" >
<#include "/search_head.ftl" >
<link rel="stylesheet" href="${xmet_root}/style/form.css" type="text/css">
<script type="text/javascript" src="${xmet_root}/scripts/sketcher.js"></script>
</head>
<body>

<div class="container columns" style="padding:1;">
	    
	<#include "/s_banner.ftl">
	<#include "/s_menu.ftl">
		
	<!-- Page Content
	================================================== -->
	

	    	
    <div class="twelve columns ui-widget-header ui-corner-top">Search XMETDB</div>
	<div class="twelve columns remove-bottom ui-widget-content ui-corner-bottom">
	<div  style="margin:5px;padding:5px;" class="remove-bottom"> 	
		
	<form method="GET" action="${xmet_root}/chemical" name="search_form" id="search_form">
		<div class="row remove-bottom ">
	    	<iframe class="twelve columns alpha remove-bottom" style='height:450px;width:500px;' id="iframeSketcher" src="${xmet_root}/scripts/sketcher_2D.html"></iframe>	
			<div class="four columns omega remove-bottom ">
				<br/>
			    <label>Search for</label>
    		 	<br/>
    		 	<input type="checkbox" name="search_substrates" id="search_substrates" checked /><span>Substrate</span>
    		 	<br/>
   				<input type="checkbox" name="search_products" id="search_products" checked /><span>Product</span>
   				<br/>
   				<br/>
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
    		 	<input type='text' name='search' size='60' value='c1ccccc1Cl' tabindex='1' title='Enter any chemical compound identifier (CAS, Name, EINECS, SMILES or InChI). The the input type is guessed automatically.'>
   				<input type='hidden' name='type' value='smiles'>
    		 	<input type="submit" class="search" value="Structure search" tabindex='13'>
			</div>
    	</div>	       	
	</form>
	<form method="GET" action="${xmet_root}/protocol" name="form" id="search_protocol_form">		
	    <div class="row remove-bottom">
      		<div class="two columns alpha"><label>Experiment:</label></div>
      		<div class="eight columns omega">
				<input class="ui-widget" type='checkbox' name="search_exp_ms"  id="search_exp_ms"  tabindex='6'>MS (Microsomes)&nbsp;
				<input class="ui-widget" type='checkbox' name="search_exp_hep"  id="search_exp_hep" tabindex='7'>HEP (Hepatocytes)&nbsp;
				<input class="ui-widget" type='checkbox' name="search_exp_enz" id="search_exp_enz" tabindex='8'>ENZ (Enzyme)
      		</div>
    	</div>	
	    <div class="row remove-bottom">
      		<div class="two columns alpha"><label>Enzyme:</label></div>
      		<div class="eight columns omega">
				<!-- option=qmrfnumber | endpoint| title | text | author-->
				<input type='hidden' name="option" value='endpointcode'>
				<select id="search_enzyme" name="search" multiple tabindex='9'></select>
      		</div>
      		<div class="two columns omega">
      				Allele
      				<select id="search_allele" name="search_allele" multiple tabindex='10'>
					</select>
      		</div>
    	</div>	    	
    	<div class="row remove-bottom">
      		<div class="two columns alpha"><label>XMETID:</label></div>
      		<div class="six columns omega">
				<input class="ui-widget" type='text' id="search_xmetid" name="search_xmetid" tabindex='11'>
      		</div>
    	</div>	
     	<div class="row remove-bottom">
      		<div class="two columns alpha"><label>Number of hits</label></div>
      		<div class="eight columns omega">
      		   	<input type='text' size='3' name='pagesize' value='100' tabindex='12'>
      		</div>
      		<div class="six columns omega">
    		 	   <input type="submit" class="search" value="Search observation" tabindex='13'>
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