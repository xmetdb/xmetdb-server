<#include "/html.ftl" >
<head>
<#include "/head.ftl" >
<script type='text/javascript' src='/xmetdb/jme/jme.js'></script>
<#include "/submit_head.ftl" >
</head>
<body>
	<div class='w_wrap'>
	<#include "/banner.ftl">
	<#include "/menu.ftl">
			
	<div class='w_content ui-widget ' style='margin-top: 25px; margin-left: 0px;  margin-right: 10px; padding: 0 .1em;'>
	<div class='ui-widget-header ui-corner-top'><p>New XMETDB observation <span id="xmet_id"></span></p></div>
	<div class='ui-widget-content ui-corner-bottom' style='margin: 0 0 0 0; padding: 0 0 0 0;' />
	<!-- Don't expect this to work! -->

	<p class='w_p'>		
	<table id="xmet_obs" style="margin: 0 0 0 0;padding: 0 0 0 0;" width="100%">
	<!-- Substrate (to be able to edit the structure / retrieve existing and select SOM ) -->
	<tr>
	<th align="right"  valign='top' width='15%'>
	Substrate:
	</th>
	<td>
		<a style='float:right;' href="#" id="buttonSubstrateSearch">Hide search options</a>
		<br>

	</td>
	</tr>	
	<tr>
	<th align="right" height="150px" valign='top'>
	</th>
	<td>
	<!-- the picture -->
	<span  style="float:left;" id='xmet_substrate_img'></span>
	<span style='display:none'>
	<p id="structure1"/>
	<input type="text" id="xmet_substrate" value="TODO" valign="bottom" style="position:relative;float:right;"/>
	</span>
	</td>
	</tr>	
	<tr><td colspan="2"><hr></td></tr>
	<tr>
	<th align="right">
		Product:
	</th>
	<td>
		<a style='float:right;' href="#" id="buttonProductSearch">Hide search options</a>
		<br>
		<div id="productsearch"></div>
	</td>
	</tr>	
	<tr>
	<th align="right" height="150px" valign='top'></th>
	<td>
	<span  style="float:left;" id='xmet_product_img'></span>
	<span style='display: none;'>
	<p id="structure2"/>
	<input type="text" id="xmet_product" value="TODO" valign="bottom" style="position:relative;float:right;"/>
	</span>
	</td>
	</tr>
	<tr><td colspan="2"><hr></td></tr>
	</table>
	
	<form method="POST" action="/xmetdb/protocol" id="submitForm" ENCTYPE="multipart/form-data">

	<p><label for="xmet_substrate_upload">Upload substrate:<em>*</em></label>
	<input type='file'  class='multi' maxlength='1' accept='sdf|mol|csv|xls' name='xmet_substrate_upload' title='Substrate upload' size='30'>
	</p>	
	
	<p><label for="xmet_product_upload">Upload product:<em>*</em></label>
	<input type='file'  class='multi' maxlength='1' accept='sdf|mol|csv|xls' name='xmet_product_upload' title='Product upload' size='30'>
	</p>	
	
	<p><label for="xmet_atom_uncertainty">Atom uncertainty:<em>*</em></label>
			<select id="xmet_atom_uncertainty" >
			<option value="Certain" selected>Certain</option>
			<option value="Uncertain">Uncertain</option>
			</select>
	</p>	
		<!-- Product amount -->
	<p><label for="xmet_product_amount">Product amount:<em>*</em></label>
		<select id="xmet_product_amount">
			<option value="Major" selected>Major</option>
			<option value="Minor">Minor</option>
			<option value="Unknown">Unknown</option>
		</select>
	</p>
	<!-- Experiment -->
	<p><label for="xmet_experiment">Experiment:<em>*</em></label>
		<select id="xmet_experiment"  >
			<option value="MS" selected>MS (Microsomes)</option>
			<option value="HEP">HEP (Hepatocytes)</option>
			<option value="ENZ">ENZ (Enzyme)</option>
		</select>
	</p>
	<!-- Enzymes -->
	<p><label for="search_enzyme">Enzyme:<em>*</em></label>
		<select id="search_enzyme"  >
		</select>
		Allele
	</p>
	<!-- References -->
	<p><label for="xmet_reference">Reference:<em>*</em></label>
		<input type="text" name="xmet_reference" size='60' title="DOI or free text?" value="">
	</p>
	
	<!-- Submit -->
	<p><input type="submit" class="submit" value="Submit">	</p>			
	<p>
	<input type="hidden" id="xmet_substrate_uri" name="xmet_substrate_uri" value="">
	</p>
	<p>
	<input type="hidden" id="xmet_product_uri" name="xmet_product_uri" value="">
	</p>
	</form>
	<!-- End of form submission -->
	
	</div>
		   	
		   	
			<div class="ui-widget">
				<div class="ui-state-highlight ui-corner-all" style="margin-top: 20px; padding: 0 .7em;"> 
					<p><span class="ui-icon ui-icon-info" style="float: left; margin-right: .3em;"></span>
 
					<strong>Warning:</strong> This form is under development! <br>Submission of new observations <br>does not work yet! </p>
				</div>
			</div>
					   	
	</div> <#-- w_content -->

	<#-- Prevents the footer from hiding the last line on smaller screens. -->
	<div class='pusher'></div>

	
	</div> <#-- w_wrap -->
	
