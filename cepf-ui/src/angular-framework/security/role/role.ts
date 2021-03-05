import {BaseEntity} from '../../model/base-entity';

export class Role extends BaseEntity {
  code: string;
  name: string;
  active: boolean;
}
