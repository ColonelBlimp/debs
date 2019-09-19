function updateSelected(ctx,id) {
var edit = document.getElementById('edit')
edit.href=ctx+'/edit.action?id='+id;
edit.className="pointer-events-auto";
return true;
}
function checkForTab(event) {
  var dElem = document.getElementById("date");
  if(event.keyCode==9 && dElem.value.length == 0) {
    var fdElem = document.getElementById('sessionDate');
    dElem.value=fdElem.value;
  }
}
function dataHrefLink(elem) {
  if(elem.getElementsByTagName('input')[0].checked) {
	  return false;
  }
  var href = elem.getAttribute('data-href');
  window.location = href;
  return false;
}
