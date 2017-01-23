import {Serializable} from "./serialization.interface";
/**
 * Created by Andrew Zelenskiy on 23.01.2017.
 */

export interface ProjectDTO extends Serializable{
    name: string;
    isVisible: boolean;
}