import {User} from '../../angular-framework/security/user/user';

import {Investidor} from '../investidor/investidor';

export class UserApplication extends User {

  investidor: Investidor;

  constructor(username?: string, password?: string, active?: boolean, cpf?: string) {
    super(username, password, active, cpf);
  }
}
