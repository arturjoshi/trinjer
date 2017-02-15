import {ProjectDTO} from "./project.interface";
import {Serializable} from "./serialization.interface";
/**
 * Created by Andrew Zelenskiy on 23.01.2017.
 */

export class Project implements ProjectDTO, Serializable{
    constructor(public id: number, public name: string, public isVisible: boolean = false){
    }

    serialize(): string{
        let json = {
            id: this.id,
            name: this.name,
            isVisible: this.isVisible
        };

        return JSON.stringify(json);
    }
}