import { BaseEntity } from './../../shared';

export class ShoppingCart implements BaseEntity {
    constructor(
        public id?: number,
        public quantity?: number,
        public price?: number,
        public purchaseOrder?: BaseEntity,
        public product?: BaseEntity,
    ) {
    }
}
