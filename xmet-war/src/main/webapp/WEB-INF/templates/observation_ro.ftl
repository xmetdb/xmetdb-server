<div class='ui-widget-content' style='margin-top: 0px;margin-bottom: 0px;' />
	<p class='w_p'>		
	
	<p><label for="xmet_substrate">Substrate:</label>
			<ul class='structresults' id="xmet_substrate"></ul>
	</p>	
	<p><label for="xmet_product">Product:</label>
			<ul class='structresults' id="xmet_product"></ul>
	</p>	
	<p><label for="xmet_atom_uncertainty">Atom uncertainty:</label>
			<span id="xmet_atom_uncertainty">&nbsp;</span>
	</p>	
	<p><label for="xmet_product_amount">Product amount:</label>
		<span id="xmet_product_amount">&nbsp;</span>
	</p>
	<p><label for="xmet_experiment">Experiment:</label>
		<span id="xmet_experiment">&nbsp;</span>
	</p>
	<p><label for="xmet_enzyme">Enzyme:</label>
		<span id="xmet_enzyme">&nbsp;</span>
		&nbsp;<b>Allele:</b>
		<span id="xmet_allele">&nbsp;</span>
	</p>	
	<p><label for="xmet_reference">Reference:</label>
		<span id="xmet_reference">&nbsp;</span>
	</p>
</p>
	</div>
	<div class='ui-widget-header ui-corner-bottom'><p>
		<a class='button' id="xmet_editor" href="/xmetdb/editor/">Edit</a>
		&nbsp;|&nbsp;
		Export observation:&nbsp;
		<a class='button' id="xmet_export_json" href="${xmet_request}">as JSON</a>
		&nbsp;|&nbsp;
		Export substrate:&nbsp;
		<a class='button' id="xmet_export_substrate" href="#">as SDF</a>
		&nbsp;|&nbsp;
		Export product(s):&nbsp;
		<a class='button' id="xmet_export_product" href="#">as SDF</a>
		</p>
	</div>	