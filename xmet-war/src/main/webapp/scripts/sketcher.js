var _sketcher = {
		sketcher : null,
		smiles : "",
		form : 'search_form',
		startEditor : function (baseRef,formName) {
			form = formName==undefined?'form':formName;
			window.open(baseRef+'/scripts/sketcher_2D.html','ChemDoodle','width=550,height=450,scrollbars=no,resizable=yes,titlebar=yes,toolbar=yes,menubar=yes');
		},
		submitMol2Document: function(doc) {
			  var mol = this.sketcher.getMolecule();
			  var molFile = ChemDoodle.writeMOL(mol);
			  //console.log(window.frameElement.id);
			  doc[this.form].type.value = "mol"; 
			  doc[this.form].search.value = molFile; 
		}
		
};