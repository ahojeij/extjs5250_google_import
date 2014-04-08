/*
** Returns the caret (cursor) position of the specified text field.
** Return value range is 0-oField.length.
*/
function doGetCaretPosition (oField) {
	
	// Initialize
	var iCaretPos = 0;
	
	// IE Support
	if (document.selection) {
	
		// Set focus on the element
		oField.focus ();
		
		// To get cursor position, get empty selection range
		var oSel = document.selection.createRange ();
		
		// Move selection start to 0 position
		oSel.moveStart ('character', -oField.value.length);
		
		// The caret position is selection length
		iCaretPos = oSel.text.length;
	}	
	// Firefox support
	else if (oField.selectionStart || oField.selectionStart == '0')
	iCaretPos = oField.selectionStart;
	
	// Return results
	return (iCaretPos);
}


/*
** Sets the caret (cursor) position of the specified text field.
** Valid positions are 0-oField.length.
*/
function doSetCaretPosition (oField, iCaretPos) {

	// IE Support
	if (document.selection) {
	
	// Set focus on the element
	oField.focus ();
	
	// Create empty selection range
	var oSel = document.selection.createRange ();
	
	// Move selection start and end to 0 position
	oSel.moveStart ('character', -oField.value.length);
	
	// Move selection start and end to desired position
	oSel.moveStart ('character', iCaretPos);
	oSel.moveEnd ('character', 0);
	oSel.select ();
}

// Firefox support
else if (oField.selectionStart || oField.selectionStart == '0') {
oField.selectionStart = iCaretPos;
oField.selectionEnd = iCaretPos;
oField.focus ();
}
}

// Cross Browser selectionStart/selectionEnd
// Version 0.2
// Copyright (c) 2005-2007 KOSEKI Kengo
// 
// This script is distributed under the MIT licence.
// http://www.opensource.org/licenses/mit-license.php

function Selection(textareaElement) {
    this.element = textareaElement;
}

Selection.prototype.create = function() {
    if (document.selection != null && this.element.selectionStart == null) {
        return this._ieGetSelection();
    } else {
        return this._mozillaGetSelection();
    }
};

Selection.prototype._mozillaGetSelection = function() {
    return { 
        start: this.element.selectionStart, 
        end: this.element.selectionEnd 
    };
};

Selection.prototype._ieGetSelection = function() {
    this.element.focus();

    var range = document.selection.createRange();
    var bookmark = range.getBookmark();

    var contents = this.element.value;
    var originalContents = contents;
    var marker = this._createSelectionMarker();
    while(contents.indexOf(marker) != -1) {
        marker = this._createSelectionMarker();
    }

    var parent = range.parentElement();
    if (parent == null || parent.type != 'textarea') {
        return { start: 0, end: 0 };
    }
    range.text = marker + range.text + marker;
    contents = this.element.value;

    var result = {};
    result.start = contents.indexOf(marker);
    contents = contents.replace(marker, '');
    result.end = contents.indexOf(marker);

    this.element.value = originalContents;
    range.moveToBookmark(bookmark);
    range.select();

    return result;
};

Selection.prototype._createSelectionMarker = function() {
    return '##SELECTION_MARKER_' + Math.random() + '##';
};

