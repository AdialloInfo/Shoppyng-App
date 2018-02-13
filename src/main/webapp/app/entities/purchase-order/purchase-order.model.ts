import { BaseEntity, User } from './../../shared';

export class PurchaseOrder implements BaseEntity {
    constructor(
        public id?: number,
        public order_date?: any,
        public user?: User,
    ) {
    }
}
