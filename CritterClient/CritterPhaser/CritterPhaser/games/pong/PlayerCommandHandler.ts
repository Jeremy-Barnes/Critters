export class PlayerCommandHandler {

    
    static createMapping(keyboard: Phaser.Keyboard, keyCode: number, onUp: any, onDown: any, onHold: any, gameThis: any) {
        var key = keyboard.addKey(keyCode);
        keyboard.addKeyCapture(keyCode);

        if (onDown) {
            key.onDown.removeAll();
            key.onDown.add(onDown.bind(gameThis));
        }

        if (onUp) {
            key.onUp.removeAll();
            key.onUp.add(onUp.bind(gameThis));
        }

        if (onHold) {
            key.onHoldCallback = null;
            key.onHoldCallback = onHold.bind(gameThis);
        }
    }

    static createSingleMapping(keyboard: Phaser.Keyboard, keyCode: number, handlerFunc: any, gameThis: any) {
        PlayerCommandHandler.createMapping(keyboard, keyCode, handlerFunc, handlerFunc, handlerFunc, gameThis);
    }
}

class PlayerCommandMapping {

    realCommand: any;
    playerButton: any;

}