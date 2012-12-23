	<div class="row remove-bottom ">	
     	<div class="two columns alpha"><label for="xmet_substrate">Substrate:</label></div>
      		<div class="six columns omega">
      				<ul class='structresults' id="xmet_substrate"></ul>
      		</div>
    </div>		
	<div class="row remove-bottom ">	
     	<div class="two columns alpha"><label for="xmet_product">Product:</label></div>
      		<div class="six columns omega">
      			<ul class='structresults' id="xmet_product"></ul>
      		</div>
    </div>		
	<div class="row remove-bottom ">	
     	<div class="three columns alpha"><label for="xmet_atom_uncertainty">Atom uncertainty:</label></div>
      	<div class="three columns omega">
      		<span id="xmet_atom_uncertainty">&nbsp;</span>
      	</div>
       	<div class="three columns omega"><label for="xmet_product_amount">Product amount:</label></div>
      	<div class="two columns omega">
      		<span id="xmet_product_amount">&nbsp;</span>
      	</div>
    </div>	    
	<div class="row remove-bottom ">	
     	<div class="three columns alpha"><label for="xmet_experiment">Experiment:</label></div>
      		<div class="six columns omega">
				<span id="xmet_experiment">&nbsp;</span>
      		</div>
    </div>		    
	<div class="row remove-bottom ">	
     	<div class="three columns alpha"><label for="xmet_enzyme">Enzyme:</label></div>
      		<div class="eight columns omega">
      			<span id="xmet_enzyme">&nbsp;</span>
      		</div>
      		<div class="one column omega"><label>Allele</label></div>
 			<div class="two columns omega">
      			<span id="xmet_allele">&nbsp;</span>
      		</div>      		
    </div>	   
    <div class="row remove-bottom">	
     	<div class="three columns alpha"><label for="xmet_reference">Reference:</label></div>
      		<div class="six columns omega">
      				<span id="xmet_reference">&nbsp;</span>
      		</div>
    </div>	
	
	<div class='row remove-bottom'>
		<hr class='half-bottom'/>
		<a class='button' id="xmet_editor" href="${xmet_root}/editor/"  title='Modify the observation'>Edit</a>
		&nbsp;|&nbsp;
		Export observation:&nbsp;
		<a class='button' id="xmet_export_json" href="${xmet_request}"  title='Export the observation as JSON'>as JSON</a>
		&nbsp;|&nbsp;
		Export substrate:&nbsp;
		<a class='button' id="xmet_export_substrate" href="#"  title='Export substrate structure as SDF'>as SDF</a>
		&nbsp;|&nbsp;
		Export product(s):&nbsp;
		<a class='button' id="xmet_export_product" href="#" title='Export product structure(s) as SDF'>as SDF</a>
		&nbsp;|&nbsp;
		<a class='button' id="xmet_copy_observation" href="#"  title='Create new observation with the same content as this one.Launches the new observation editor.'  onclick="alert('TODO')">Copy and create new observation</a>
	</div>	