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

    public static getPetOptions(user: User) {
        alert("FIX ME APPSERVICE.TS")//ServiceMethods.getPetOptions(createRequest);
    }
}