function updateSelected(ctx,id) {
var edit = document.getElementById('edit')
edit.href=ctx+'/edit.action?id='+id;
edit.className="pointer-events-auto";
}
