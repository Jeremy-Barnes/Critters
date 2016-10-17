import { User, CreateAccountRequest, Pet } from './dtos'
import {ServiceMethods} from "./servicemethods"


export class Application {
    public static user: User = new User();

    public static submitUserAccountCreationRequest(user: User, pet: Pet) : JQueryPromise<User> {
        var createRequest = new CreateAccountRequest();
        createRequest.pet = pet;
        createRequest.user = user;
        return ServiceMethods.createUser(createRequest);
    }

    public static submitCreatePet(pet: Pet): JQueryPromise<Pet> {
        return ServiceMethods.createPet(pet);
    }

    public static submitUserAccountUpdate(user: User): JQueryPromise<User> {
        return ServiceMethods.changeUserInformation(user);
    }

    public static getPetSpecies() {
        //ServiceMethods.getPetOptions(createRequest);
        return [{ petSpeciesConfigID: 1, petTypeName: "dog" }, { petSpeciesConfigID: 2, petTypeName: "cat" }, { petSpeciesConfigID: 3, petTypeName: "horrible clion" }]; //todo replace with server call, this is test data
    }

    public static getPetColors() {
        //ServiceMethods.getPetOptions(createRequest);
        return [{ petColorConfigID: 1, petColorName: "blue" }, { petColorConfigID: 2, petColorName: "red" }, { petColorConfigID: 3, petColorName: "octarine" }]; //todo replace with server call, this is test data

    }
}