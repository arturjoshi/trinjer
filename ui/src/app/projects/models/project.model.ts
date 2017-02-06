import {ProjectDTO} from "./project.interface";
import {Serializable} from "./serialization.interface";
/**
 * Created by Andrew Zelenskiy on 23.01.2017.
 */

export class Project implements ProjectDTO, Serializable{
    name: string;
    isVisible: boolean;

    constructor(name: string, isVisible: boolean = false){
        this.name = name;
        this.isVisible = isVisible;
    }

    serialize(): string{
        let json = {
            name: this.name,
            isVisible: this.isVisible
        };

        return JSON.stringify(json);
    }
}