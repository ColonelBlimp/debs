<a href="javascript:void(0);" id="modalLink" title="Generate Voucher">
  <span class="float-right hover:bg-blue-500 w-7 mr-1 p-1 mb-1">
    <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20" width="20px" height="20px">
      <path d="M16 1H4a1 1 0 0 0-1 1v16a1 1 0 0 0 1 1h12a1 1 0 0 0 1-1V2a1 1 0 0 0-1-1zm-1 16H5V3h10v14zM13 5H7v2h6V5zm0 8H7v2h6v-2zm0-4H7v2h6V9z"/>
    </svg>
  </span>
</a>
<div id="modal" class="voucher-modal">
  <div class="voucher-modal-content">
    <span id="close" class="voucher-modal-close">X</span>
    <span class="text-2xl text-center">Create Voucher</span>
    <@s.form action="transactions" onsubmit="return validateForm();" id="voucher" autocomplete="off">
    <@s.hidden name="id" id="id"/>
    <@s.label for="date" value="Date:" class="inline-block w-16"/>
    <@s.textfield name="voucherDate" id="date" autocomplete="off" autofocus="autofocus" placeholder="YYYY-MM-DD" required="true" maxlength="10" oninput="fieldUpdate()" class="border border-gray-400 w-32 my-1 p-1 outline-none focus:shadow-outline focus:bg-blue-100"/>
    <@s.fielderror><@s.param value="%{'date'}"/></@s.fielderror><br/>
    <@s.label for="number" value="Number:" class="inline-block w-16"/>
    <@s.textfield name="voucherNumber" id="number" autocomplete="off" placeholder="PVYYYYMMDD###" required="true" class="border border-gray-400 w-40 my-1 p-1 outline-none focus:shadow-outline focus:bg-blue-100"/>
    <@s.fielderror><@s.param value="%{'number'}"/></@s.fielderror><br/><br/><br/>
    <@s.submit name="submitType" id="create" value="Create" title="Create Voucher" class="border border-gray-400 px-2 pb-1 rounded cursor-pointer hover:bg-blue-700 hover:text-white font-semibold outline-none focus:shadow-outline"/>
    </@s.form>
  </div>
</div>
<script>
  var modal = document.getElementById('modal');
  var modalLink = document.getElementById('modalLink');
  var close = document.getElementById('close');
  modalLink.onclick = function() {
    modal.style.visibility = 'visible';
    document.voucher.voucherDate.focus();
  }
  close.onclick = function() {
    modal.style.visibility = 'hidden';
  }
  window.onclick = function(event) {
    if(event.target === modal) {
      modal.style.visibility = 'hidden';
    }
  }
  function validateForm() {
    var date = document.voucher.voucherDate;
    if (!validateDate(date.value)) {
      date.focus();
      return false;
    }
    var num = document.voucher.voucherNumber;
    if(!validateVoucherNumber(num.value)) {
      num.focus();
      return false;
    }
    return true;
  }
  function fieldUpdate() {
    var date = document.getElementById("date");
    var number = document.getElementById("number");
    number.value = "PV" + date.value.replace(/\D/g,'');
  }
</script>
