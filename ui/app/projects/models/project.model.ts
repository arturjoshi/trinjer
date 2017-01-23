import {ProjectDTO} from "./project.interface";
import {Serializable} from "./serialization.interface";
/**
 * Created by Andrew Zelenskiy on 23.01.2017.
 */

export class Project implements ProjectDTO, Serializable{
    private _name: string;
    private _isVisible: boolean;

    get name(): string {
        return this._name;
    }

    get isVisible(): boolean {
        return this._isVisible;
    }

    serialize(): string{
        let json = {
            name: this.name,
            isVisible: this.isVisible
        };

        return JSON.stringify(json);
    }
}