	$(document).ready( function () {
    	$('div#header,div#copyright').mouseenter( function () {
    		$('div#copyright').stop().animate({top: '3px', opacity: '1'}, {duration: 1000, easing: 'easeOutBounce'});
    	});
    	$('div#header,div#copyright').mouseleave( function () {
    		$('div#copyright').stop().animate({top: '-20px', opacity: '0'}, {duration: 1000});
    	});
    });