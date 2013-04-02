	<div class="row remove-bottom ">	
     	<div class="two columns alpha"><label for="xmet_substrate">Substrate:<a href='#' class='chelp substrate'></a></label>
     		<br/>
     		<div id="sim_substrate"></div>
     	</div>
      		<div class="six columns omega">
      				<ul class='structresults' id="xmet_substrate"></ul>
      		</div>
    </div>		
	<div class="row remove-bottom ">	
     	<div class="two columns alpha"><label for="xmet_product">Product:<a href='#' class='chelp product'></a></label>
     	<br/>
     	<div id="sim_product"></div>
     	</div>
      		<div class="six columns omega">
      			<ul class='structresults' id="xmet_product"></ul>
      		</div>
    </div>		
	<div class="row remove-bottom ">	
     	<div class="three columns alpha"><label for="xmet_atom_uncertainty">Atom uncertainty:<a href='#' class='chelp atomuncertainty'></a></label></div>
      	<div class="three columns omega">
      		<span id="xmet_atom_uncertainty">&nbsp;</span>
      	</div>
       	<div class="three columns omega"><label for="xmet_product_amount">Product amount:<a href='#' class='chelp productamount'></a></label></div>
      	<div class="two columns omega">
      		<span id="xmet_product_amount">&nbsp;</span>
      	</div>
    </div>	    
	<div class="row remove-bottom ">	
     	<div class="three columns alpha"><label for="xmet_experiment">Experiment:<a href='#' class='chelp experiment'></a></label></div>
      		<div class="six columns omega">
				<span id="xmet_experiment">&nbsp;</span>
      		</div>
    </div>		    
	<div class="row remove-bottom ">	
     	<div class="three columns alpha"><label for="xmet_enzyme">Enzyme:<a href='#' class='chelp enzyme'></a></label></div>
      		<div class="eight columns omega">
      			<span id="xmet_enzyme">&nbsp;</span>
      		</div>
      		<div class="one column omega"><label>Allele</label></div>
 			<div class="two columns omega">
      			<span id="xmet_allele">&nbsp;</span>
      		</div>      		
    </div>	   
    <div class="row remove-bottom">	
     	<div class="three columns alpha"><label for="xmet_reference">Reference:<a href='#' class='chelp reference'></a></label></div>
      		<div class="six columns omega">
      				<span id="xmet_reference">&nbsp;</span>
      				<a href='http://dx.doi.org/' target='doi' title='' id='xmet_doi' ><span id="info-link" class="ui-icon ui-icon-link" style="display: inline-block;"></span></a> 
      				<a href='#' id='sim_reference' title='Search for observations with the same reference'><span id="info-sim-ref" class="ui-icon ui-icon-search" style="display: inline-block;"></span></a>
      		</div>
    </div>	
	
	<div class='row remove-bottom' style="margin:5px;padding:5px;"> 	
		<div class='three columns alpha'>
			<label>Comment:</label>
		</div>
		<textarea name="xmet_comments" id="xmet_comments" title="Note" value="" row="1" class="eight columns omega remove-bottom" readonly></textarea>
	</div>		
	<div class='row remove-bottom' >
		<hr class='half-bottom'/>
		<div class='eleven columns alpha'>
		Export observation:&nbsp;
		<a id="xmet_export_json" href="${xmet_request}"  title='Export the observation as JSON'><img src='${xmet_root}/images/json.png'></a>
		<br/>Export substrate:&nbsp;&nbsp;&nbsp;&nbsp;
		<a id="xmet_export_substrate" href="#"  title='Export substrate structure as SDF'><img src='${xmet_root}/images/sdf.jpg'></a>
		<br/>Export product(s):&nbsp;&nbsp;&nbsp;
		<a id="xmet_export_product" href="#" title='Export product structure(s) as SDF'><img src='${xmet_root}/images/sdf.jpg'></a>
		</div>
		<div class='five columns omega' style='margin-top:0.5em'>
		<a class='button' id="xmet_editor" href="${xmet_root}/editor/"  title='Modify the observation'>Modify this observation</a><a href='#' class='chelp modifyobs'></a>
		<br/>
		<form method='POST' action='${xmet_root}/protocol' enctype='application/x-www-form-urlencoded' autocomplete name='xmet_copy'>
		<input type='hidden' name='source_uri' value='${xmet_request}'>
		<input type='submit' class='button remove-bottom' title='Create new observation with the same content as this one.'  value='Copy and create new observation'>
		<a href='#' class='chelp copyobs'></a>
		</form>
		</div>


	</div>	