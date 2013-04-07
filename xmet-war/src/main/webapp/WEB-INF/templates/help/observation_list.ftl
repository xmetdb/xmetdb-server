<div class='helptitle' style='font-weight:bold;'>XMetDB Observations</div>
<div class='helpcontent'>
This is a list of XMetDB observations. For observation details please click on the XMetDB ID.
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
    <li><a href="#search">Search/Draw options</a></li>
    <li><a href="#hcurator">XMETDB curation</a></li>
  </ul>
  <div id="substrate">
    <p>The substrate molecule</p>
  </div>
  <div id="product">
    <p>The product molecule formed in this biotransformation. <br />If it is a multi-step reaction (i.e. has several semi-stable intermediate (Where semi-stable means can be identified experimentally, at least with the best possible experiment.), please add each step as a separate reaction if the intermediates are known with certainty.</p>
  </div>
  <div id="atomuncertainty">
    <p>Certain/Uncertain<br />The atom certainty reflects how well the reaction mechanism is understood. An atom mapping is certain if the highlighted atom(s) in the substrate are certain to lead to this product. A certain mapping should in most cases only contain a single atom (see the documentation for details on how to do correct atom mapping for different types of enzymatic reactions).</p>
  </div>
  <div id="productamount">
    <p>Major/Minor/Unknown<br />The amount is major if we know that this is the major product or that the amount formed is >50% of the most formed product, minor if we know that its formed to <50% of the major product. It is unknown when we do not know to what extent other products are formed in relation to this product. The amount is of course under the experimental conditions mentioned and for the enzyme labelled.</p>
  </div>
  <div id="experiment">
    <p>The type of experiment. MS (microsomes) represents the common case of using human liver microsomes. HEP (hepatocytes)  represents any use of liver cells or liver fractions, please add details in the comment sections. ENZ (enzyme) means a purified enzyme has been used.</p>
  </div>
  <div id="enzyme">
    <p>The enzyme responsible for this biotransformation. If multiple enzymes do the same biotransformation please copy the observation and make a new one for each enzyme.</p>
  </div>
  <div id="allele">
    <p>The enzyme allele studied. If none selected wild type is assumed.</p>
  </div>  
  <div id="reference">
    <p>DOI or plain text. Please start doi with "DOI:"</p>
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