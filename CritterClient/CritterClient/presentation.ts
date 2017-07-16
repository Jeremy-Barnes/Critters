/* This is non-complex, presentation only logic. It should not deal with data in any way */
//jQuery(document).ready(

function prepDisplay() {
    //off-canvas stuff
    jQuery('[data-toggle="offcanvas"]').click(function () {
        jQuery('.sidebar-offcanvas').toggleClass('active')
    });

    //hide collapses when not in use
    $(document).click(function (event) {
        var clickover = $(event.target);
        var _opened = $("#userMenuMobile").hasClass("in");
        if (_opened === true && !clickover.hasClass("buttonCollapse")){
            $("#userMenuClickMobile").click();
        }

        _opened = $("#userMenu").hasClass("in");
        if (_opened === true && !clickover.hasClass("buttonCollapse")){
            $("#userMenuClick").click();
        }
    });

    //other stuff here later
}//);

function prepDisplayAfterLogin() {
    (<any>$('.messageslink')).popover({
        placement: 'bottom',
        container: 'body',
        html: true,
        content: function () {
            return $(this).next('.notifications-bubble-container').html();
        }
    });
}
