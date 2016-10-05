import { User, CreateAccountRequest, Pet } from './dtos'
import {ServiceMethods} from "./servicemethods"


export class Application {
    public static user: User = new User();

    public static submitUserAccountCreationRequest(user: User, pet: Pet) {
        var createRequest = new CreateAccountRequest();
        createRequest.pet = pet;
        createRequest.user = user;
        alert("FIX ME APPSERVICE.TS")//ServiceMethods.createUser(createRequest);
    }

    public static getPetSpecies(user: User) {
        alert("FIX ME APPSERVICE.TS")//ServiceMethods.getPetOptions(createRequest);
        return [{ petSpeciesConfigID: 0, petTypeName: "dog" }, { petSpeciesConfigID: 1, petTypeName: "cat" }, { petSpeciesConfigID: 2, petTypeName: "horrible clion" }]; //todo replace with server call, this is test data
    }

    public static getPetColors(user: User) {
        alert("FIX ME APPSERVICE.TS")//ServiceMethods.getPetOptions(createRequest);
        return [{ petColorConfigID: 0, petColorName: "blue" }, { petColorConfigID: 1, petColorName: "red" }, { petColorConfigID: 2, petColorName: "octarine" }]; //todo replace with server call, this is test data

    }
}