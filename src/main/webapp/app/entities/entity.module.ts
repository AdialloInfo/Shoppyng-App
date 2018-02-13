import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { ShoppyngAppProductModule } from './product/product.module';
import { ShoppyngAppPurchaseOrderModule } from './purchase-order/purchase-order.module';
import { ShoppyngAppShoppingCartModule } from './shopping-cart/shopping-cart.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    imports: [
        ShoppyngAppProductModule,
        ShoppyngAppPurchaseOrderModule,
        ShoppyngAppShoppingCartModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ShoppyngAppEntityModule {}
