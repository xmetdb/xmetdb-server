<div class='helptitle' style='font-weight:bold;'>XMetDB Observation</div>
<div class='helpcontent'>
</div>

<div id="keys" style="display:none;">
  <ul>
    <li><a href="#substrate">Substrate</a></li>
    <li><a href="#product">Product</a></li>
    <li><a href="#atomuncertainty">Atom Uncertainty</a></li>
    <li><a href="#productamount">Product amount</a></li>
    <li><a href="#experiment">Experiment</a></li>
    <li><a href="#enzyme">Enzyme</a></li>
    <li><a href="#allele">Allele</a></li>
    <li><a href="#reference">Reference</a></li>
    <li><a href="#modifyobs">Modify</a></li>
    <li><a href="#modifysom">Modify Sites of Metabolism</a></li>
    <li><a href="#copyobs">Copy</a></li>
    <li><a href="#hcurator">XMETDB curation</a></li>
  </ul>
  <div id="substrate">
    <p>The substrate</p>
  </div>
  <div id="product">
    <p>The product.</p>
  </div>
  <div id="atomuncertainty">
    <p>Certain/Uncertain. An uncertain atom mapping means that the reaction mechanism occuring is not known with atomic detail.</p>
  </div>
  <div id="productamount">
    <p>The product amount. One of Major, Minor or Unknown</p>
  </div>
  <div id="experiment">
  <!-- Change the links to the proper documentation. Could be on xmetdb wiki pages -->
    <p>The experiment. One of 
    <a href='http://en.wikipedia.org/wiki/Hepatocyte' target=_blank>Hepatocytes</a>, 
    <a href='http://en.wikipedia.org/wiki/Microsomes' target=_blank>Microsomes</a>, 
    Enzymes.
    </p>
  </div>
  <div id="enzyme">
    <p>XMetDB supports list of <a href='${xmet_root}/catalog'>enzymes</a>, editable by XMetDB administrators.</p>
  </div>
  <div id="allele">
    <p>Allele</p>
  </div>  
  <div id="reference">
    <p>The publication from which the data is taken, either as a <a href='http://en.wikipedia.org/wiki/Digital_object_identifier' target=_blank>DOI</a> or as a plain text reference.</p>
  </div>  
  <div id="modifyobs">
    <p>Launches the observation editor. Only allowed for observation owners and admins.</p>
  </div>  
  <div id="modifysom">
    <p>Allows to modify Sites of Metabolism (atom hilighting). Only allowed for observation owners and admins.</p>
  </div>    
  <div id="copyobs">
    <p>Copies this observation into new one and launches the observation editor. 
    This is a convenient way to enter a new observation which only slightly differ from an existing one.</p>
  </div>  
    <div id="hcurator">
   <p>
    The curated observations are marked with a star, otherwise the cell is empty.
    A link "Curate" appears in this column, if the logged in user is assigned a curator role.
    <p>
    The curators can edit all observations but not essential info as experiment and enzymes.
    The curators can change atom highlighting and comments and typos in references.
    The curators can set the flag curated to yes for any observations.
    The users may indicate he is available as a curator, but this does not automatically grant the curator role.
    The curator role is assigned by admins only.
<#if xmetdb_admin?? && xmetdb_admin>
	<br/>
	Double click on the curator role cell to edit.
</#if>    
    </p>
	</div>
</div>