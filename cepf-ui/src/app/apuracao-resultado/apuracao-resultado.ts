import {BaseEntity} from '../../angular-framework/model/base-entity';
import {Investidor} from '../investidor/investidor';

export class ApuracaoResultado extends BaseEntity {
  periodoInicial: Date;
  periodoFinal: Date;
  investidor: Investidor;
}
