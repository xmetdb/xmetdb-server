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
	<div class='ui-widget-content ui-corner-bottom' style='margin-top: 0px;' />
	<!-- Don't expect this to work! -->

			<p class='w_p'>		
	<form method="POST" action="/xmetdb/protocol" id="submitForm" ENCTYPE="multipart/form-data">

	<table id="xmet_obs" style="margin: 0 0 0 0;padding: 0 0 0 0;" width="100%">
	<!-- Substrate (to be able to edit the structure / retrieve existing and select SOM ) -->
	<tr>
	<th align="right"  valign='top' width='15%'>
	Substrate:
	<br>
	<a href="#" id="buttonSubstrateSearch">Hide search options</a>
	</th><td colspan="2" id="substratesearch"></td>
	</tr>	
	<tr>
	<th align="right" height="150px" valign='top'>
		
	</th><td colspan="2">
	
	<!-- the picture -->
	<p style="float:left;" id="structure1"/>
	<input type="text" id="xmet_substrate" value="TODO" valign="bottom" style="position:relative;float:right;"/>
	</td>
	<td valign="top"  align="right">

	
	</td>
	</tr>
	<tr>
	<th align="right">Atom uncertainty:</th><td colspan="3">
			<select id="xmet_atom_uncertainty" >
			<option value="Certain" selected>Certain</option>
			<option value="Uncertain">Uncertain</option>
			</select>
	</td>
	</tr>	
	<tr>
	<td colspan="4"><hr></td>
	</tr>
	<!-- Product (to be able to edit the structure / retrieve existing ) -->
	<tr>
	<th align="right">
		Product:
		<br>
		<a href="#" id="buttonProductSearch">Hide search options</a>
	</th><td colspan="2" id="productsearch" ></td>
	</tr>	
	<tr>
	<th align="right" height="150px" valign='top'></th><td colspan="3">
	<p style="float:left;" id="structure2"/>
	<input type="text" id="xmet_product" value="TODO" valign="bottom" style="position:relative;float:right;"/>	
</p>
	</td>
	</tr>
	<!-- Product amount -->
	<tr>
	<th align="right">Product amount:</th><td colspan="3">
		<select id="xmet_product_amount">
			<option value="Major" selected>Major</option>
			<option value="Minor">Minor</option>
			<option value="Unknown">Unknown</option>
		</select>
	</td>
	</tr>	
	<tr>
	<td colspan="4"><hr></td>
	</tr>
	<!-- Experiment -->
	<tr>
	<th align="right">Experiment:</th><td colspan="3">
	
			<select id="xmet_experiment"  >
			<option value="MS" selected>MS (Microsomes)</option>
			<option value="HEP">HEP (Hepatocytes)</option>
			<option value="ENZ">ENZ (Enzyme)</option>
		</select>
	</td>
	</tr>	
	
	<!-- Enzymes -->
	<tr>
	<th align="right" valign="top">Enzyme:</th>
	<td>
		<select id="search_enzyme">
		</select>
	</td>
	<th align="right" valign="top">Allele:</th>
	<td valign="top">
	TODO
	</td>
	</tr>	
	<!-- References -->
	<tr>
	<th align="right">Reference</th><td colspan="3">
		<input type="text" name="xmet_reference" title="DOI or free text?" value="">
	</td>
	</tr>	
	
	<!-- Submit -->
			
	<tr>
	<th align="right" valign="top"></th>
	<td ></td>
	<td colspan="2">
			<input type="submit" class="submit" value="Submit">	
	</td>
	</tr>	
		
	</table>
	</form>
		</p>
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
	
</body>
</html>

