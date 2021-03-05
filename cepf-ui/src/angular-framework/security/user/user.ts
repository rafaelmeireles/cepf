import {BaseEntity} from '../../model/base-entity';

import {Role} from '../role/role';

export class User extends BaseEntity {

  username: string;
  password: string;
  newPassword: string;
  active: boolean;
  roles: Role[];
  cpf: string;
  email: string;
  token: string;

  constructor(username: string, password: string, active?: boolean, cpf?: string, email?: string) {
    super();
    this.username = username;
    this.password = password;
    this.active = active;
    this.cpf = cpf;
    this.email = email;
  }
}
