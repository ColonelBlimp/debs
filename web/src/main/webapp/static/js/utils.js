function updateSelected(ctx,id) {
var del = document.getElementById('delete');
del.href=ctx+'/delete.action?id='+id;
del.className="pointer-events-auto";
var edit = document.getElementById('edit')
edit.href=ctx+'/edit.action?id='+id;
edit.className="pointer-events-auto";
}
