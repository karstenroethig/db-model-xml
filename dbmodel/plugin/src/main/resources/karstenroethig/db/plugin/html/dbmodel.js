function expandAll() {
	$( ".contract" ).hide();
	$( ".expand" ).show();
}

function expand( attributeName ) {
	$( "#desc_contract_" + attributeName ).hide();
	$( "#desc_expand_" + attributeName ).show();
	
	$( "#toggle_contract_" + attributeName ).hide();
	$( "#toggle_expand_" + attributeName ).show();
}

function contractAll() {
	$( ".expand" ).hide();
	$( ".contract" ).show();
}

function contract( attributeName ) {
	$( "#desc_expand_" + attributeName ).hide();
	$( "#desc_contract_" + attributeName ).show();
	
	$( "#toggle_expand_" + attributeName ).hide();
	$( "#toggle_contract_" + attributeName ).show();
}