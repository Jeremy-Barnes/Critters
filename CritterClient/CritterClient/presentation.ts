/* This is non-complex, presentation only logic. It should not deal with data in any way */
//jQuery(document).ready(

function prepDisplay() {


    //off-canvas stuff
    jQuery('[data-toggle="offcanvas"]').click(function () {
        jQuery('.sidebar-offcanvas').toggleClass('active')
    });

    //other stuff here later
}//);

function prepDisplayAfterLogin() {
    (<any>$('.messageslink')).popover({
        placement: 'bottom',
        container: 'body',
        html: true,
        content: function () {
            return $(this).next('.notifications-bubble').html();
        }
    });
}
