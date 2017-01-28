/**
 * Created by Andrew Zelenskiy on 17.01.2017.
 */

export class AccountTokenDTO{
    id: number;
    username: string;
    email: string;
    token: string;

    public static getFromJSON(json: Object): AccountTokenDTO{
        let dto = new AccountTokenDTO();
        dto.id = json['id'] || null;
        dto.username = json['username'] || null;
        dto.email = json['email'] || null;
        dto.token = json['token'] || null;

        return dto;
    }
}