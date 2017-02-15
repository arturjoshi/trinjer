import {Serializable} from "./serialization.interface";
/**
 * Created by Andrew Zelenskiy on 23.01.2017.
 */

export interface ProjectDTO extends Serializable{
    id: number;
    name: string;
    isVisible: boolean;
}