<#include "/footer.ftl">
<#include "/scripts-welcome.ftl">


	<script>
	structure("#structure1","#xmet_substrate");
	structure("#structure2","#xmet_product");
	
	function structure(svgtag,tag) {
  		var width = 200,
      	height = 200;
		var svg = d3.select(svgtag).append("svg")
      		.attr("width", width)
      		.attr("height", height);

		 svg.append("rect")
      		.attr("class", "background")
      		.attr("width", width)
      		.attr("height", height);
      		
        
          var link = svg.selectAll(".link")
        .data(molecule.bonds)
      	.enter().append("line")
        .attr("class", "bond")
        .attr("x1", function(d) { return 50+molecule.atoms[d["atoms"][0]]["x"]; })
        .attr("y1", function(d) { return 90+molecule.atoms[d["atoms"][0]]["y"]; })
        .attr("x2", function(d) { return 50+molecule.atoms[d["atoms"][1]]["x"]; })
        .attr("y2", function(d) { return 90+molecule.atoms[d["atoms"][1]]["y"]; })
        .style("stroke-width", function(d) { return Math.sqrt(d.value); });
        
               
         var node = svg.selectAll(".node")
        //.data(molecule.atoms)
 		.data(molecule.atoms.filter(function(d) {return d["atom"]; }))         
      	.enter().append("g")
        .attr("cx", function(d) { return 50+d["x"]; })
        .attr("cy", function(d) { return 90+d["y"]; });
        
        var circle =  node.append("circle")
	    .attr("class", "atom")
        .attr("cx", function(d) { return 50+d["x"]; })
        .attr("cy", function(d) { return 90+d["y"]; })
        .style("fill", function(d) { return d["selected"]?"red":"white"; })
        .attr("r", 12)
        ;
	        
		var text = node.append("text")
          .attr("x", function(d) { return 50+d["x"]; })
          .attr("y", function(d) { return 90+d["y"]; })
	      .attr("text-anchor", "end")
	      .text(function(d) { return d["id"]; });
    
       var anode = svg.selectAll(".atom")
			.on("click", function(d,i) {
				circle.filter(function (dd) { return dd == d;} )
				.style("fill",function (ddd) { 
					ddd["selected"] = !ddd["selected"]; 
					d3.select(tag).attr("value",ddd["selected"]?ddd["id"]:"");
					return ddd["selected"]?"red":"white"; 
					} 
				);
			});

	};
	</script>
	
	<div id="substratesearch"></div>
	
</body>
</html>

