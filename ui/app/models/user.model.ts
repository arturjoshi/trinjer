/**
 * Created by xoll on 08.01.2017.
 */

export class User{
    private constructor(
        private username: string,
        private email: string,
        private password: string,
    ){}

    public static getNewUser(){
        return new User('', '','');
    }
}