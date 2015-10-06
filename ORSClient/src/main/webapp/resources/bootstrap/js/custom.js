function checkUsername(){
    var inputvalue = $("#user_username").val();
    window.location.replace("control?action=checkUsername&username="+inputvalue);
}
    
function validatePassword(){
    var password = document.getElementById("user_new_password"),
        confirm_password = document.getElementById("user_passwordConfirm");
    if(password.value != confirm_password.value) {
        confirm_password.setCustomValidity("Passwords don't match");
    } else {
        confirm_password.setCustomValidity('');
    }
}

// triggered when modal is about to be shown
$(document).ready(function(){
    $('#startAuctionModal').on('show.bs.modal', function(e) {
        
        //get data-id attribute of the clicked element
        var itemId = $(e.relatedTarget).data('item-id');
    
        //set hidden value
        $(e.currentTarget).find('input[name="item"]').val(itemId);
    });
});